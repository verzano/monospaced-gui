package dev.verzano.monospaced.gui.terminal;

import dev.verzano.monospaced.core.metric.Size;
import java.io.IOException;
import org.jline.terminal.TerminalBuilder;

public class JlineTerminal implements Terminal {
    private final org.jline.terminal.Terminal terminal;

    public JlineTerminal() throws IOException {
        terminal = TerminalBuilder.terminal();
        terminal.enterRawMode();
        terminal.echo(false);
    }

    @Override
    public void close() throws IOException {
        terminal.close();
    }

    @Override
    public int getWidth() {
        return terminal.getWidth();
    }

    @Override
    public int getHeight() {
        return terminal.getHeight();
    }

    @Override
    public Size getSize() {
        return new Size(getWidth(), getHeight());
    }

    @Override
    public int read() throws IOException {
        return terminal.reader().read(100);
    }

    @Override
    public void write(String s) {
        terminal.writer().print(s);
    }

    @Override
    public void writef(String s, Object... fs) {
        terminal.writer().printf(s, fs);
    }

    @Override
    public void flush() {
        terminal.flush();
    }
}
