package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

import java.nio.file.Files;
import java.nio.file.Paths;

@CommandAnnotation(name = "pwd", description = "вывод текущей рабочей директории")
public class PwdCommand implements Command {
    public String getName() {
        return "ls";
    }

    public void execute(String[] args) {
        try {
            Files.list(Paths.get(".")).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении директории");
        }
    }

    public String getHelp() {
        return "вывод текущей рабочей директории";
    }
}
