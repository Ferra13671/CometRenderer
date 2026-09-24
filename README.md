<p align="center">
    <img src="https://raw.githubusercontent.com/Ferra13671/CometRenderer/main/logo.png" style="width: 50%" alt="logo">
</p>

<p align="center">
    <img src="https://img.shields.io/github/downloads/Ferra13671/CometRenderer/total" alt="Downloads"/>
    <a href="https://github.com/Ferra13671/CometRenderer/blob/main/LICENSE.md"> <img src="https://img.shields.io/badge/license-EPL%202.0-blue.svg" alt="License"/> </a> 
    <a href="https://github.com/Ferra13671/CometRenderer/releases"> <img src="https://img.shields.io/github/v/release/Ferra13671/CometRenderer" alt="Release"/> </a>
    <a href="https://ferra13671-dev.gitbook.io/projects/main/cometrenderer-documentation"> <img src="https://img.shields.io/badge/docs-GitBook-brightgreen" alt="Documentation"/> </a>
</p>

CometRenderer is an optimized, customized, easy-to-use, and full independent rendering system for OpenGL.
This rendering system provides a wide range of tools that can be used to easily render your projects.
<p>

## Official CometRenderer plugins
Several official plugins were also created for the project, expanding the functionality of the rendering system:
* better-compiler
* post-effects
* lwjgl

## Documentation
CometRenderer has its own documentation, which you can read on the <a href="https://ferra13671-dev.gitbook.io/projects/main/cometrenderer-documentation">website</a>.

## Installing
### Gradle
Add the Ferra13671 repository to build.gradle:
```groovy
repositories {
    maven {
        name = "ferra13671-maven"
        url = "https://ferra13671.github.io/maven/"
    }
}
```
### For standalone OpenGL apps
```groovy
dependencies {
    implementation "com.ferra13671:comet-renderer:${comet_renderer_version}"

    //Recommended plugin
    implementation "com.ferra13671:comet-renderer-glfw:${comet_renderer_version}"
}
```
### For Minecraft fabric mods (from CRM)
```groovy
dependencies {
    modImplementation "com.ferra13671:crm:${crm_version}"

    //Include CRM as a Jar-in-Jar dependency (optional)
    include "com.ferra13671:crm:${crm_version}"
}
```
### Plugins
```groovy
dependencies {
    implementation "com.ferra13671:comet-renderer-better-compiler:${comet_renderer_version}" //By default it is in CRM
    implementation "com.ferra13671:comet-renderer-post-effects:${comet_renderer_version}"
    implementation "com.ferra13671:comet-renderer-glfw:${comet_renderer_version}"
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