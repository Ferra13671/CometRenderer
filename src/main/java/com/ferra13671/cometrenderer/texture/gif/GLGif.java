package com.ferra13671.cometrenderer.texture.gif;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.texture.*;
import com.ferra13671.cometrenderer.texture.loader.GifLoader;
import com.ferra13671.cometrenderer.texture.loader.TextureLoader;
import com.ferra13671.cometrenderer.utils.Pair;
import com.ferra13671.cometrenderer.utils.TextureUtils;
import org.apiguardian.api.API;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public class GLGif implements GLTex {
    protected final List<GLGifFrame> frames = new ArrayList<>();

    protected GLGifFrame currentFrame;
    protected int currentFrameId = 0;

    protected long lastUpdateTime = System.currentTimeMillis();

    public GLGif(String name, InputStream inputStream) {
        try (inputStream; ImageInputStream stream = ImageIO.createImageInputStream(inputStream)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream);

                int numFrames = reader.getNumImages(true);
                BufferedImage prevImage = null;
                for (int i = 0; i < numFrames; i++) {
                    BufferedImage image = reader.read(i);
                    IIOMetadata metadata = reader.getImageMetadata(i);
                    Node nodeMetadata = metadata.getAsTree(
                            metadata.getNativeMetadataFormatName()
                    );

                    AtomicInteger delay = new AtomicInteger(100);
                    AtomicReference<int[]> offsets = new AtomicReference<>(new int[]{0, 0});
                    AtomicReference<Disposal> disposal = new AtomicReference<>(null);

                    TextureUtils.visitNodeAndDirectChildren(
                            nodeMetadata,
                            node -> {
                                if (node.getNodeName().equals("GraphicControlExtension")) {
                                    NamedNodeMap attrs = node.getAttributes();
                                    String delayStr = attrs.getNamedItem("delayTime").getNodeValue();

                                    delay.set(Integer.parseInt(delayStr) * 10);


                                    String d = attrs.getNamedItem("disposalMethod").getNodeValue();

                                    disposal.set(
                                            (d.equalsIgnoreCase("none") || d.equalsIgnoreCase("doNotDispose")) ?
                                                    Disposal.None
                                                    : d.equalsIgnoreCase("restoreToBackgroundColor") ?
                                                    Disposal.ToBackground
                                                    : d.equalsIgnoreCase("restoreToPrevious") ?
                                                    Disposal.ToPrevious
                                                    : Disposal.None
                                    );
                                }

                                if (node.getNodeName().equals("ImageDescriptor")) {
                                    NamedNodeMap attrs = node.getAttributes();

                                    offsets.set(new int[]{
                                            Integer.parseInt(attrs.getNamedItem("imageLeftPosition").getNodeValue()),
                                            Integer.parseInt(attrs.getNamedItem("imageTopPosition").getNodeValue())
                                    });
                                }
                            }
                    );


                    if (prevImage == null)
                        prevImage = image;
                    image = disposal.get().disposalFunction.apply(prevImage, new Pair<>(offsets.get(), image));

                    this.frames.add(
                            new GLGifFrame(
                                    GLTexture.builder(TextureLoader.BUFFERED_IMAGE)
                                        .name(name.concat("-frame-" + i))
                                        .info(image)
                                        .build(),
                                    image,
                                    delay.get())
                    );

                    disposal.get().extraConsumer.accept(this.frames);

                    prevImage = image;
                }

                reader.dispose();
            }

            this.currentFrame = this.frames.getFirst();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public void delete() {
        for (GLGifFrame frame : this.frames)
            frame.texture().delete();
    }

    @Override
    public void bind() {
        CometRenderer.getDevice().getPipelineStateManager().bindTexture(getId());
    }

    @Override
    public TextureFiltering getFiltering() {
        return this.currentFrame.texture().getFiltering();
    }

    @Override
    public void setFiltering(TextureFiltering textureFiltering) {
        for (GLGifFrame frame : this.frames)
            frame.texture().setFiltering(textureFiltering);
    }

    @Override
    public TextureWrapping getWrapping() {
        return this.currentFrame.texture().getWrapping();
    }

    @Override
    public void setWrapping(TextureWrapping textureWrapping) {
        for (GLGifFrame frame : this.frames)
            frame.texture().setWrapping(textureWrapping);
    }

    @Override
    public ColorMode getColorMode() {
        return this.currentFrame.texture().getColorMode();
    }

    @Override
    public int getWidth() {
        return this.currentFrame.texture().getWidth();
    }

    @Override
    public int getHeight() {
        return this.currentFrame.texture().getHeight();
    }

    @Override
    public int getId() {
        updateFrame();
        return this.currentFrame.texture().getId();
    }

    private void updateFrame() {
        while (this.lastUpdateTime + this.currentFrame.delay() <= System.currentTimeMillis())
            setCurrentFrame(this.currentFrameId++ % (this.frames.size() - 1));
    }

    private void setCurrentFrame(int frameId) {
        this.lastUpdateTime = this.lastUpdateTime + this.currentFrame.delay();
        this.currentFrame = this.frames.get(frameId);
    }

    public static <T> GLGifBuilder<T> builder(GifLoader<T> loader) {
        return new GLGifBuilder<>(loader);
    }
}
