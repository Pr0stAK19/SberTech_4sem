package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

import java.time.LocalDate;

@CommandAnnotation(name = "date", description = "Выводит текущую дату")
public class DateCommand implements Command {

    public String getName(){
        return "date";
    }

    public String getHelp(){
        return "Выводит текущую дату";
    }

    public void execute(String[] args ) {
        System.out.println(LocalDate.now());
    }
}
