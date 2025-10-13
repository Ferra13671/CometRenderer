package com.ferra13671.cometrenderer.exceptions;

import com.ferra13671.cometrenderer.CometRenderer;

public class ExceptionPrinter {
    private static final String separateLine = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

    /*
     * Отрисовывает в консоли информацию о исключении и закрывает процесс игры
     */
    public static void printAndExit(CometException exception) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        stringBuilder.append(separateLine.concat("\n"));
        stringBuilder.append("Comet Renderer error occurred!\n");
        stringBuilder.append(separateLine.concat("\n"));
        stringBuilder.append("Whats wrong?\n\n");
        stringBuilder.append(exception.getDescription().concat("\n"));
        stringBuilder.append(separateLine.concat("\n"));
        stringBuilder.append("Details:\n\n");
        stringBuilder.append(exception.getDetails().concat("\n"));
        stringBuilder.append(separateLine.concat("\n"));
        stringBuilder.append("Possible reasons:\n\n");
        for (int i = 0; i < exception.getReasons().length; i++)
            stringBuilder.append(i + 1).append(".").append(exception.getReasons()[i]).append("\n");
        stringBuilder.append(separateLine.concat("\n"));
        stringBuilder.append("Possible solutions:\n\n");
        for (int i = 0; i < exception.getSolutions().length; i++)
            stringBuilder.append(i + 1).append(".").append(exception.getSolutions()[i]).append("\n");
        stringBuilder.append(separateLine);

        CometRenderer.getLogger().error(stringBuilder.toString());
        exception.printStackTrace();

        System.exit(-1);
    }
}
