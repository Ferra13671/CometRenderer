package com.ferra13671.cometrenderer.texture.loader;

import com.ferra13671.cometrenderer.texture.ColorMode;
import com.ferra13671.cometrenderer.utils.TextureUtils;
import com.ferra13671.cometrenderer.texture.GLTextureInfo;
import org.apiguardian.api.API;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicReference;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public interface TextureLoader<T> {
    TextureLoader<InputStream> INPUT_STREAM = (path, colorMode) -> {
        ByteBuffer buffer = TextureUtils.readStream(path);
        buffer.rewind();

        AtomicReference<GLTextureInfo> glTextureInfo = new AtomicReference<>(null);

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer xBuffer = memoryStack.mallocInt(1);
            IntBuffer yBuffer = memoryStack.mallocInt(1);
            IntBuffer channelsBuffer = memoryStack.mallocInt(1);
            ByteBuffer byteBuffer = STBImage.stbi_load_from_memory(buffer, xBuffer, yBuffer, channelsBuffer, colorMode == ColorMode.RGBA ? 4 : 3);

            if (byteBuffer != null)
                glTextureInfo.set(new GLTextureInfo(byteBuffer, xBuffer.get(0), yBuffer.get(0), true));
        } finally {
            MemoryUtil.memFree(buffer);
            if (path != null)
                path.close();
        }

        return glTextureInfo.get();
    };
    TextureLoader<String> IN_JAR = (path, colorMode) -> INPUT_STREAM.load(TextureLoader.class.getClassLoader().getResourceAsStream(path), colorMode);
    TextureLoader<URL> URL = (path, colorMode) -> INPUT_STREAM.load(path.openStream(), colorMode);
    TextureLoader<BufferedImage> BUFFERED_IMAGE = (path, colorMode) -> {
        GLTextureInfo glTextureInfo;

        int[] pixels = new int[path.getWidth() * path.getHeight()];
        path.getRGB(0, 0, path.getWidth(), path.getHeight(), pixels, 0, path.getWidth());

        ByteBuffer byteBuffer = MemoryUtil.memAlloc((path.getWidth() * path.getHeight() * 4));

        for (int y = 0; y < path.getHeight(); y++) {
            for (int x = 0; x < path.getWidth(); x++) {
                int pixel = pixels[y * path.getWidth() + x];
                byteBuffer.put(TextureUtils.getRed(pixel));
                byteBuffer.put(TextureUtils.getGreen(pixel));
                byteBuffer.put(TextureUtils.getBlue(pixel));
                byteBuffer.put(TextureUtils.getAlpha(pixel));
            }
        }
        byteBuffer.flip();

        glTextureInfo = new GLTextureInfo(byteBuffer, path.getWidth(), path.getHeight(), false);

        return glTextureInfo;
    };

    GLTextureInfo load(T path, ColorMode colorMode) throws Exception;
}
