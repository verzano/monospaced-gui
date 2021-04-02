package dev.verzano.monospaced.gui.terminal;

import static java.lang.Integer.parseInt;

import dev.verzano.monospaced.core.ansi.sgr.Attribute;
import dev.verzano.monospaced.core.ansi.sgr.Background;
import dev.verzano.monospaced.core.ansi.sgr.Foreground;
import dev.verzano.monospaced.core.ansi.sgr.SgrFormat;
import dev.verzano.monospaced.core.constant.Keys;
import dev.verzano.monospaced.core.metric.Point;
import dev.verzano.monospaced.core.metric.Size;

public class TixelGrid {
    private static final TixelGrid INSTANCE = new TixelGrid();

    private final Point cursorPosition;
    private final SgrFormat currentFormat;
    private volatile Tixel[][] tixels;

    private String escapeSequence = "";
    private boolean escapedMode = false;

    public static TixelGrid getInstance() {
        return INSTANCE;
    }

    private TixelGrid() {
        tixels = new Tixel[0][0];
        cursorPosition = new Point(0, 0);
        currentFormat = new SgrFormat(Background.NONE, Foreground.NONE, Attribute.NORMAL);
    }

    public Tixel[][] getTixels() {
        return tixels;
    }

    public Size getSize() {
        return new Size(getWidth(), getHeight());
    }

    // TODO don't obliterate the old values???
    public void setSize(int width, int height) {
        tixels = new Tixel[height][width];

        cursorPosition.setX(Math.min(width, cursorPosition.getX()));
        cursorPosition.setY(Math.min(height, cursorPosition.getY()));
    }

    public int getWidth() {
        return tixels.length == 0 ? 0 : tixels[0].length;
    }

    public int getHeight() {
        return tixels.length;
    }

    // TODO Should this wrap around both ways?
    // TODO When it wraps should it go the number of places over? like should it just do a modulo?
    private void setX(int x) {
        if (x >= getWidth() || x < 0) {
            cursorPosition.setX(0);
        } else {
            cursorPosition.setX(x);
        }
    }

    // TODO Should this wrap around both ways?
    // TODO When it wraps should it go the number of places over? like should it just do a modulo?
    private void setY(int y) {
        if (y >= getHeight() || y < 0) {
            cursorPosition.setY(0);
        } else {
            cursorPosition.setY(y);
        }
    }

    public void write(char c) {
        if (escapedMode) {
            processEscapedChar(c);
        } else if (c == Keys.ESC) {
            escapedMode = true;
        } else {
            tixels[cursorPosition.getY()][cursorPosition.getX()] = new Tixel(c, currentFormat.getFormatString());

            setX(cursorPosition.getX() + 1);
        }
    }

    private void processEscapedChar(char c) {
        int n, m;
        switch (c) {
            case '[': // since we only process CSI sequences we don't look especially for this
                break;
            case 'A': // CUU
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) : 1;
                setY(cursorPosition.getY() - n);
                resetEscapeMode();
                break;
            case 'B': // CUD
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) : 1;
                setY(cursorPosition.getY() + n);
                resetEscapeMode();
                break;
            case 'C': // CUF
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) : 1;
                setX(cursorPosition.getX() + n);
                resetEscapeMode();
                break;
            case 'D': // CUB
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) : 1;
                setX(cursorPosition.getX() - n);
                resetEscapeMode();
                break;
            case 'E': // CNL
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) : 1;
                setX(0);
                setY(cursorPosition.getY() + n);
                resetEscapeMode();
                break;
            case 'F': // CPL
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) : 1;
                setX(0);
                setY(cursorPosition.getY() - n);
                resetEscapeMode();
                break;
            case 'G': // CHA
                // terminals use (1, 1) as default position but we use (0, 0) so we remove 1
                n = escapeSequence.length() > 0 ? parseInt(escapeSequence) - 1 : 0;
                setY(n);
                resetEscapeMode();
                break;
            case 'H': // CUP
                var parts = escapeSequence.split(";");
                // terminals use (1, 1) as default position but we use (0, 0) so we remove 1
                n = parseInt(parts[0]) - 1;
                m = parseInt(parts[1]) - 1;
                setY(n);
                setX(m);
                resetEscapeMode();
                break;
            case 'm':
                // TODO-OOOOOOOOOOOOOOOOO
                resetEscapeMode();
                break;
            case 'h', 'l': // don't care about showing/hiding the cursor
                resetEscapeMode();
                break;
            default:
                escapeSequence += c;
                break;
        }
    }

    private void resetEscapeMode() {
        escapeSequence = "";
        escapedMode = false;
    }

    public void reset() {
        tixels = new Tixel[0][0];

        cursorPosition.setX(0);
        cursorPosition.setY(0);

        currentFormat.setBackground(Background.NONE);
        currentFormat.setForeground(Foreground.NONE);
        currentFormat.setAttributes(Attribute.NORMAL);

        resetEscapeMode();
    }
}
