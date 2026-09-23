package com.ferra13671.cometrenderer.texture.gif;

import com.ferra13671.cometrenderer.texture.GLTexture;
import com.ferra13671.cometrenderer.utils.Pair;
import com.ferra13671.cometrenderer.texture.loader.TextureLoader;
import lombok.AllArgsConstructor;
import org.apiguardian.api.API;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
@AllArgsConstructor
public enum Disposal {
    None((prevImage, imageData) -> {
        BufferedImage bufferedImage = new BufferedImage(prevImage.getWidth(), prevImage.getHeight(), prevImage.getType());
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.drawImage(prevImage, 0, 0, null);
        graphics.drawImage(imageData.right(), imageData.left()[0], imageData.left()[1], null);
        graphics.dispose();

        return bufferedImage;
    }, frames -> {}),
    ToBackground((frames, imageData) -> imageData.right(), frames -> {
        GLGifFrame frame = frames.getLast();
        frames.add(new GLGifFrame(
                GLTexture.builder(TextureLoader.BUFFERED_IMAGE)
                        .name(frame.texture().getName() + "-extra")
                        .info(new BufferedImage(frame.texture().getWidth(), frame.texture().getHeight(), frame.image().getType()))
                        .filtering(frame.texture().getFiltering())
                        .wrapping(frame.texture().getWrapping())
                        .build(),
                frame.image(),
                0
        ));
    }),
    ToPrevious((frames, imageData) -> imageData.right(), frames -> {
        GLGifFrame prevFrame = frames.get(frames.size() - 2);
        frames.add(new GLGifFrame(prevFrame.texture(), prevFrame.image(), 0));
    });

    public final BiFunction<BufferedImage, Pair<int[], BufferedImage>, BufferedImage> disposalFunction;
    public final Consumer<List<GLGifFrame>> extraConsumer;
}
