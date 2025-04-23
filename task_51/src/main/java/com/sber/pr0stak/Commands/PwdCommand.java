package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;

public class PwdCommand implements Command {
    public void execute() {
        System.out.println(System.getProperty("user.dir"));
    }
}
