package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;
import com.sber.pr0stak.CommandAnnotation;

import java.nio.file.Files;
import java.nio.file.Paths;

@CommandAnnotation(name = "pwd")
public class PwdCommand implements Command {

    @Override
    public String getName() {
        return "pwd";
    }

    @Override
    public void execute(String[] args) {
        try {
            Files.list(Paths.get(".")).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении директории");
        }
    }

    @Override
    public String getHelp() {
        return "выводит содержимое текущей директории";
    }
}
