package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;

public class ExitCommand implements Command {
    public void execute() {
        System.exit(0);
    }
}