package com.ferra13671.cometrenderer.program.compile;

/*
 * Статус компиляции чего-либо
 * SUCCESSFUL — компиляция прошла успешно
 * FAILURE — при компиляции произошла ошибка
 */
public enum CompileStatus {
    SUCCESSFUL(1),
    FAILURE(0);

    public final int id;

    CompileStatus(int id) {
        this.id = id;
    }

    /*
     * Возвращает статус по его айди
     */
    public static CompileStatus fromStatusId(int id) {
        for (CompileStatus compileStatus : values())
            if (compileStatus.id == id)
                return compileStatus;

        return null;
    }
}
