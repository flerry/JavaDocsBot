package com.flerry.tgbot.javadocs;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;

public class TgBot extends TelegramLongPollingBot {
	public static void main(String[] args) {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new TgBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public String getBotUsername() {
<<<<<<< HEAD
		return "JavaDocsBot";
=======
		return "your_bot_name";
>>>>>>> 131cd9bb14032d2aa8923da8304a2869f985461c
	}

	public String getBotToken() {
		return "your_token";
	}

	public void onUpdateReceived(Update update) {

		Message message = update.getMessage();
		if (message != null && message.hasText()) {
			if (message.getText().equals("/start")
					|| message.getText().equals("/help")
					|| message.getText().equals("/help@JavaDocsBot")
					|| message.getText().equals("/start@JavaDocsBot")) {
				sendMsg(message,
						"Привет, я JavaDocBot. Я всегда готов помочь тебе, о Java-кодер, в поиске документации :) Пожалуйста, напиши нужный класс/метод/исключение для поиска нужной документации");

			}
			if (message.getText().contains("@JavaDocsBot")
					&& !message.getText().equals("/help@JavaDocsBot")
					&& !message.getText().equals("/start@JavaDocsBot")) {
				try {
					sendMsg(message,
							ParseDocs.docsTextLink(message.getText()
									.replace("@JavaDocsBot ", "")
									.substring(0, 1).toUpperCase()
									+ message.getText()
											.replace("@JavaDocsBot ", "")
											.substring(1)));
				} catch (IOException e) {
					sendMsg(message, "Ошибка поиска, попробуй позже");
				} catch (IndexOutOfBoundsException a) {
					sendMsg(message,
							"Извини, но я не смог найти информации по твоему запросу");
				}
			}
			if (message.getChatId() < 0) {
				System.out.println("Сообщение из группового чата");
			} else if (!message.getText().contains("/help")
					&& !message.getText().contains("/start")
					&& !message.getText().contains("@JavaDocsBot")) {
				try {
					sendMsg(message,
							(ParseDocs.docsTextLink(message.getText()
									.substring(0, 1).toUpperCase()
									+ message.getText().substring(1))));
				} catch (IOException e) {
					sendMsg(message, "Ошибка поиска, попробуй позже");
				} catch (IndexOutOfBoundsException a) {
					sendMsg(message,
							"Извини, но я не смог найти информации по твоему запросу");
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
