package com.sber.pr0stak;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Shell {
    private final List<Command> commands = new ArrayList<>();

    public Shell() {
        loadCommands();
    }

    private void loadCommands() {
        Reflections reflections = new Reflections("com.sber.pr0stak");
        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> commandClass : commandClasses) {
            if (commandClass.isAnnotationPresent(CommandAnnotation.class)) {
                try {
                    Command command = commandClass.getDeclaredConstructor().newInstance();
                    commands.add(command);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String commandName = parts[0];
            String[] args = (parts.length > 1) ? new String[parts.length - 1] : new String[0];
            System.arraycopy(parts, 1, args, 0, args.length);

            Command command = commands.stream()
                    .filter(cmd -> cmd.getName().equals(commandName))
                    .findFirst()
                    .orElse(null);

            if (command != null) {
                command.execute(args);
            } else {
                System.out.println("Неизвестная команда: " + commandName);
            }
        }
    }

    public static void main(String[] args) {
        new Shell().start();
    }
}

