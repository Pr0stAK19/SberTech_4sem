package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

import java.time.LocalTime;

@CommandAnnotation(name = "time")
public class TimeCommand implements Command {
    public String getName() {
        return "time";
    }

    public void execute(String[] args) {
        System.out.println(LocalTime.now());
    }

    public String getHelp() {
        return "выводит текущее время";
    }
}
