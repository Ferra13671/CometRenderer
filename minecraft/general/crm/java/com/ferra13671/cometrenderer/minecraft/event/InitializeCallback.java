package com.ferra13671.cometrenderer.minecraft.event;

import com.ferra13671.cometrenderer.Config;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.1")
public interface InitializeCallback {

    Event<InitializeCallback> EVENT = EventFactory.createArrayBacked(
            InitializeCallback.class,
            callbacks -> c -> {
                for (InitializeCallback callback : callbacks)
                    callback.onInitialize(c);
            }
    );

    void onInitialize(Config.ConfigBuilder configBuilder);
}
