package com.ferra13671.cometrenderer.minecraft.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface BeforeInitializeCallback {

    Event<BeforeInitializeCallback> EVENT = EventFactory.createArrayBacked(
            BeforeInitializeCallback.class,
            callbacks -> () -> {
                for (BeforeInitializeCallback callback : callbacks)
                    callback.onBeforeInitialize();
            }
    );

    void onBeforeInitialize();
}
