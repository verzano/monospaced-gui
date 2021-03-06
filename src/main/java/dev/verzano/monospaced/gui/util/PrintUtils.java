package dev.verzano.monospaced.gui.util;

import dev.verzano.monospaced.core.constant.Position;

import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

public class PrintUtils {
    private PrintUtils() {
    }

    public static String getRowForText(String text, Position textPosition, String formatString, int width) {
        if (text.length() != width) {
            switch (textPosition) {
                case TOP_LEFT:
                case LEFT:
                case BOTTOM_LEFT:
                    if (text.length() > width) {
                        text = text.substring(0, width);
                    } else {
                        text += new String(new char[width - text.length()]).replace('\0', ' ');
                    }
                    break;
                case TOP:
                case CENTER:
                case BOTTOM:
                    if (text.length() > width) {
                        var halfExtra = (text.length() - width) / 2D;

                        text = text.substring((int) halfExtra, text.length() - (int) Math.ceil(halfExtra));
                    } else {
                        var halfRemaining = (width - text.length()) / 2D;
                        text = new String(new char[(int) Math.ceil(halfRemaining)]).replace('\0', ' ') + text
                                + new String(new char[(int) halfRemaining]).replace('\0', ' ');
                    }
                    break;
                case TOP_RIGHT:
                case RIGHT:
                case BOTTOM_RIGHT:
                    if (text.length() > width) {
                        text = text.substring(text.length() - width);
                    } else {
                        text = new String(new char[width - text.length()]).replace('\0', ' ') + text;
                    }
                    break;
                default:
                    break;
            }
        }

        return formatString + text + normalSgrFormat();
    }
}
