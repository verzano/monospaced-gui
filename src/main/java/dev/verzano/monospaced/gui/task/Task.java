package dev.verzano.monospaced.gui.task;

public interface Task {
    Task NULL_TASK = () -> {
    };

    void fire();
}
