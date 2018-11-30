package com.toasttab.pgwarm.util;

public class ConsoleProgressBar {
    private static int BAR_WIDTH = 30;

    private final int percent;
    private final boolean details;
    private Integer num;
    private Integer den;

    public ConsoleProgressBar(int percent) {
        this.percent = percent;
        this.details = false;
    }

    public ConsoleProgressBar(int num, int den) {
        this.percent = (int)(num/(float)den * 100);
        this.num = num;
        this.den = den;
        this.details = true;
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

        builder.append(String.format(" (%-3s%%)", percent));

        if (details) {
            builder.append(String.format(" (%s/%s)", num, den));
        }

        return builder.toString();
    }
}
