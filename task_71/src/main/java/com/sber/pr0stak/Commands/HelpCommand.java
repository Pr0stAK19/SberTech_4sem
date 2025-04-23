package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

@CommandAnnotation(name = "help")
public class HelpCommand implements Command {
    private final Command[] commands;

    public HelpCommand(Command[] commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String[] args) {
        for (Command cmd : commands) {
            System.out.println(cmd.getName() + ": " + cmd.getHelp());
        }
    }
    @Override
    public String getHelp() {
        return "выводит список доступных команд";
    }
}