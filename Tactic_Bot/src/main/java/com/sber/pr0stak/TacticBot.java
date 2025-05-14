package com.sber.pr0stak;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TacticBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final Db db;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Унифицированные состояния пользователя
    private enum CommandType {
        ADD,
        REPORT,
        REPORT_RANGE,
        EDIT,
        DELETE
    }

    private enum Step {
        AWAITING_DATE,
        AWAITING_REASON,
        AWAITING_AMOUNT,
        AWAITING_ID,
        AWAITING_START_DATE,
        AWAITING_END_DATE,
        AWAITING_EDIT_FULL_COMMAND
    }

    // Класс для хранения состояния и данных пользователя
    private static class UserState {
        CommandType command;
        Step step;
        Date date;
        String reason;
        Double amount;
        Integer id; // Для edit и delete
        Date startDate;
        Date endDate;

        UserState(CommandType command, Step step) {
            this.command = command;
            this.step = step;
        }
    }

    private final Map<String, UserState> userStates = new ConcurrentHashMap<>();


    public TacticBot(String botToken) throws SQLException {
        telegramClient = new OkHttpTelegramClient(botToken);
        db = new Db();
        setBotCommands(); // Устанавливаем список команд при старте
    }

    private void setBotCommands() {
        List<BotCommand> commands = List.of(
                new BotCommand("/start", "Начать использование бота"),
                new BotCommand("/add", "Добавить запись расхода"),
                new BotCommand("/report", "Показать отчёт за день"),
                new BotCommand("/report_range", "Показать отчёт за период"),
                new BotCommand("/edit", "Отредактировать запись"),
                new BotCommand("/delete", "Удалить запись"),
                new BotCommand("/cancel", "Отменить текущую операцию")
        );
        try {
            telegramClient.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            processCallback(update.getCallbackQuery());
            return;
        }

        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String messageText = update.getMessage().getText().trim();
        String chatId = String.valueOf(update.getMessage().getChatId());

        // Обработка команды отмены
        if (messageText.equalsIgnoreCase("/cancel")) {
            cancelOperation(chatId);
            return;
        }

        // Проверяем, находится ли пользователь в интерактивном режиме ввода
        if (userStates.containsKey(chatId)) {
            processInteractiveInput(chatId, messageText);
            return;
        }

        // Если нет активного интерактивного режима, обрабатываем команды
        if (messageText.startsWith("/add")) {
            processAddCommand(chatId, messageText);
        } else if (messageText.startsWith("/report_range")) {
            processReportRangeCommand(chatId, messageText);
        } else if (messageText.startsWith("/report")) {
            processReportCommand(chatId, messageText);
        } else if (messageText.startsWith("/edit")) {
            processEditCommand(chatId, messageText);
        } else if (messageText.equals("/start")) {
            sendWelcome(chatId);
        } else if (messageText.startsWith("/delete")) {
            processDeleteCommand(chatId, messageText);
        }
        else {
            sendMessage(chatId, "Неизвестная команда. Используйте /start для списка команд.");
        }
    }

    private void cancelOperation(String chatId) {
        if (userStates.containsKey(chatId)) {
            userStates.remove(chatId);
            sendMessage(chatId, "Операция отменена.");
        } else {
            sendMessage(chatId, "Нет активных операций для отмены.");
        }
    }


    private void sendWelcome(String chatId) {
        String welcomeText = "Привет! Я бот для учёта расходов.\n" +
                "Используйте команды из меню или введите вручную.\n" +
                "Команды можно использовать сразу с параметрами или запустить без параметров для пошагового ввода:\n" +
                "/add [дата причина сумма] — добавить запись расхода\n" +
                "/report [дата] — показать отчёт за день\n" +
                "/report_range [дата_начало дата_конец] — показать отчёт за период\n" +
                "/edit [ID дата причина сумма] — отредактировать запись по ID\n" +
                "/delete [ID] — удалить запись по её ID\n" +
                "/cancel — отменить текущую операцию ввода";
        sendMessage(chatId, welcomeText);
    }

    // --- Методы обработки начального вызова команд ---

    private void processAddCommand(String chatId, String messageText) {
        String[] parts = messageText.split(" ", 4);
        if (parts.length < 4) {
            // Если параметров нет, переходим в интерактивный режим
            userStates.put(chatId, new UserState(CommandType.ADD, Step.AWAITING_DATE));
            sendMessage(chatId, "Введите дату расхода в формате YYYY-MM-dd (или /cancel для отмены):");
        } else {
            // Если параметры есть, обрабатываем сразу (аналогично старому add_quick)
            try {
                Date date = new Date(sdf.parse(parts[1]).getTime());
                String reason = parts[2];
                double amount = Double.parseDouble(parts[3]);

                db.addRecord(chatId, date, reason, amount);
                sendMessage(chatId, "Запись добавлена!");
            } catch (ParseException e) {
                sendMessage(chatId, "Неверный формат даты. Используйте YYYY-MM-dd.");
            } catch (NumberFormatException e) {
                sendMessage(chatId, "Сумма должна быть числом.");
            } catch (SQLException e) {
                sendMessage(chatId, "Ошибка базы данных: " + e.getMessage());
            }
        }
    }

    // --- Вспомогательные методы для формирования отчетов ---
    public void sendDailyReport(String chatId, Date date) throws SQLException {
        List<Map<String, Object>> records = db.getRecordsForDate(chatId, date);

        if (records.isEmpty()) {
            sendMessage(chatId, "За указанный день записей нет.");
            return;
        }

        StringBuilder reportText = new StringBuilder("Отчёт за " + sdf.format(date) + ":\n\n");
        double totalAmount = 0;
        for (Map<String, Object> r : records) {
            Object idObj = r.get("id");
            Object reasonObj = r.get("reason");
            Object amountObj = r.get("amount");

            if (idObj == null || reasonObj == null || amountObj == null) {
                reportText.append("Некорректная запись в базе данных.\n");
                continue;
            }

            int id;
            try {
                id = ((Number) idObj).intValue();
            } catch (ClassCastException e) {
                reportText.append("Ошибка чтения id записи.\n");
                continue;
            }

            String reason;
            try {
                reason = reasonObj.toString();
            } catch (Exception e) {
                reportText.append("Ошибка чтения причины записи.\n");
                continue;
            }

            double amount;
            try {
                amount = ((Number) amountObj).doubleValue();
            } catch (ClassCastException e) {
                reportText.append("Ошибка чтения суммы записи.\n");
                continue;
            }

            reportText.append(String.format("ID: %d | %s: %.2f\n", id, reason, amount));
            totalAmount += amount;
        }

        reportText.append(String.format("\nИтого: %.2f", totalAmount));

        sendMessage(chatId, reportText.toString());
    }

    public void sendRangeReport(String chatId, Date startDate, Date endDate) throws SQLException {
        List<Map<String, Object>> records = db.getRecordsForDateRange(chatId, startDate, endDate);

        if (records.isEmpty()) {
            sendMessage(chatId, "За указанный период записей нет.");
            return;
        }

        StringBuilder reportText = new StringBuilder("Отчёт за период с " + sdf.format(startDate) + " по " + sdf.format(endDate) + ":\n\n");
        double totalAmount = 0;
        for (Map<String, Object> r : records) {
            Object idObj = r.get("id");
            Object dateObj = r.get("record_date");
            Object reasonObj = r.get("reason");
            Object amountObj = r.get("amount");

            if (idObj == null || dateObj == null || reasonObj == null || amountObj == null) {
                reportText.append("Некорректная запись в базе данных.\n");
                continue;
            }

            int id;
            try {
                id = ((Number) idObj).intValue();
            } catch (ClassCastException e) {
                reportText.append("Ошибка чтения id записи.\n");
                continue;
            }

            Date recordDate;
            try {
                recordDate  = (Date) dateObj;
            } catch (ClassCastException e) {
                reportText.append("Ошибка чтения даты записи.\n");
                continue;
            }


            String reason;
            try {
                reason = reasonObj.toString();
            } catch (Exception e) {
                reportText.append("Ошибка чтения причины записи.\n");
                continue;
            }

            double amount;
            try {
                amount = ((Number) amountObj).doubleValue();
            } catch (ClassCastException e) {
                reportText.append("Ошибка чтения суммы записи.\n");
                continue;
            }


            reportText.append(String.format("ID: %d | %s | %s: %.2f\n", id, sdf.format(recordDate), reason, amount));
            totalAmount += amount;
        }
        reportText.append(String.format("\nИтого за период: %.2f", totalAmount));


        sendMessage(chatId, reportText.toString());
    }


    private void processReportCommand(String chatId, String messageText) {
        String[] parts = messageText.split(" ", 2);
        if (parts.length < 2) {
            // Если параметров нет, переходим в интерактивный режим
            userStates.put(chatId, new UserState(CommandType.REPORT, Step.AWAITING_DATE));
            sendMessage(chatId, "Введите дату отчёта в формате YYYY-MM-dd (или /cancel для отмены):");
        } else {
            // Если дата указана, формируем отчёт сразу
            try {
                Date date = new Date(sdf.parse(parts[1]).getTime());
                sendDailyReport(chatId, date); // Выносим логику формирования отчёта в отдельный метод
            } catch (ParseException | SQLException e) {
                sendMessage(chatId, "Неверный формат даты. Используйте YYYY-MM-dd.");
            }
        }
    }

    private void processReportRangeCommand(String chatId, String messageText) {
        String[] parts = messageText.split(" ", 3); // /report_range start_date end_date
        if (parts.length < 3) {
            // Если параметров нет, запускаем интерактивный запрос дат
            userStates.put(chatId, new UserState(CommandType.REPORT_RANGE, Step.AWAITING_START_DATE));
            sendMessage(chatId, "Введите начальную дату для отчёта в формате YYYY-MM-dd (или /cancel для отмены):");
        } else {
            // Если даты указаны, формируем отчёт сразу
            try {
                Date startDate = new Date(sdf.parse(parts[1]).getTime());
                Date endDate = new Date(sdf.parse(parts[2]).getTime());

                if (endDate.before(startDate)) {
                    sendMessage(chatId, "Конечная дата не может быть раньше начальной.");
                    return;
                }
                sendRangeReport(chatId, startDate, endDate); // Выносим логику формирования отчёта в отдельный метод

            } catch (ParseException e) {
                sendMessage(chatId, "Неверный формат даты. Используйте YYYY-MM-dd.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processEditCommand(String chatId, String messageText) {
        String[] parts = messageText.split(" ", 5); // /edit ID дата причина сумма
        if (parts.length < 5) {
            // Если параметров нет, переходим в интерактивный режим запроса ID
            userStates.put(chatId, new UserState(CommandType.EDIT, Step.AWAITING_ID));
            sendMessage(chatId, "Введите ID записи для редактирования (или /cancel для отмены):");
        } else {
            // Если параметры есть, редактируем сразу
            try {
                int id = Integer.parseInt(parts[1]);
                Date date = new Date(sdf.parse(parts[2]).getTime());
                String reason = parts[3];
                double amount = Double.parseDouble(parts[4]);

                boolean updated = db.updateRecord(chatId, id, date, reason, amount);
                if (updated) {
                    sendMessage(chatId, "Запись ID " + id + " успешно обновлена!");
                } else {
                    sendMessage(chatId, "Запись с указанным ID не найдена или не принадлежит вам.");
                }
            } catch (NumberFormatException e) {
                sendMessage(chatId, "ID и сумма должны быть числами.");
            } catch (ParseException e) {
                sendMessage(chatId, "Неверный формат даты, используйте YYYY-MM-dd");
            } catch (SQLException e) {
                sendMessage(chatId, "Ошибка базы данных: " + e.getMessage());
            }
        }
    }

    private void processDeleteCommand(String chatId, String messageText) {
        String[] parts = messageText.split(" ", 2);
        if (parts.length < 2) {
            // Если параметров нет, переходим в интерактивный режим запроса ID
            userStates.put(chatId, new UserState(CommandType.DELETE, Step.AWAITING_ID));
            sendMessage(chatId, "Введите ID записи для удаления (или /cancel для отмены):");
        } else {
            // Если ID указан, удаляем сразу
            try {
                int id = Integer.parseInt(parts[1]);
                boolean deleted = db.deleteRecord(chatId, id);
                if (deleted) {
                    sendMessage(chatId, "Запись успешно удалена.");
                } else {
                    sendMessage(chatId, "Запись с указанным ID не найдена или не принадлежит вам.");
                }
            } catch (NumberFormatException e) {
                sendMessage(chatId, "ID должно быть числом, например: /delete 3.");
            } catch (SQLException e) {
                sendMessage(chatId, "Ошибка базы данных: " + e.getMessage());
            }
        }
    }


    // --- Метод обработки ввода в интерактивном режиме ---

    private void processInteractiveInput(String chatId, String messageText) {
        UserState state = userStates.get(chatId);

        switch (state.command) {
            case ADD:
                processInteractiveAdd(chatId, state, messageText);
                break;
            case REPORT:
                processInteractiveReport(chatId, state, messageText);
                break;
            case REPORT_RANGE:
                processInteractiveReportRange(chatId, state, messageText);
                break;
            case EDIT:
                processInteractiveEdit(chatId, state, messageText);
                break;
            case DELETE:
                processInteractiveDelete(chatId, state, messageText);
                break;
        }
    }

    // --- Методы обработки конкретных команд в интерактивном режиме ---

    private void processInteractiveAdd(String chatId, UserState state, String messageText) {
        switch (state.step) {
            case AWAITING_DATE:
                try {
                    Date date = new Date(sdf.parse(messageText).getTime());
                    state.date = date;
                    state.step = Step.AWAITING_REASON;
                    sendMessage(chatId, "Введите причину расхода (или /cancel для отмены):");
                } catch (ParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Используйте YYYY-MM-dd. Попробуйте снова ввести дату (или /cancel для отмены):");
                }
                break;

            case AWAITING_REASON:
                if (messageText.trim().isEmpty()) {
                    sendMessage(chatId, "Причина расхода не может быть пустой. Введите причину (или /cancel для отмены):");
                } else {
                    state.reason = messageText.trim(); // Причина может содержать пробелы
                    state.step = Step.AWAITING_AMOUNT;
                    sendMessage(chatId, "Введите сумму расхода (например, 1500 или -500) (или /cancel для отмены):");
                }
                break;

            case AWAITING_AMOUNT:
                try {
                    double amount = Double.parseDouble(messageText.trim());
                    // Все данные собраны, добавляем запись
                    db.addRecord(chatId, state.date, state.reason, amount);
                    sendMessage(chatId, "Запись добавлена!");
                    userStates.remove(chatId); // Завершаем операцию, убираем состояние

                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Неверный формат суммы. Введите число (например, 1500 или -500) (или /cancel для отмены):");
                } catch (SQLException e) {
                    sendMessage(chatId, "Ошибка базы данных при добавлении записи: " + e.getMessage());
                    userStates.remove(chatId); // В случае ошибки тоже сбрасываем состояние
                }
                break;
        }
    }

    private void processInteractiveReport(String chatId, UserState state, String messageText) {
        switch (state.step) {
            case AWAITING_DATE:
                try {
                    Date date = new Date(sdf.parse(messageText).getTime());
                    sendDailyReport(chatId, date);
                    userStates.remove(chatId); // Завершаем операцию
                } catch (ParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Используйте YYYY-MM-dd. Попробуйте снова ввести дату (или /cancel для отмены):");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private void processInteractiveReportRange(String chatId, UserState state, String messageText) {
        switch (state.step) {
            case AWAITING_START_DATE:
                try {
                    Date startDate = new Date(sdf.parse(messageText).getTime());
                    state.startDate = startDate;
                    state.step = Step.AWAITING_END_DATE;
                    sendMessage(chatId, "Теперь введите конечную дату в формате YYYY-MM-dd (или /cancel для отмены):");
                } catch (ParseException e) {
                    sendMessage(chatId, "Неверный формат начальной даты. Используйте YYYY-MM-dd. Попробуйте снова ввести дату (или /cancel для отмены):");
                }
                break;

            case AWAITING_END_DATE:
                try {
                    Date endDate = new Date(sdf.parse(messageText).getTime());
                    state.endDate = endDate;

                    if (state.endDate.before(state.startDate)) {
                        sendMessage(chatId, "Конечная дата не может быть раньше начальной. Попробуйте снова ввести команду /report_range (или /cancel для отмены)");
                        userStates.remove(chatId); // Сбрасываем состояние
                        return;
                    }

                    sendRangeReport(chatId, state.startDate, state.endDate);
                    userStates.remove(chatId); // Завершаем операцию

                } catch (ParseException e) {
                    sendMessage(chatId, "Неверный формат конечной даты. Используйте YYYY-MM-dd. Попробуйте снова ввести дату (или /cancel для отмены):");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private void processInteractiveEdit(String chatId, UserState state, String messageText) {
        switch (state.step) {
            case AWAITING_ID:
                try {
                    int id = Integer.parseInt(messageText.trim());
                    state.id = id;
                    state.step = Step.AWAITING_EDIT_FULL_COMMAND;
                    sendMessage(chatId, "Введите новые данные для записи ID " + id + " в формате:\nдата причина сумма (или /cancel для отмены)\nНапример: 2025-10-05 Ужин 600");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Неверный формат ID. Введите число (или /cancel для отмены):");
                }
                break;

            case AWAITING_EDIT_FULL_COMMAND:
                String[] parts = messageText.split(" ", 3); // дата причина сумма
                if (parts.length < 3) {
                    sendMessage(chatId, "Неверный формат. Используйте формат: дата причина сумма (или /cancel для отмены)");
                    return;
                }
                try {
                    Date date = new Date(sdf.parse(parts[0]).getTime());
                    String reason = parts[1];
                    double amount = Double.parseDouble(parts[2]);

                    boolean updated = db.updateRecord(chatId, state.id, date, reason, amount);
                    if (updated) {
                        sendMessage(chatId, "Запись ID " + state.id + " успешно обновлена!");
                    } else {
                        sendMessage(chatId, "Запись с указанным ID не найдена или не принадлежит вам.");
                    }
                    userStates.remove(chatId); // Завершаем операцию

                } catch (ParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Используйте YYYY-MM-dd. Попробуйте снова ввести данные в формате: дата причина сумма (или /cancel для отмены)");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Неверный формат суммы. Попробуйте снова ввести данные в формате: дата причина сумма (или /cancel для отмены)");
                } catch (SQLException e) {
                    sendMessage(chatId, "Ошибка базы данных: " + e.getMessage());
                    userStates.remove(chatId); // В случае ошибки тоже сбрасываем состояние
                }
                break;
        }
    }

    private void processInteractiveDelete(String chatId, UserState state, String messageText) {
        switch (state.step) {
            case AWAITING_ID:
                try {
                    int id = Integer.parseInt(messageText.trim());
                    boolean deleted = db.deleteRecord(chatId, id);
                    if (deleted) {
                        sendMessage(chatId, "Запись ID " + id + " успешно удалена.");
                    } else {
                        sendMessage(chatId, "Запись с указанным ID не найдена или не принадлежит вам.");
                    }
                    userStates.remove(chatId); // Завершаем операцию

                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Неверный формат ID. Введите число (или /cancel для отмены):");
                } catch (SQLException e) {
                    sendMessage(chatId, "Ошибка базы данных: " + e.getMessage());
                    userStates.remove(chatId); // В случае ошибки тоже сбрасываем состояние
                }
                break;
        }
    }



    private void processCallback(CallbackQuery callbackQuery) {
        try {
            telegramClient.execute(org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Этот метод больше не используется, т.к. все интерактивные вводы обрабатываются в processInteractiveInput
    private void processEditInput(String chatId, String messageText) {
        // Логика перемещена в processInteractiveEdit
    }

    // Этот метод не используется в текущих отчетах
    private void sendRecordWithEditButton(String chatId, int recordId, String text) {
        // ... (код без изменений, но не используется в текущих отчетах)
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}