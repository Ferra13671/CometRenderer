package com.ferra13671.cometrenderer.minecraft;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.nio.file.Paths;

public class PreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        System.load(Paths.get("renderdoc.dll").toAbsolutePath().toString());
    }
}
