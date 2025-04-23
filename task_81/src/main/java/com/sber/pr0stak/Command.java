package com.sber.pr0stak;

public interface Command {
    String getName();

    void execute(String[] args);
    String getHelp();
}
