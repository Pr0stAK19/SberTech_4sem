package com.sber.pr0stak;

import com.sber.pr0stak.Commands.*;

import java.util.Scanner;

public class ShellApp {
    public static void main(String[] args) {
        CommandInvoker invoker = new CommandInvoker();
        invoker.registerCommand("date", new DateCommand());
        invoker.registerCommand("time", new TimeCommand());
        invoker.registerCommand("pwd", new PwdCommand());
        invoker.registerCommand("help", new HelpCommand());
        invoker.registerCommand("exit", new ExitCommand());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            invoker.executeCommand(input);
        }
    }
}
