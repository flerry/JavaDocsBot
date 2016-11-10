package com.flerry.tgbot.javadocs;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;

public class TgBot extends TelegramLongPollingBot {
	private static final String COMMAND_HELP = "/help";
	private static final String COMMAND_START = "/start";
	private static final String COMMAND_HELP_BOT = "/help@JavaDocsBot";
	private static final String COMMAND_START_BOT = "/start@JavaDocsBot";
	private static final String COMMAND_APPEAL = "@JavaDocsBot";
	private static final String COMMAND_APPEAL_WITH_SPACE = "@JavaDocsBot ";
	private static final String MESSAGE_HELP = "Привет, я JavaDocsBot. Я всегда готов помочь тебе, о Java-кодер, в поиске документации :) Пожалуйста, напиши нужный класс/метод/исключение для поиска нужной документации";
	private static final String MESSAGE_NON_RESULT = "Извини, но я не смог найти информации по твоему запросу";
	private static final String MESSAGE_GET_RESULT_TIME_OUT = "Ошибка поиска, попробуй позже";
	private static final String MESSAGE_FROM_GROUP_CHAT = "Сообщение из группового чата";

	public static void main(String[] args) {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new TgBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public String getBotUsername() {

		return "bot_name";
	}

	public String getBotToken() {
		return "bot_token";
	}

	public void onUpdateReceived(Update update) {

		Message message = update.getMessage();
		if (message != null && message.hasText()) {
			if (message.getText().equals(COMMAND_START_BOT)
					|| message.getText().equals(COMMAND_HELP_BOT)
					|| message.getText().equals(COMMAND_START)
					|| message.getText().equals(COMMAND_HELP)) {
				sendMsg(message, MESSAGE_HELP);

			}
			if (message.getText().contains(COMMAND_APPEAL)
					&& !message.getText().equals(COMMAND_HELP_BOT)
					&& !message.getText().equals(COMMAND_START_BOT)) {
				try {
					sendMsg(message,
							ParseDocs.docsTextLink(message.getText()
									.replace(COMMAND_APPEAL_WITH_SPACE, "")
									.substring(0, 1).toUpperCase()
									+ message
											.getText()
											.replace(COMMAND_APPEAL_WITH_SPACE,
													"").substring(1)));
				} catch (IOException e) {
					sendMsg(message, MESSAGE_GET_RESULT_TIME_OUT);
				} catch (IndexOutOfBoundsException a) {
					sendMsg(message, MESSAGE_NON_RESULT);
				}
			}
			if (message.getChatId() < 0) {
				System.out.println(MESSAGE_FROM_GROUP_CHAT);
			} else if (!message.getText().contains(COMMAND_HELP)
					&& !message.getText().contains(COMMAND_START)
					&& !message.getText().contains(COMMAND_APPEAL)) {
				try {
					sendMsg(message,
							(ParseDocs.docsTextLink(message.getText()
									.substring(0, 1).toUpperCase()
									+ message.getText().substring(1))));
				} catch (IOException e) {
					sendMsg(message, MESSAGE_GET_RESULT_TIME_OUT);
				} catch (IndexOutOfBoundsException a) {
					sendMsg(message, MESSAGE_NON_RESULT);
				}

			}

		}
	}

	private void sendMsg(Message message, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(true);
		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setReplyToMessageId(message.getMessageId());
		sendMessage.setText(text);
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
