<p align="left">
    <img src="https://raw.githubusercontent.com/Ferra13671/CometRenderer/main/logo.png" style="width: 50%" alt="logo">
</p>

Premium custom renderer for minecraft >=1.21.8.
CometRenderer has a well-optimized rendering system, extensive customization, and a fairly user-friendly API.
<p>
At the moment, the library depends on Minecraft, albeit only slightly. Despite this, in the future the library may become completely independent, thus it could be used as a renderer for, for example, your game.

## Gradle
```groovy
repositories {
    maven {
        name = "ferra13671-maven"
        url = "https://ferra13671.github.io/maven/"
    }
}

dependencies {
    implementation "com.ferra13671:comet-renderer:2.0"
    implementation "com.ferra13671:gl-texture-utils:1.7.1"
    
    //Plugins (optional)
    implementation "com.ferra13671:comet-renderer-minecraft:1.21.8-1.21.11-fabric-yarn-2.0"
    implementation "com.ferra13671:comet-renderer-better-exceptions:2.0"
    implementation "com.ferra13671:comet-renderer-post-effects:2.0"
    implementation "com.ferra13671:comet-renderer-shader-libraries:2.0"
}
```

## Example
For example check out `src/testmod/java/com/ferra13671/cometrenderer/test/TestMod.java`.  

## JavaDoc
JavaDoc is written entirely in Russian, but I may rewrite it into English in the future.