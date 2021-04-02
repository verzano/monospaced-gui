package dev.verzano.monospaced.gui.terminal;

import dev.verzano.monospaced.core.metric.Size;
import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MockTerminal implements Terminal {
    private static final MockTerminal INSTANCE = new MockTerminal();

    private final TixelGrid grid = TixelGrid.getInstance();
    private BlockingDeque<Integer> readBuffer = new LinkedBlockingDeque<>();

    private MockTerminal() {

    }

    public static MockTerminal getInstance() {
        return INSTANCE;
    }

    public Deque<Integer> getReadBuffer() {
        return readBuffer;
    }

    public TixelGrid getGrid() {
        return grid;
    }

    @Override
    public int getWidth() {
        return grid.getWidth();
    }

    @Override
    public int getHeight() {
        return grid.getHeight();
    }

    @Override
    public Size getSize() {
        return grid.getSize();
    }

    public void setSize(int width, int height) {
        grid.setSize(width, height);
    }

    @Override
    public int read() {
        try {
            return readBuffer.takeFirst();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String s) {
        for (var c : s.toCharArray()) {
            grid.write(c);
        }
    }

    @Override
    public void writef(String s, Object... fs) {
        write(s.formatted(fs));
    }

    @Override
    public void flush() {
        // No-Op
    }

    @Override
    public void close() {
        grid.reset();
        readBuffer = new LinkedBlockingDeque<>();
    }
}