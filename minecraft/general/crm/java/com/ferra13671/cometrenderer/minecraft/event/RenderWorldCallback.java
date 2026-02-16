package com.ferra13671.cometrenderer.minecraft.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RenderWorldCallback {

    Event<RenderWorldCallback> EVENT = EventFactory.createArrayBacked(
            RenderWorldCallback.class,
            callbacks -> () -> {
                for (RenderWorldCallback callback : callbacks)
                    callback.onRenderWorld();
            }
    );

    void onRenderWorld();
}
