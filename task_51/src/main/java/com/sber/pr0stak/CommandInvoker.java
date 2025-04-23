package com.sber.pr0stak;

import java.util.HashMap;
import java.util.Map;

class CommandInvoker {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void executeCommand(String name) {
        Command command = commands.get(name);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Ошибка: неизвестная команда '" + name + "'");
        }
    }
}
