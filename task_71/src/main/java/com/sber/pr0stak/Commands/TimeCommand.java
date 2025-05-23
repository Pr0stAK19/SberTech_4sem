package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

import java.time.LocalTime;

@CommandAnnotation(name = "time")
public class TimeCommand implements Command {

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void execute(String[] args) {
        System.out.println(LocalTime.now());
    }

    @Override
    public String getHelp() {
        return "выводит текущее время";
    }
}
