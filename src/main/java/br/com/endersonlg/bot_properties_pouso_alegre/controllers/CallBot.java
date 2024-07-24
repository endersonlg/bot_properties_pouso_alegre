package br.com.endersonlg.bot_properties_pouso_alegre.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import br.com.endersonlg.bot_properties_pouso_alegre.dto.GenericRealEstate;
import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;
import br.com.endersonlg.bot_properties_pouso_alegre.repositories.PropertyRepository;
import br.com.endersonlg.bot_properties_pouso_alegre.service.GenericRealEstateService;
import br.com.endersonlg.bot_properties_pouso_alegre.service.TelegramService;

@RestController
@RequestMapping("/call")
public class CallBot {
  @Autowired
  GenericRealEstateService genericRealEstateService;

  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  TelegramService telegramService;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @GetMapping("/bot")
  void call() throws TelegramApiException {
    try {

      // List<GenericRealEstate> genericRealEstates = new
      // ArrayList<GenericRealEstate>();

      List<GenericRealEstate> genericRealEstates = Arrays.asList(
          new GenericRealEstate("https://www.aristeurios.com.br", "aristeu"),
          new GenericRealEstate("https://www.tadeuimoveis.imb.br", "tadeu imoveis"),
          new GenericRealEstate("https://www.imobiliariapantanal.com.br", "pantanal"),
          new GenericRealEstate("https://www.imobiliariapedraforte.com.br", "pedra forte"),
          new GenericRealEstate("https://www.spheraimoveis.com.br", "sphera"),
          new GenericRealEstate("https://www.imoveisimpacta.com.br", "impacta"),
          new GenericRealEstate("https://www.nadirimoveis.com.br", "nadir"),
          new GenericRealEstate("https://www.tiagosilvaimoveis.com.br", "tiagosilvaimoveis"),
          new GenericRealEstate("https://www.imobiliariafonsecapa.com.br", "imobiliariafonsecapa"),
          new GenericRealEstate("https://www.jairosilvaimoveis.com.br", "jairosilvaimoveis"),
          new GenericRealEstate("https://www.imobiliariafranciscanunes.com.br", "imobiliariafranciscanunes"),
          new GenericRealEstate("https://www.paimoveispa.com.br", "paimoveispa"),
          new GenericRealEstate("https://www.vltimoveis.com.br", "vltimoveis"),
          new GenericRealEstate("https://www.lealimoveis.com", "lealimoveis"),
          new GenericRealEstate("https://www.kdimoveis.imb.br", "kdimoveis"));

      List<PropertyEntity> properties = new ArrayList<PropertyEntity>();

      genericRealEstates.forEach(genericRealEstate -> {
        properties.addAll(genericRealEstateService.execute(genericRealEstate.getUrl(),
            "/api/listings/para-alugar/apartamento/pouso-alegre?preco-de-locacao=600~2000&ordenar=recentes",
            genericRealEstate.getName()));
      });

      for (int i = 0; i < properties.size(); i++) {
        final int index = i;

        scheduler.schedule(() -> {

          propertyRepository.save(properties.get(index));
          telegramService.sendProperty(properties.get(index));
        }, index * 10, TimeUnit.SECONDS);
      }

      // genericRealEstateService.execute("https://www.aristeurios.com.br",
      // "",
      // "aristeu");
      // genericRealEstateService.execute("https://www.tadeuimoveis.imb.br",
      // "/api/listings/para-alugar/apartamento/pouso-alegre?preco-de-locacao=600~2000&ordenar=recentes",
      // "tadeu imoveis");
      // genericRealEstateService.execute("https://www.imobiliariapantanal.com.br",
      // null, "pantanal");

    } catch (Exception e) {

    }
  }
}
