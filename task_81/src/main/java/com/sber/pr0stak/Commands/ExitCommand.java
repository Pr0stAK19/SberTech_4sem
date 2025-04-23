package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

@CommandAnnotation(name = "exit", description = "Завершает работу приложения")
public class ExitCommand implements Command {
    public String getName(){
        return "exit";
    }

    public String getHelp(){
        return "Завершает работу приложения";
    }

    public void execute(String[] args ) {
        System.exit(0);
    }
}