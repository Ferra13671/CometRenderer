package com.ferra13671.cometrenderer.minecraft.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface AfterInitializeCallback {

    Event<AfterInitializeCallback> EVENT = EventFactory.createArrayBacked(
            AfterInitializeCallback.class,
            callbacks -> () -> {
                for (AfterInitializeCallback callback : callbacks)
                    callback.onAfterInitialize();
            }
    );

    void onAfterInitialize();
}
