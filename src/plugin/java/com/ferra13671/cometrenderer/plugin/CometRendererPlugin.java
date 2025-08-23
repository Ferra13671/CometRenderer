package com.ferra13671.cometrenderer.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

import java.nio.file.Paths;

public class CometRendererPlugin implements Plugin<Project> {
    private TaskProvider<IncludeRendererTask> task;

    @Override
    public void apply(Project project) {
        CometRendererPluginExtension extension = project.getExtensions().create("cometrenderer", CometRendererPluginExtension.class);

        task = project.getTasks()
                .register("includeRenderTask", IncludeRendererTask.class, t ->
                        t.getOutputDir().set(project.getLayout().getBuildDirectory().dir("generated/sources/cometrenderer/main/"))
                );

        project.afterEvaluate(p -> p.getPlugins().withId("java", plugin -> {
            task.get().libraryPath = Paths.get(extension.getLibraryPath());
            try {
                task.get().include();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            p.getConvention().getPlugin(org.gradle.api.plugins.JavaPluginConvention.class)
                    .getSourceSets().getByName("main")
                    .getJava().srcDir(task.get().getOutputDir());
            p.getTasks().getByName("compileJava").dependsOn(task);
        }));
    }
}
