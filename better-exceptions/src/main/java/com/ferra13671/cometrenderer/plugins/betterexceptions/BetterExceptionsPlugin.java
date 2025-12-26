package com.ferra13671.cometrenderer.plugins.betterexceptions;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.tag.Registry;
import com.ferra13671.cometrenderer.tag.Tag;

import java.util.Map;

public class BetterExceptionsPlugin {
    private static final String exceptionText = """
    
    ----------------------------------------------
            CometRenderer error occurred!
    ----------------------------------------------
                      Whats wrong?
    
    %s
    ----------------------------------------------
                       Details:
    
    %s
    ----------------------------------------------
                  Possible reasons:
    
    %s
    ----------------------------------------------
                 Possible solutions:
    
    %s
    ----------------------------------------------
    """;
    public static final Tag<Map<Class<? extends CometException>, PrintInfoCreator<?>>> EXCEPTIONS_PRINT_INFO = new Tag<>("exceptions-print-info");
    public static final Tag<String> DESCRIPTION = new Tag<>("description");
    public static final Tag<String> DETAILS = new Tag<>("details");
    public static final Tag<String[]> REASONS = new Tag<>("reasons");
    public static final Tag<String[]> SOLUTIONS = new Tag<>("solutions");

    public static void init() {
        if (!CometRenderer.getRegistry().contains(CometTags.INITIALIZED))
            throw new IllegalStateException("CometRenderer is not initialized!");

        CometRenderer.getRegistry().set(EXCEPTIONS_PRINT_INFO, ExceptionsPrintInfo.getMap());
        CometRenderer.getRegistry().set(CometTags.EXCEPTION_PROVIDER, exception -> {
            Map<Class<? extends CometException>, PrintInfoCreator<?>> map = CometRenderer.getRegistry().get(EXCEPTIONS_PRINT_INFO).orElseThrow().getValue();
            if (map.containsKey(exception.getClass()))
                printException(((PrintInfoCreator<CometException>) map.get(exception.getClass())).create(exception));
            else
                CometRenderer.getLogger().error(String.format("Unable to find information about exception '%s'.", exception.getClass()));

            CometRenderer.throwOrLogException(exception);
        });
    }

    private static void printException(Registry registry) {
        CometRenderer.getLogger().error(
                String.format(
                        exceptionText,
                        registry.get(DESCRIPTION).orElseThrow().getValue(),
                        registry.get(DETAILS).orElseThrow().getValue(),
                        createList(registry.get(REASONS).orElseThrow().getValue()),
                        createList(registry.get(SOLUTIONS).orElseThrow().getValue())
                )
        );
    }

    private static String createList(String[] elements) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++)
            builder.append(i + 1).append(".").append(elements[i]).append("\n");

        return builder.toString();
    }
}
