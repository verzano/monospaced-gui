package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.debug.Logger;
import dev.verzano.monospaced.gui.debug.LoggerService;
import dev.verzano.monospaced.gui.floater.Floater;
import dev.verzano.monospaced.gui.lifecycle.LifecycleListener;
import dev.verzano.monospaced.gui.lifecycle.LifecycleListenerAdapter;
import dev.verzano.monospaced.gui.task.print.PrintTask;
import dev.verzano.monospaced.gui.terminal.JlineTerminal;
import dev.verzano.monospaced.gui.terminal.Terminal;
import dev.verzano.monospaced.gui.widget.Widget;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

// TODO use an executor to schedule events
// TODO add defaults for attributes as well as some css style waterfall thing for getting them
// TODO order all of the fields in all of the classes correctly
// TODO only permit one floater at a time, or have a stack of floaters
// TODO 'focused' is if any of its children are focused
// TODO come up with a better way to determine the time between pulling for draw events
// TODO allow resize events to be grouped together
// TODO foreground/background can be inherited from parent
public class MonospacedGui {
    private static final Logger log = LoggerService.getLogger(MonospacedGui.class);
    private static final Set<LifecycleListener> lifecycleListeners = new HashSet<>();

    private static MonospacedGuiContext context;

    public static MonospacedGuiContext getContext() {
        return context;
    }

    public static void startup(Terminal terminal) {
        lifecycleListeners.forEach(LifecycleListener::onStartupStart);

        context = new MonospacedGuiContext(
                terminal,
                new LifecycleListenerAdapter() {
                    @Override
                    public void onStartupComplete() {
                        lifecycleListeners.forEach(LifecycleListener::onStartupComplete);
                    }

                    @Override
                    public void onShutdownComplete() {
                        lifecycleListeners.forEach(LifecycleListener::onShutdownComplete);
                    }
                });
        context.startup();
    }

    public static void startup() throws IOException {
        startup(new JlineTerminal());
    }

    public static void shutdown() {
        lifecycleListeners.forEach(LifecycleListener::onShutdownStart);

        new Thread(() -> context.shutdown()).start();
    }

    public static void enableLogging(OutputStream os) {
        LoggerService.enable(os);
        addLifecycleListener(new LifecycleListener() {
            @Override
            public void onStartupStart() {
                log.log("Startup running...");
            }

            @Override
            public void onStartupComplete() {
                log.log("Startup complete!");
            }

            @Override
            public void onShutdownStart() {
                log.log("Shutdown running...");
            }

            @Override
            public void onShutdownComplete() {
                log.log("Shutdown complete!");
            }
        });
    }

    public static void addLifecycleListener(LifecycleListener lifecycleListener) {
        lifecycleListeners.add(lifecycleListener);
    }

    public static void removeLifecycleListener(LifecycleListener lifecycleListener) {
        lifecycleListeners.remove(lifecycleListener);
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
