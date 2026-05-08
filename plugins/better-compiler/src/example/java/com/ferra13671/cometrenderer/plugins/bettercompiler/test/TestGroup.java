package com.ferra13671.cometrenderer.plugins.bettercompiler.test;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;

public class TestGroup {
    private final String name;
    private final Test[] tests;

    public TestGroup(String name, Test... tests) {
        this.name = name;
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

    public void startAndLog(File file) {
        try {
            file.getParentFile().mkdirs();

            if (file.exists())
                file.delete();

            file.createNewFile();

            OutputStream outputStream = Files.newOutputStream(file.toPath());
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
            startAndLog(writer);

            writer.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAndLog(PrintWriter writer) {
        writer.println(String.format("Test group '%s'\nDate '%s'\n\n\n", this.name, Date.from(Instant.now())));
        writer.println("\n\n\n\n");

        for (Test test : this.tests)
            test.startAndLog(writer);
    }
}
