package com.ferra13671.cometrenderer.texture;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferInfo;
import com.ferra13671.cometrenderer.device.GLDevice;
import com.ferra13671.cometrenderer.device.directstate.DirectStateManager;
import com.ferra13671.cometrenderer.texture.loader.TextureLoader;
import lombok.Getter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

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

    GLTexture(String name, ColorMode colorMode, GLTextureInfo glTextureInfo) {
        this.name = name;
        this.colorMode = colorMode;

        create(glTextureInfo);
    }

    void create(GLTextureInfo glTextureInfo) {
        GLDevice device = CometRenderer.getDevice();

        this.id = CometRenderer.getDevice().createTexture();

        prepareDefaultTextureParameters(device.getDirectStateManager());

        this.width = glTextureInfo.width();
        this.height = glTextureInfo.height();

        device.getDirectStateManager().textureStorage(this);

        ByteBuffer pixels = glTextureInfo.pixels();
        if (pixels != null) {
            prepareDefaultPixelStore();

            device.getDirectStateManager().textureImage(this, pixels);

            if (glTextureInfo.usingStb())
                nstbi_image_free(MemoryUtil.memAddress(pixels));
            else
                MemoryUtil.memFree(pixels);
        }
    }

    /**
     * Создаёт копию текущей текстуры и "обрезает" её.
     */
    public GLTexture cutTexture(float u1, float v1, float u2, float v2) {
        int cutWidth = (int) ((u2 - u1) * this.width);
        int cutHeight = (int) ((v2 - v1) * this.height);

        GLTexture texture = builder()
                .name(this.name.concat(String.format("_cut_%s_%s", cutWidth, cutHeight)))
                .info(cutWidth, cutHeight)
                .colorMode(this.colorMode)
                .build();

        FramebufferImpl framebuffer = new FramebufferImpl(FramebufferInfo.builder()
                .name("cut framebuffer")
                .width(1)
                .height(1)
                .useStencil(false)
                .useDepth(false)
                .build()
        );

        framebuffer.getColorTexture().delete();
        framebuffer.setColorTexture(this);
        framebuffer.bind(true);

        CometRenderer.getDevice().getDirectStateManager().copyTexture(
                texture,
                (int) (u1 * this.width),
                (int) (v1 * this.height),
                texture.width,
                texture.height
        );

        framebuffer.setColorTexture(null);
        framebuffer.delete();

        return texture;
    }

    /**
     * Рисует необходимую текстуру в текущую.
     */
    public GLTexture drawImage(GLTexture texture, int x, int y) {
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(texture.getWidth() * texture.getHeight() * texture.getColorMode().pixelSize());

        GLDevice device = CometRenderer.getDevice();

        device.getDirectStateManager().copyTextureToBuffer(texture, byteBuffer);
        byteBuffer.flip();

        device.getDirectStateManager().textureImage(
                this,
                Math.min(getWidth(), x),
                Math.min(getHeight(), y),
                Math.min(texture.getWidth(), getWidth() - x),
                Math.min(texture.getHeight(), getHeight() - y),
                byteBuffer
        );

        setFiltering(this.filtering);
        setWrapping(this.wrapping);

        MemoryUtil.memFree(byteBuffer);

        return this;
    }

    private void prepareDefaultTextureParameters(DirectStateManager dsm) {
        dsm.textureParameterInt(this, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        dsm.textureParameterInt(this, GL12.GL_TEXTURE_MIN_LOD, 0);
        dsm.textureParameterInt(this, GL12.GL_TEXTURE_MAX_LOD, 0);
        dsm.textureParameterFloat(this, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
    }

    private void prepareDefaultPixelStore() {
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
        DirectStateManager dsm = CometRenderer.getDevice().getDirectStateManager();

        if (filtering != null) {
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_MAG_FILTER, filtering.id);
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_MIN_FILTER, filtering.id);
        } else if (this.filtering != null) {
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_MAG_FILTER, TextureFiltering.DEFAULT.id);
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_MIN_FILTER, TextureFiltering.DEFAULT.id);
        }

        this.filtering = filtering;
    }

    @Override
    public void setWrapping(TextureWrapping wrapping) {
        DirectStateManager dsm = CometRenderer.getDevice().getDirectStateManager();

        if (wrapping != null) {
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_WRAP_S, wrapping.id);
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_WRAP_T, wrapping.id);
        } else if (this.wrapping != null) {
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_WRAP_S, TextureWrapping.DEFAULT.id);
            dsm.textureParameterInt(this, GL11.GL_TEXTURE_WRAP_T, TextureWrapping.DEFAULT.id);
        }

        this.wrapping = wrapping;
    }

    public static <T> GLTextureBuilder<T> builder(TextureLoader<T> loader) {
        return new GLTextureBuilder<>(loader);
    }

    public static GLTextureBuilder<?> builder() {
        return builder(null);
    }
}
