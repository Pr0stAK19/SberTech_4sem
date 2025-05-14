package com.sber.pr0stak;

import java.util.List;
import java.util.Scanner;
import java.util.Optional;

// Основной класс приложения с консольным интерфейсом
public class TodoListApp {

    private TaskRepository taskRepository; // Ссылка на репозиторий для работы с БД
    private Scanner scanner;

    public TodoListApp() {
        this.taskRepository = new TaskRepository(); // Создаем экземпляр репозитория
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        // Создаем экземпляр приложения и запускаем его
        TodoListApp app = new TodoListApp();
        app.run();
    }

    // Главный метод запуска приложения
    public void run() {
        System.out.println("Добро пожаловать в приложение ToDo List!");
        printHelp(); // Показать доступные команды

        // Основной цикл обработки команд пользователя
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "add":
                    handleAddTask();
                    break;
                case "show":
                    handleShowTasks();
                    break;
                case "done":
                    handleMarkTaskAsDone();
                    break;
                case "delete":
                    handleDeleteTask();
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    System.out.println("До свидания!");
                    scanner.close(); // Закрыть сканер перед выходом
                    return; // Выйти из приложения
                default:
                    System.out.println("Неизвестная команда. Введите 'help' для списка команд.");
            }
        }
    }

    // Обработчик команды "add"
    private void handleAddTask() {
        System.out.print("Введите описание задачи: ");
        String taskName = scanner.nextLine().trim();

        if (taskName.isEmpty()) {
            System.out.println("Описание задачи не может быть пустым!");
            return;
        }

        if (taskRepository.addTask(taskName)) {
            System.out.println("Задача успешно добавлена!");
        } else {
            System.out.println("Не удалось добавить задачу.");
        }
    }

    // Обработчик команды "show"
    private void handleShowTasks() {
        List<Task> tasks = taskRepository.getAllTasks();

        System.out.println("\nСписок задач:");
        System.out.println("----------");

        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (Task task : tasks) {
                System.out.println(task); // Task.toString() будет вызван автоматически
            }
        }
        System.out.println("----------\n");
    }

    // Обработчик команды "done"
    private void handleMarkTaskAsDone() {
        System.out.print("Введите ID задачи для отметки о выполнении: ");
        String input = scanner.nextLine().trim();

        try {
            int taskId = Integer.parseInt(input);

            // Опционально: проверить, существует ли задача с таким ID перед обновлением
            Optional<Task> taskOptional = taskRepository.getTaskById(taskId);
            if (!taskOptional.isPresent()) {
                System.out.println("Не найдена задача с ID: " + taskId);
                return;
            }
            // Можно добавить проверку, что задача еще не выполнена, если нужно

            if (taskRepository.updateTaskStatus(taskId, "DONE")) {
                System.out.println("Задача отмечена как выполненная!");
            } else {
                // Это сообщение, вероятно, не будет достигнуто, если taskOptional.isPresent() был true,
                // но оставим его для полной обработки результата репозитория.
                System.out.println("Не удалось обновить статус задачи с ID: " + taskId);
            }

        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите корректный ID задачи (число).");
        }
    }

    // Обработчик команды "delete"
    private void handleDeleteTask() {
        System.out.print("Введите ID задачи для удаления: ");
        String input = scanner.nextLine().trim();

        try {
            int taskId = Integer.parseInt(input);

            // Опционально: проверить, существует ли задача с таким ID перед удалением
            Optional<Task> taskOptional = taskRepository.getTaskById(taskId);
            if (!taskOptional.isPresent()) {
                System.out.println("Не найдена задача с ID: " + taskId);
                return;
            }

            if (taskRepository.deleteTask(taskId)) {
                System.out.println("Задача успешно удалена!");
            } else {
                // Аналогично handleMarkTaskAsDone, это маловероятно, если задача найдена
                System.out.println("Не удалось удалить задачу с ID: " + taskId);
            }

        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите корректный ID задачи (число).");
        }
    }

    // Метод для вывода справки по командам
    private void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("add    - Добавить новую задачу");
        System.out.println("show   - Показать все задачи");
        System.out.println("done   - Отметить задачу как выполненную (нужен ID)");
        System.out.println("delete - Удалить задачу (нужен ID)");
        System.out.println("help   - Показать эту справку");
        System.out.println("exit   - Выйти из приложения");
        System.out.println();
    }
}
