package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

@CommandAnnotation(name = "exit")
public class ExitCommand implements Command {

    @Override
    public String getName(){
        return "exit";
    }

    @Override
    public String getHelp(){
        return "Завершает работу приложения";
    }

    @Override
    public void execute(String[] args ) {
        System.exit(0);
    }
}