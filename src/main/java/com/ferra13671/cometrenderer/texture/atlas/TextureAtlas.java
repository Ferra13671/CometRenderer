package com.ferra13671.cometrenderer.texture.atlas;

import com.ferra13671.cometrenderer.texture.*;
import org.apiguardian.api.API;

import java.util.*;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public class TextureAtlas implements GLTex {
    private final GLTexture texture;
    private final HashMap<String, TextureBorder> borders = new HashMap<>();

    public TextureAtlas(String name, List<GLTexture> textures) {
        textures = new ArrayList<>(textures);
        textures.sort(Comparator.comparingInt(tex -> tex.getWidth() * tex.getHeight()));
        Collections.reverse(textures);

        TextureAtlasScheme scheme = calculateAtlasScheme(textures);

        this.texture = GLTexture.builder()
                .name(name)
                .info(scheme.textureWidth(), scheme.textureHeight())
                .build();

        scheme.poses().forEach((tex, texturePos) -> {
            this.texture.drawImage(tex, texturePos.x1(), texturePos.y1());
            this.borders.put(tex.getName(), texturePos.normalize(this.texture.getWidth(), this.texture.getHeight()));
        });
    }

    private static TextureAtlasScheme calculateAtlasScheme(List<GLTexture> textures) {
        HashMap<GLTexture, TexturePos> poses = new HashMap<>();

        int xLength = (int) Math.sqrt(textures.size());

        int maxHeight = 0;
        int maxWidth = 0;
        int x = 0;
        int y = 0;

        int i = 0;
        for (GLTexture tex : textures) {
            if (i % xLength == 0) {
                x = 0;
                y += maxHeight;
                maxHeight = 0;
            }

            poses.put(tex, new TexturePos(x, y, x + tex.getWidth(), y + tex.getHeight()));

            x += tex.getWidth();
            maxHeight = Math.max(maxHeight, tex.getHeight());
            maxWidth = Math.max(maxWidth, x);

            i++;
        }

        return new TextureAtlasScheme(maxWidth, y + maxHeight, poses);
    }

    public TextureBorder getBorder(GLTexture texture) {
        return this.borders.get(texture.getName());
    }

    public TextureBorder getBorder(String textureName) {
        return this.borders.get(textureName);
    }

    @Override
    public void delete() {
        this.texture.delete();
    }

    @Override
    public void bind() {
        this.texture.bind();
    }

    @Override
    public TextureFiltering getFiltering() {
        return this.texture.getFiltering();
    }

    @Override
    public void setFiltering(TextureFiltering textureFiltering) {
        this.texture.setFiltering(textureFiltering);
    }

    @Override
    public TextureWrapping getWrapping() {
        return this.texture.getWrapping();
    }

    @Override
    public void setWrapping(TextureWrapping textureWrapping) {
        this.texture.setWrapping(textureWrapping);
    }

    @Override
    public ColorMode getColorMode() {
        return this.texture.getColorMode();
    }

    @Override
    public int getWidth() {
        return this.texture.getWidth();
    }

    @Override
    public int getHeight() {
        return this.texture.getHeight();
    }

    @Override
    public int getId() {
        return this.texture.getId();
    }
}
