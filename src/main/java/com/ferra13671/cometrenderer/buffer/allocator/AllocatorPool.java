package com.ferra13671.cometrenderer.buffer.allocator;

import org.apiguardian.api.API;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Пул для кеширования и переиспользования нативных аллокаторов памяти ({@link IAllocator}).
 * <p>
 * Основная задача пула — избавить приложение от накладных расходов на системные вызовы
 * {@code malloc}/{@code free} при частом создании и удалении мешей. Вместо реального
 * освобождения памяти, аллокатор возвращается в пул и ожидает следующего использования.
 * <p>
 * <b>Как это работает:</b>
 * Пул создаёт заданное количество аллокаторов фиксированного размера ({@code allocatorSize}).
 * При вызове {@link #acquire(int)} пул:
 * <ul>
 *     <li>Выдаёт свободный аллокатор из кеша, если его размера достаточно;</li>
 *     <li>Создаёт <b>overflow-аллокатор</b> (временный, не кешируемый), если запрошенный размер
 *         больше {@code allocatorSize} или если все аллокаторы в пуле сейчас заняты.</li>
 * </ul>
 * <p>
 * <b>Жизненный цикл:</b>
 * Когда пользователь вызывает {@link IAllocator#close()} у аллокатора, полученного из пула:
 * <ul>
 *     <li>Обычный аллокатор возвращается в пул для повторного использования;</li>
 *     <li>Overflow-аллокатор реально освобождает нативную память (уничтожается).</li>
 * </ul>
 * <p>
 * <b>Потокобезопасность:</b> Класс полностью потокобезопасен.
 * <p>
 * <b>Закрытие пула:</b> При вызове {@link #close()} все аллокаторы, находящиеся внутри пула
 * (как свободные, так и используемые в данный момент), будут принудительно уничтожены.
 * Поэтому пул следует закрывать только тогда, когда приложение завершает работу и
 * не ожидает больше запросов на создание мешей.
 *
 * @see IAllocator
 */
@API(status = API.Status.MAINTAINED, since = "3.1")
public class AllocatorPool implements AutoCloseable {
    private final int allocatorSize;

    private final Deque<PooledAllocator> free;
    private final Set<PooledAllocator> inUse = new HashSet<>();
    private final Set<PooledAllocator> overflow = new HashSet<>();

    private boolean closed = false;

    public AllocatorPool(int poolSize, int allocatorSize) {
        if (poolSize < 1)
            throw new IllegalStateException("Pool size must be >= 1");
        if (allocatorSize < 0)
            throw new IllegalStateException("Allocator size must be >= 0");

        this.allocatorSize = allocatorSize;

        this.free = new ArrayDeque<>(poolSize);

        for (int i = 0; i < poolSize; i++)
            this.free.add(new PooledAllocator(allocatorSize, this));
    }

    /**
     * Запрашивает аллокатор с минимально необходимым размером.
     * <p>
     * Если в пуле есть подходящий свободный аллокатор, он будет переиспользован.
     * В противном случае будет создан временный (overflow) аллокатор, который
     * будет уничтожен при вызове {@link IAllocator#close()}.
     *
     * @param size минимальный требуемый размер памяти в байтах.
     * @return аллокатор, готовый к записи данных.
     * @throws IllegalStateException если пул уже был закрыт через {@link #close()}.
     */
    public synchronized IAllocator acquire(int size) {
        if (this.closed)
            throw new IllegalStateException("SourcePool closed");

        PooledAllocator allocator;
        if (this.allocatorSize >= size) {
            allocator = this.free.pollFirst();

            if (allocator == null) {
                allocator = new PooledAllocator(this.allocatorSize, this);

                this.overflow.add(allocator);
            } else
                allocator.acquire();
        } else {
            allocator = new PooledAllocator(size, this);
            this.overflow.add(allocator);
        }

        this.inUse.add(allocator);
        return allocator;
    }

    synchronized void release(PooledAllocator allocator) {
        if (this.closed) {
            allocator.destroy();
            return;
        }

        if (!this.inUse.remove(allocator))
            return;

        if (this.overflow.remove(allocator)) {
            allocator.destroy();
            return;
        }

        this.free.addLast(allocator);
    }

    /**
     * Закрывает пул и принудительно освобождает нативную память всех аллокаторов.
     * <p>
     * После вызова этого метода любые попытки получить новый
     * аллокатор приведут к исключению {@link IllegalStateException}.
     * Если на момент закрытия какие-то аллокаторы находились в работе, они
     * будут уничтожены, и дальнейшее обращение к ним вызовет ошибку.
     */
    @Override
    public synchronized void close() {
        if (this.closed)
            return;

        this.free.forEach(PooledAllocator::destroy);
        this.inUse.forEach(PooledAllocator::destroy);

        this.free.clear();
        this.overflow.clear();
        this.inUse.clear();

        this.closed = true;
    }
}
