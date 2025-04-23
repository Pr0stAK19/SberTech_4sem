package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

@CommandAnnotation(name = "help", description = "выводит список доступных команд")
public class HelpCommand implements Command {
    private final Command[] commands;

    public HelpCommand(Command[] commands) {
        this.commands = commands;
    }

    public String getName() {
        return "help";
    }

    public void execute(String[] args) {
        for (Command cmd : commands) {
            System.out.println(cmd.getName() + ": " + cmd.getHelp());
        }
    }
    public String getHelp() {
        return "выводит список доступных команд";
    }
}