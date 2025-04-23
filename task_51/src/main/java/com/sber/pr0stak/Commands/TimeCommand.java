package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCommand implements Command {
    public void execute() {
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}
