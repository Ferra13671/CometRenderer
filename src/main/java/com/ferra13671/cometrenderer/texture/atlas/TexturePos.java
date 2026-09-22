package com.ferra13671.cometrenderer.texture.atlas;

import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public record TexturePos(int x1, int y1, int x2, int y2) {

    public TextureBorder normalize(int width, int height) {
        return new TextureBorder(
                (float) this.x1 / width,
                (float) this.y1 / height,
                (float) this.x2 / width,
                (float) this.y2 / height
        );
    }
}
