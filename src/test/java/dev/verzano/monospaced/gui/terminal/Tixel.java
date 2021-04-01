package dev.verzano.monospaced.gui.terminal;

import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

import java.util.Objects;

public class Tixel {
    private final char symbol;
    private final String format;

    public Tixel(char symbol, String format) {
        this.symbol = symbol;
        this.format = format;
    }

    public Tixel(char symbol) {
        this(symbol, normalSgrFormat());
    }

    public char getSymbol() {
        return symbol;
    }

    public String getFormat() {
        return format;
    }

    public String asString() {
        return format + symbol + normalSgrFormat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tixel tixel = (Tixel) o;
        return symbol == tixel.symbol && Objects.equals(format, tixel.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, format);
    }

    @Override
    public String toString() {
        return "Tixel{" +
                "symbol=" + symbol +
                ", format=" + format +
                '}';
    }
}
