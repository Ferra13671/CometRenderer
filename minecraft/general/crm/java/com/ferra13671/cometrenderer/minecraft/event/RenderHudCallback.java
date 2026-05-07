package com.ferra13671.cometrenderer.minecraft.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.6")
public interface RenderHudCallback {

    Event<RenderHudCallback> EVENT = EventFactory.createArrayBacked(
            RenderHudCallback.class,
            callbacks -> () -> {
                for (RenderHudCallback callback : callbacks)
                    callback.onRenderHud();
            }
    );

    void onRenderHud();
}
