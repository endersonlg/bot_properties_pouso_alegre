package br.com.endersonlg.bot_properties_pouso_alegre.telegram;

import java.io.File;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import br.com.endersonlg.bot_properties_pouso_alegre.utils.FileUtils;

@Component
public class Bot extends TelegramLongPollingBot {

  @Override
  public String getBotUsername() {
    return "imovel_pouso_alegre_bot";
  }

  @Override
  public String getBotToken() {
    return "7269552552:AAGutJtYZ5ip7z-ZKfWS6_DnSWE5wUsSGe4";
  }

  @Override
  public void onUpdateReceived(Update update) {
    var msg = update.getMessage();
    var user = msg.getFrom();

    System.out.println(user.getFirstName() + " wrote " + msg.getText());
  }

  public void sendText(Long who, String what) {
    SendMessage sm = SendMessage.builder()
        .chatId(who.toString()) // Who are we sending a message to
        .text(what).build(); // Message content
    try {
      execute(sm); // Actually sending the message
    } catch (TelegramApiException e) {
      throw new RuntimeException(e); // Any error will be printed here
    }
  }

  public void sendPhoto(Long who, String photoPath) {

    try {
      File imageFile = FileUtils.downloadImage(photoPath);

      SendPhoto sp = SendPhoto.builder()
          .chatId(who.toString()) // Quem vai receber a foto
          .photo(new InputFile(imageFile)) // Arquivo da foto
          .build();

      execute(sp); // Enviando a foto

      imageFile.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}