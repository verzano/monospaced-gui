package dev.verzano.monospaced.gui.task;

@FunctionalInterface
public interface Task {
    Task NULL_TASK = () -> {
    };

    void fire();
}
