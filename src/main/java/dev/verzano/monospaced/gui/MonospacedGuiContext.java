package dev.verzano.monospaced.gui;

import static dev.verzano.monospaced.core.ansi.ControlSequences.CSI;
import static dev.verzano.monospaced.core.ansi.ControlSequences.CUP;
import static dev.verzano.monospaced.core.ansi.ControlSequences.HCU;
import static dev.verzano.monospaced.core.ansi.ControlSequences.SCU;

import dev.verzano.monospaced.core.constant.Keys;
import dev.verzano.monospaced.core.metric.Size;
import dev.verzano.monospaced.gui.container.floor.Floor;
import dev.verzano.monospaced.gui.container.floor.FloorOptions;
import dev.verzano.monospaced.gui.debug.Logger;
import dev.verzano.monospaced.gui.debug.LoggerService;
import dev.verzano.monospaced.gui.floater.Floater;
import dev.verzano.monospaced.gui.task.print.PrintTask;
import dev.verzano.monospaced.gui.terminal.Terminal;
import dev.verzano.monospaced.gui.widget.Widget;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonospacedGuiContext {
    private static final Logger log = LoggerService.getLogger(MonospacedGuiContext.class);

    private final AtomicBoolean run = new AtomicBoolean(true);
    private final BlockingDeque<PrintTask> printTaskQueue = new LinkedBlockingDeque<>();
    private final Floor floor = new Floor();
    private final Thread printingThread = new Thread(this::printingLoop, "Printing");
    private final Thread keyActionThread = new Thread(this::keyActionLoop, "Key Action");
    private final Thread resizingThread = new Thread(this::resizingLoop, "Resizing");
    private final Terminal terminal;
    private final Size oldSize;

    private volatile boolean printingLoopRunning = false;
    private volatile boolean keyActionLoopRunning = false;
    private volatile boolean resizingLoopRunning = false;

    private Floater floater = Floater.NULL_FLOATER;
    private Widget focusedWidget = Widget.NULL_WIDGET;

    protected MonospacedGuiContext(Terminal terminal) {
        this.terminal = terminal;
        oldSize = new Size(terminal.getWidth(), terminal.getHeight());
    }

    protected Floor getFloor() {
        return floor;
    }

    public Floater getFloater() {
        return floater;
    }

    public Size getSize() {
        return terminal.getSize();
    }

    public int getHeight() {
        return terminal.getSize().getHeight();
    }

    public int getWidth() {
        return terminal.getSize().getWidth();
    }

    public void setFloater(Floater floater) {
        this.floater = floater;
        floater.setFocused();
    }

    public void removeFloater() {
        floater = Floater.NULL_FLOATER;
    }

    public Widget getFocusedWidget() {
        return focusedWidget;
    }

    public void setFocusedWidget(Widget focusedWidget) {
        this.focusedWidget = focusedWidget;
    }

    public void setBaseWidget(Widget baseWidget) {
        floor.addWidget(baseWidget, new FloorOptions());
    }

    public void startup() {
        log.log("Startup running");
        printingThread.start();
        keyActionThread.start();
        resizingThread.start();

        printTaskQueue.addFirst(() -> print(HCU.apply()));
        log.log("Startup complete");
    }

    public boolean startupComplete() {
        return printingLoopRunning
                && keyActionLoopRunning
                && resizingLoopRunning;
    }

    public boolean shutdownComplete() {
        return !printingLoopRunning
                && !keyActionLoopRunning
                && !resizingLoopRunning;
    }

    private void keyActionLoop() {
        try {
            keyActionLoopRunning = true;
            log.log("Key Action Loop running");

            while (run.get()) {
                var key = terminal.read();
                switch (key) {
                    case Keys.ESC -> {
                        if (terminal.read() == '[') {
                            var sequence = CSI + terminal.read();
                            log.log("Registered control sequence: " + sequence);
                            focusedWidget.fireKeyActions(sequence);
                        }
                    }
                    case -2 -> {
                    }
                    default -> {
                        // TODO convert this value better (for printable chars)
                        log.log("Registered key press: " + (char)key);
                        focusedWidget.fireKeyActions((char) key + "");
                    }
                }
            }
        } catch (IOException e) {
            keyActionLoopRunning = false;
            throw new RuntimeException(e);
        }
    }

    public void move(int x, int y) {
        if (Thread.currentThread() != printingThread) {
            printTaskQueue.add(() -> move(x, y));
        } else {
            terminal.writef(CUP.apply(y, x));
        }
    }

    public void print(String s) {
        if (Thread.currentThread() != printingThread) {
            printTaskQueue.add(() -> print(s));
        } else {
            terminal.write(s);
        }
    }

    public void printn(String s, int n) {
        if (Thread.currentThread() != printingThread) {
            printTaskQueue.add(() -> printn(s, n));
        } else {
            for (int i = 0; i < n; i++) {
                terminal.write(s);
            }
        }
    }

    public void reprint() {
        if (Thread.currentThread() != printingThread) {
            printTaskQueue.add(this::reprint);
        } else {
            floor.print();
            if (floater != Floater.NULL_FLOATER) {
                floater.print();
            }
        }
    }

    public void schedulePrintTask(PrintTask printTask) {
        printTaskQueue.add(printTask);
    }

    private void printingLoop() {
        clear();

        printingLoopRunning = true;
        log.log("Printing Loop running");

        while (run.get()) {
            try {
                printTaskQueue.take().print();
                terminal.flush();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        clear();
    }

    public void resize() {
        if (Thread.currentThread() != printingThread) {
            printTaskQueue.addFirst(this::resize);
        } else {
            floor.arrange();
            if (floater != Floater.NULL_FLOATER) {
                floater.arrange();
            }
        }
    }

    private void resizingLoop() {
        resizingLoopRunning = true;
        log.log("Resizing Loop started");

        while (run.get()) {
            if (!oldSize.equals(getSize())) {
                resize();
                oldSize.setWidth(terminal.getWidth());
                oldSize.setHeight(terminal.getHeight());

                reprint();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void clear() {
        String emptyLine = new String(new char[getWidth()]).replace("\0", " ");
        for (int row = 1; row <= getHeight(); row++) {
            move(1, row);
            terminal.write(emptyLine);
        }
        move(1, 1);
        terminal.flush();
    }

    public void shutdown() {
        log.log("Shutdown started");
        printTaskQueue.addFirst(() -> print(SCU.apply()));
        printTaskQueue.addFirst(() -> run.set(false));

        try {
            printingThread.join(200);
        } catch (InterruptedException ignored) {
        } finally {
            printingLoopRunning = false;
            log.log("Printing Loop stopped");
        }

        try {
            keyActionThread.join(200);
        } catch (InterruptedException ignored) {
        } finally {
            keyActionLoopRunning = false;
            log.log("Key Action Loop stopped");
        }

        try {
            resizingThread.join(200);
        } catch (InterruptedException ignored) {
        } finally {
            resizingLoopRunning = false;
            log.log("Resizing Loop stopped");
        }

        try {
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.log("Shutdown complete");
    }
}
