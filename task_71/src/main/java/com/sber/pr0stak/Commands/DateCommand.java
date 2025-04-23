package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

import java.time.LocalDate;

@CommandAnnotation(name = "date")
public class DateCommand implements Command {

    @Override
    public String getName(){
        return "date";
    }

    @Override
    public String getHelp(){
        return "Выводит текущую дату";
    }

    @Override
    public void execute(String[] args ) {
        System.out.println(LocalDate.now());
    }
}
