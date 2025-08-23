package com.ferra13671.cometrenderer.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;

public abstract class IncludeRendererTask extends DefaultTask {
    public Path libraryPath;

    @OutputDirectory
    public abstract DirectoryProperty getOutputDir();

    @TaskAction
    public void include() throws Exception {
        File sourceDir = getOutputDir().get().getAsFile();
        sourceDir.mkdir();

        JarFile jarFile = new JarFile(libraryPath.toFile());
        jarFile.stream().forEach(entry -> {
            if (entry.getName().endsWith(".java")) {
                try {
                    includeClass(sourceDir, entry.getName(), jarFile.getInputStream(entry));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void includeClass(File sourceDir, String path, InputStream inputStream) throws Exception {
        Path classFile = new File(sourceDir, path).toPath();
        classFile.toFile().mkdirs();
        Files.deleteIfExists(classFile);
        if (!Files.exists(classFile))
            Files.createFile(classFile);
        OutputStream outputStream = Files.newOutputStream(classFile);
        outputStream.write(inputStream.readAllBytes());
        outputStream.close();
        inputStream.close();
    }
}
