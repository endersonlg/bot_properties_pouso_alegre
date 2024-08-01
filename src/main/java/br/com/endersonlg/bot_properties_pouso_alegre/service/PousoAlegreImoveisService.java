package br.com.endersonlg.bot_properties_pouso_alegre.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.endersonlg.bot_properties_pouso_alegre.dto.PousoAlegreImoveisDataDTO;
import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;
import br.com.endersonlg.bot_properties_pouso_alegre.repositories.PropertyRepository;
import br.com.endersonlg.bot_properties_pouso_alegre.utils.HttpUtils;

@Service
public class PousoAlegreImoveisService {
  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  TelegramService telegramService;

  private int i = 0;

  public List<PropertyEntity> execute() {
    try {
      String response = HttpUtils.getHttpResponse(
          "https://api-sites.gerenciarimoveis-cf.com.br/api/properties?custom_query=card&sort=-created_at%2Cid&offset=1&limit=99999&with_grouped_condos=true&filter%5Btransaction%5D=2&filter%5Bby_area%5D%5Bname%5D=total_area&filter%5Bby_area%5D%5Bmeasure%5D=m%C2%B2&filter%5Bby_neighborhood_or_city_slug%5D=pouso-alegre-mg&include=subtype.type%2Cuser&with_title=true",
          "X-Domain", "pousoalegreimoveis.com.br");

      Gson gson = new Gson();

      PousoAlegreImoveisDataDTO responseDataGeneric = gson.fromJson(response.toString(),
          PousoAlegreImoveisDataDTO.class);

      List<PousoAlegreImoveisDataDTO.Property> genericDatas = responseDataGeneric.getData();

      List<PropertyEntity> properties = new ArrayList<PropertyEntity>();

      genericDatas.forEach(genericData -> {

        System.out.println(this.i);
        this.i++;
        if (genericData.getImages().size() > 0 && genericData.getImages().get(0).getFileUrl() != null) {

          var propertyExists = propertyRepository.findByRealEstateAndPictureUrl("pouso alegre imoveis",
              genericData.getImages().get(0).getFileUrl().getLarge());

          if (!propertyExists.isPresent() && genericData.getTransaction().equals("ALUGUEL")) {
            PropertyEntity propertyEntity = PropertyEntity.builder()
                .realEstate("pouso alegre imoveis")
                .title(genericData.getTitleFormatted())
                .pictureUrl(genericData.getImages().get(0).getFileUrl().getLarge())
                .url("https://pousoalegreimoveis.com.br/imovel/" + genericData.getUrl())
                .neighborhood(genericData.getAddress().getFormatted())
                .build();

            if (genericData.getPrice().contains("R$")) {
              propertyEntity.setRent(Double.parseDouble(genericData.getPrice().substring(2).replaceAll("\\.", "")
                  .replaceAll(",", ".")));
            }

            if (genericData.getAreas().containsKey("primary_area")) {
              propertyEntity.setArea(genericData.getAreas().get("primary_area").getValue());
            }

            if (genericData.getRooms().containsKey("bedroom")) {
              propertyEntity.setBedrooms(Integer.toString(genericData.getRooms().get("bedroom").getValue()));
            }

            properties.add(propertyEntity);
          }
        }
      });

      return properties;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
