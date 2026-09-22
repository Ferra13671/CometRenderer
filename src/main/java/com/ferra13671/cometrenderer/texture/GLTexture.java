package com.ferra13671.cometrenderer.texture;

import com.ferra13671.cometrenderer.CometRenderer;
import lombok.Getter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.Random;

import static org.lwjgl.stb.STBImage.nstbi_image_free;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
@Getter
public class GLTexture implements GLTex {
    protected int id;

    protected final String name;
    protected int width;
    protected int height;

    protected TextureFiltering filtering = null;
    protected TextureWrapping wrapping = null;
    protected ColorMode colorMode;

    protected GLTexture(String name, ColorMode colorMode) {
        this.name = name;
        this.colorMode = colorMode;
    }

    private GLTexture create(GLTextureInfo glTextureInfo) {
        this.id = CometRenderer.getDevice().createTexture();

        CometRenderer.getDevice().getPipelineStateManager().bindTexture(this.id);
        prepareDefaultTextureParameters();

        this.width = glTextureInfo.width();
        this.height = glTextureInfo.height();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, this.colorMode.internalFormatId(), this.width, this.height, 0, this.colorMode.externalFormatId(), this.colorMode.dataType(), (ByteBuffer) null);

        if (glTextureInfo.pixels() != null) {
            long bufferAddress = MemoryUtil.memAddress(glTextureInfo.pixels());

            prepareDefaultPixelStore();
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, this.width, this.height, this.colorMode.externalFormatId(), this.colorMode.dataType(), bufferAddress);

            if (glTextureInfo.usingStb())
                nstbi_image_free(bufferAddress);
            else
                MemoryUtil.memFree(glTextureInfo.pixels());
        }

        return this;
    }

    /**
     * Создаёт копию текущей текстуры и "обрезает" её.
     */
    public GLTexture cutTexture(float u1, float v1, float u2, float v2) {
        GLTexture texture = new GLTexture(this.name.concat(String.format("_sub_%s", new Random().nextInt())), this.colorMode);

        texture.id = CometRenderer.getDevice().createTexture();

        int fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, this.id, 0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException(String.format("An error occurred while creating FrameBuffer for subtexture '%s'.", texture.getName()));

        texture.width = (int) ((u2 - u1) * this.width);
        texture.height = (int) ((v2 - v1) * this.height);

        CometRenderer.getDevice().getPipelineStateManager().bindTexture(texture.getId());
        prepareDefaultTextureParameters();
        texture.setFiltering(this.filtering);
        texture.setWrapping(this.wrapping);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, texture.colorMode.internalFormatId(), texture.width, texture.height, 0, texture.colorMode.externalFormatId(), texture.colorMode.dataType(), (ByteBuffer) null);
        prepareDefaultPixelStore();

        GL11.glCopyTexSubImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                0,
                0,
                (int) (u1 * this.width),
                (int) (v1 * this.height),
                texture.width,
                texture.height);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL30.glDeleteFramebuffers(fbo);

        return texture;
    }

    /**
     * Рисует необходимую текстуру в текущую.
     */
    public GLTexture drawImage(GLTexture texture, int x, int y) {
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(texture.getWidth() * texture.getHeight() * texture.getColorMode().pixelSize());

        CometRenderer.getDevice().getPipelineStateManager().bindTexture(texture.getId());
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, texture.getColorMode().externalFormatId(), texture.getColorMode().dataType(), byteBuffer);
        byteBuffer.flip();

        CometRenderer.getDevice().getPipelineStateManager().bindTexture(this.getId());
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, texture.getWidth(), texture.getHeight(), texture.getColorMode().externalFormatId(),  texture.getColorMode().dataType(), MemoryUtil.memAddress(byteBuffer));

        setFiltering(this.filtering);
        setWrapping(this.wrapping);

        MemoryUtil.memFree(byteBuffer);

        return this;
    }

    private static void prepareDefaultTextureParameters() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 33085, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 33082, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 33083, 0);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34049, 0.0F);
    }

    private static void prepareDefaultPixelStore() {
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
    }

    @Override
    public void delete() {
        CometRenderer.getDevice().deleteTexture(this.getId());
    }

    @Override
    public void bind() {
        CometRenderer.getDevice().getPipelineStateManager().bindTexture(this.getId());
    }

    @Override
    public void setFiltering(TextureFiltering filtering) {
        if (filtering != null) {
            bind();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtering.id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtering.id);
        } else if (this.filtering != null) {
            bind();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, TextureFiltering.DEFAULT.id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, TextureFiltering.DEFAULT.id);
        }

        this.filtering = filtering;
    }

    @Override
    public void setWrapping(TextureWrapping wrapping) {
        if (wrapping != null) {
            bind();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapping.id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapping.id);
        } else if (this.wrapping != null) {
            bind();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, TextureWrapping.DEFAULT.id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, TextureWrapping.DEFAULT.id);
        }

        this.wrapping = wrapping;
    }

    public static GLTexture of(String name, ColorMode colorMode, GLTextureInfo glTextureInfo) {
        return new GLTexture(name, colorMode).create(glTextureInfo);
    }
}
