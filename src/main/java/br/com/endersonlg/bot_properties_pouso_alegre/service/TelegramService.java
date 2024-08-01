package br.com.endersonlg.bot_properties_pouso_alegre.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;
import br.com.endersonlg.bot_properties_pouso_alegre.telegram.Bot;
import br.com.endersonlg.bot_properties_pouso_alegre.utils.DateTimeUtils;
import jakarta.annotation.PostConstruct;

@Service
public class TelegramService {

  @Autowired
  Bot bot;

  @PostConstruct
  public void initialize() {
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(bot);
    } catch (TelegramApiException e) {
      throw new RuntimeException("Failed to initialize Telegram bot", e);
    }
  }

  public void sendProperty(PropertyEntity propertyEntity) {

    try {
      bot.sendPhoto(-1002203224895L, propertyEntity.getPictureUrl());

      String text = propertyEntity.getTitle() + "\n" +
          "BAIRRO: " + propertyEntity.getNeighborhood() + "\n";

      if (propertyEntity.getBedrooms() != null && !propertyEntity.getBedrooms().isBlank()) {
        text = text + "QUARTOS: " + propertyEntity.getBedrooms() + "\n";
      }

      if (propertyEntity.getArea() != null && !propertyEntity.getArea().isBlank()) {
        text = text + "AREA: " + propertyEntity.getArea() + "\n";
      }

      if (propertyEntity.getAddedIn() != null && !propertyEntity.getAddedIn().isBlank()) {
        String addedIn = DateTimeUtils.formatISODateTime(propertyEntity.getAddedIn(), "dd/MM/yyyy HH:mm");

        text = text + "ADICIONADO EM: " + addedIn + "\n";
      }

      text = text + "VALOR TOTAL: R$" + propertyEntity.getRent() + "\n" +
          "IMOBILIÁRIA: " + propertyEntity.getRealEstate() + "\n" +
          propertyEntity.getUrl();

      bot.sendText(-1002203224895L, text);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void sendText(String message) {
    try {
      bot.sendText(-1002203224895L, message);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void sendPhoto(String url) {
    try {
      bot.sendPhoto(-1002203224895L, url);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}