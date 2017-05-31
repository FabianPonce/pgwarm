package com.toasttab.pgwarm.util;

public class ConsoleProgressBar {
    private static int BAR_WIDTH = 80;

    private final int percent;
    public ConsoleProgressBar(int percent) {
        this.percent = percent;
    }

    private int getCompletedCharacters() {
        return (int)(BAR_WIDTH*percent/100.0);
    }

    private int getEmptyCharacters() {
        return BAR_WIDTH - getCompletedCharacters();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i = 0; i < getCompletedCharacters(); ++i) {
            builder.append("=");
        }

        for(int i = 0; i < getEmptyCharacters(); ++i) {
            builder.append(" ");
        }

        builder.append("]");

        builder.append(String.format(" (%s%%)", percent));

        return builder.toString();
    }
}
