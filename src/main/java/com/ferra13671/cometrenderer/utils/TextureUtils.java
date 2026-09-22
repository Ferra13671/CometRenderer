package com.ferra13671.cometrenderer.utils;

import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.function.Consumer;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
@UtilityClass
public class TextureUtils {

    public void checkNode(Node node, Consumer<Node> consumer) {
        consumer.accept(node);

        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); i++)
            consumer.accept(list.item(i));
    }

    public ByteBuffer readStream(InputStream stream) throws IOException {
        ReadableByteChannel rbChannel = Channels.newChannel(stream);
        if (rbChannel instanceof SeekableByteChannel sbChannel) {
            return readChannel(rbChannel, (int) sbChannel.size() + 1);
        } else
            return readChannel(rbChannel, 8192);
    }

    public ByteBuffer readChannel(ReadableByteChannel channel, int bufSize) throws IOException {
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(bufSize);

        try {
            while(channel.read(byteBuffer) != -1) {
                if (!byteBuffer.hasRemaining())
                    byteBuffer = MemoryUtil.memRealloc(byteBuffer, byteBuffer.capacity() * 2);
            }

            return byteBuffer;
        } catch (IOException var4) {
            MemoryUtil.memFree(byteBuffer);
            throw var4;
        }
    }

    public void tryGenerate(ByteBuffer buffer, InputStream stream, Consumer<MemoryStack> consumer) throws IOException {
        try {
            MemoryStack memoryStack = MemoryStack.stackPush();
            try {
                consumer.accept(memoryStack);
            } catch (Throwable var9) {
                try {
                    memoryStack.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }

                throw var9;
            }

            memoryStack.close();
        } finally {
            MemoryUtil.memFree(buffer);
            if (stream != null)
                stream.close();
        }
    }

    public byte getRed(int rgba) {
        return (byte) ((rgba >> 16) & 0xFF);
    }

    public byte getGreen(int rgba) {
        return (byte) ((rgba >> 8) & 0xFF);
    }

    public byte getBlue(int rgba) {
        return (byte) (rgba & 0xFF);
    }

    public byte getAlpha(int rgba) {
        return (byte) ((rgba >> 24) & 0xFF);
    }
}
