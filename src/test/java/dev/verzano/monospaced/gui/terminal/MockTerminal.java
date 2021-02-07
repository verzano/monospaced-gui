package dev.verzano.monospaced.gui.terminal;

import dev.verzano.monospaced.core.metric.Size;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MockTerminal implements Terminal {
    private static final MockTerminal INSTANCE = new MockTerminal();

    private final Size size = new Size(0, 0);
    private final BlockingDeque<Integer> readBuffer = new LinkedBlockingDeque<>();
    private final List<Character> writeBuffer = new LinkedList<>();

    public static MockTerminal getInstance() {
        return INSTANCE;
    }

    public void reset() {
        setWidth(0);
        setHeight(0);
        readBuffer.clear();
        writeBuffer.clear();
    }

    public Deque<Integer> getReadBuffer() {
        return readBuffer;
    }

    public List<Character> getWriteBuffer() {
        return writeBuffer;
    }

    @Override
    public int getWidth() {
        return size.getWidth();
    }

    public void setWidth(int width) {
        size.setWidth(width);
    }

    @Override
    public int getHeight() {
        return size.getHeight();
    }

    public void setHeight(int height) {
        size.setHeight(height);
    }

    @Override
    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        setWidth(size.getWidth());
        setHeight(size.getHeight());
    }

    @Override
    public int read() throws IOException {
        try {
            return readBuffer.takeFirst();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String s) {
        for (var c : s.toCharArray()) {
            writeBuffer.add(c);
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
    public void close() throws IOException {
        // No-Op
    }
}