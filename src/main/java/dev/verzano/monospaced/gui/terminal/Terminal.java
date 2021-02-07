package dev.verzano.monospaced.gui.terminal;

import dev.verzano.monospaced.core.metric.Size;
import java.io.IOException;

public interface Terminal {
    int getWidth();
    int getHeight();
    Size getSize();

    int read() throws IOException;
    void write(String s);
    void writef(String s, Object... fs);
    void flush();

    void close() throws IOException;
}
