<p align="left">
    <img src="https://raw.githubusercontent.com/Ferra13671/CometRenderer/main/logo.png" style="width: 50%" alt="logo">
</p>

CometRenderer is an optimized, customized, easy-to-use, and full independent rendering system for OpenGL.
This rendering system provides a wide range of tools that can be used to easily render your projects.
<p>

## Official CometRenderer plugins
Several official plugins were also created for the project, expanding the functionality of the rendering system:
* better-exceptions
* post-effects
* shader-libraries
* minecraft

## Documentation
CometRenderer has its own documentation, which you can read on the <a href="https://ferra13671-dev.gitbook.io/projects/main/cometrenderer-documentation">website</a>.

## Gradle
```groovy
repositories {
    maven {
        name = "ferra13671-maven"
        url = "https://ferra13671.github.io/maven/"
    }
}

dependencies {
    implementation "com.ferra13671:comet-renderer:2.3"
    implementation "com.ferra13671:gl-texture-utils:1.7.4-HOTFIX-1"
    
    //Plugins (optional)
    implementation "com.ferra13671:comet-renderer-minecraft:version"//check need version in https://github.com/Ferra13671/maven/tree/main/com/ferra13671/comet-renderer-minecraft
    implementation "com.ferra13671:comet-renderer-better-exceptions:2.3"
    implementation "com.ferra13671:comet-renderer-post-effects:2.3"
    implementation "com.ferra13671:comet-renderer-shader-libraries:2.3"
}
```

## Minecraft example
For example to use in minecraft check out `minecraft/{plugin-version}/src/example/com/ferra13671/cometrenderer/test/TestMod.java`.  

## JavaDoc
JavaDoc is written entirely in Russian, but I may rewrite it into English in the future.