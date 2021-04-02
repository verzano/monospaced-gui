package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.debug.LoggerService;
import dev.verzano.monospaced.gui.floater.Floater;
import dev.verzano.monospaced.gui.task.print.PrintTask;
import dev.verzano.monospaced.gui.terminal.JlineTerminal;
import dev.verzano.monospaced.gui.terminal.Terminal;
import dev.verzano.monospaced.gui.widget.Widget;
import java.io.IOException;
import java.io.OutputStream;

// TODO use an executor to schedule events
// TODO add defaults for attributes as well as some css style waterfall thing for getting them
// TODO order all of the fields in all of the classes correctly
// TODO only permit one floater at a time, or have a stack of floaters
// TODO 'focused' is if any of its children are focused
// TODO come up with a better way to determine the time between pulling for draw events
// TODO allow resize events to be grouped together
// TODO foreground/background can be inherited from parent
public class MonospacedGui {
    private static MonospacedGuiContext context;

    public static MonospacedGuiContext getContext() {
        return context;
    }

    public static MonospacedGuiContext startup(Terminal terminal) {
        context = new MonospacedGuiContext(terminal);
        context.startup();
        return context;
    }

    public static MonospacedGuiContext startup() throws IOException {
        return startup(new JlineTerminal());
    }

    public static boolean startupComplete() {
        return context.startupComplete();
    }

    public static void enableLogging() {
        LoggerService.enable();
    }

    public static void enableLogging(OutputStream os) {
        LoggerService.enable(os);
    }

    public static void shutdown() {
        new Thread(() -> context.shutdown()).start();
    }

    public static boolean shutdownComplete() {
        return context.shutdownComplete();
    }

    public static void removeFloater() {
        context.removeFloater();
    }

    public static void setFloater(Floater floater) {
        context.setFloater(floater);
    }

    public static Widget getFocusedWidget() {
        return context.getFocusedWidget();
    }

    public static void setFocusedWidget(Widget widget) {
        context.setFocusedWidget(widget);
    }

    public static void setBaseWidget(Widget baseWidget) {
       context.setBaseWidget(baseWidget);
    }

    public static void move(int x, int y) {
        context.move(x, y);
    }

    public static void schedulePrintTask(PrintTask printTask) {
        context.schedulePrintTask(printTask);
    }

    public static void print(String s) {
        context.print(s);
    }

    public static void printn(String s, int n) {
        context.printn(s, n);
    }

    public static int getWidth() {
        return context.getWidth();
    }

    public static int getHeight() {
        return context.getHeight();
    }

    public static void reprint() {
        context.reprint();
    }
}
