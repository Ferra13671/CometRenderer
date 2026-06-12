package com.ferra13671.cometrenderer.plugins.betterexceptions;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.Map;
import java.util.function.Consumer;

@API(status = API.Status.STABLE)
@UtilityClass
public class BetterExceptionsPlugin {
    private final String exceptionText = """
    
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
    public final Tag<Map<Class<? extends CometException>, PrintInfoCreator<?>>> EXCEPTIONS_PRINT_INFO = new Tag<>("exceptions-print-info");
    public final Tag<String> DESCRIPTION = new Tag<>("description");
    public final Tag<String> DETAILS = new Tag<>("details");
    public final Tag<String[]> REASONS = new Tag<>("reasons");
    public final Tag<String[]> SOLUTIONS = new Tag<>("solutions");

    public void init() {
        if (!CometRenderer.getRegistry().contains(CometTags.INITIALIZED))
            throw new IllegalStateException("CometRenderer is not initialized!");

        CometRenderer.getRegistry().set(EXCEPTIONS_PRINT_INFO, ExceptionsPrintInfo.getMap());
        Consumer<CometException> prevLogConsumer = CometRenderer.getExceptionManager().getOnLogException();
        CometRenderer.getExceptionManager().setOnLogException(exception -> manageException(exception, prevLogConsumer));
        CometRenderer.getExceptionManager().setOnThrowException(exception -> {
            manageException(exception, null);
            throw exception;
        });
    }

    private void manageException(CometException exception, Consumer<CometException> prevLogConsumer) {
        Map<Class<? extends CometException>, PrintInfoCreator<?>> map = CometRenderer.getRegistry().get(EXCEPTIONS_PRINT_INFO).orElseThrow();
        if (map.containsKey(exception.getClass())) {
            printException(((PrintInfoCreator<CometException>) map.get(exception.getClass())).create(exception));
        } else {
            if (prevLogConsumer == null)
                CometRenderer.getLogger().error(String.format("Unable to find information about exception '%s'.", exception.getClass()));
            else
                prevLogConsumer.accept(exception);
        }
    }

    private void printException(Registry registry) {
        CometRenderer.getLogger().error(
                String.format(
                        exceptionText,
                        registry.get(DESCRIPTION).orElseThrow(),
                        registry.get(DETAILS).orElseThrow(),
                        createList(registry.get(REASONS).orElseThrow()),
                        createList(registry.get(SOLUTIONS).orElseThrow())
                )
        );
    }

    private String createList(String[] elements) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++)
            builder.append(i + 1).append(".").append(elements[i]).append("\n");

        return builder.toString();
    }
}
