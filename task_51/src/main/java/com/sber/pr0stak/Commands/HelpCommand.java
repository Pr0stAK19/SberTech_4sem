package com.sber.pr0stak.Commands;

import com.sber.pr0stak.Command;

public class HelpCommand implements Command {
    public void execute() {
        System.out.println("Доступные команды:");
        System.out.println(" date - выводит текущую дату");
        System.out.println(" time - выводит текущее время");
        System.out.println(" pwd - выводит текущий рабочий каталог");
        System.out.println(" exit - завершает работу приложения");
        System.out.println(" help - выводит список доступных команд");
    }
}