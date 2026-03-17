<p align="left">
    <img src="https://raw.githubusercontent.com/Ferra13671/CometRenderer/main/logo.png" style="width: 50%" alt="logo">
</p>

CometRenderer is an optimized, customized, easy-to-use, and full independent rendering system for OpenGL.
This rendering system provides a wide range of tools that can be used to easily render your projects.
<p>

## Official CometRenderer plugins
Several official plugins were also created for the project, expanding the functionality of the rendering system:
* better-compiler
* better-exceptions
* post-effects

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
    //If you want use CRM in your Minecraft mod:
        modImplementation "com.ferra13671:crm:${crm_version}"
        //Include CRM as a Jar-in-Jar dependency (optional)
        include "com.ferra13671:crm:${crm_version}"
    //------------//
    //Else if you don't want to use CRM or you not use CometRenderer in Minecraft:
        implementation "com.ferra13671:comet-renderer:2.7"
        implementation "com.ferra13671:gl-texture-utils:1.7.4-HOTFIX-1"
    //------------//
    
    //Plugins (optional)
    implementation "com.ferra13671:comet-renderer-better-compiler:2.7" //By default it is in CRM
    implementation "com.ferra13671:comet-renderer-better-exceptions:2.7"
    implementation "com.ferra13671:comet-renderer-post-effects:2.7"
}
```

## CRM
CRM (CometRenderer Mod) is a Minecraft library mod that provides various utilities for using CometRenderer within Minecraft.
<p>
You can learn more about it in the <a href="https://ferra13671-dev.gitbook.io/projects/main/cometrenderer-documentation/crm/about">documentation</a>.

## Javadoc
Since this project was previously private, the Javadoc is written in Russian (in some classes, the Javadoc is missing because I stopped adding it).
<p>
I plan to completely rewrite the Javadoc into English, but given my laziness, that won't happen anytime soon.