package br.com.endersonlg.bot_properties_pouso_alegre.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.endersonlg.bot_properties_pouso_alegre.dto.GenericRealEstateResponseDataDTO;
import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;
import br.com.endersonlg.bot_properties_pouso_alegre.repositories.PropertyRepository;
import br.com.endersonlg.bot_properties_pouso_alegre.utils.HttpUtils;

@Service
public class GenericRealEstateService {
  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  TelegramService telegramService;

  public List<PropertyEntity> execute(String baseUrl, String pathUrl, String realEstate) {
    try {
      String response = HttpUtils.getHttpResponse(baseUrl + pathUrl);

      Gson gson = new Gson();

      GenericRealEstateResponseDataDTO responseDataGeneric = gson.fromJson(response.toString(),
          GenericRealEstateResponseDataDTO.class);

      List<GenericRealEstateResponseDataDTO.Property> genericDatas = responseDataGeneric.getData();

      List<PropertyEntity> properties = new ArrayList<PropertyEntity>();

      genericDatas.forEach(genericData -> {
        var propertyExists = propertyRepository.findByRealEstateAndPictureUrl(realEstate,
            genericData.getPicture_full());

        if (!propertyExists.isPresent()) {
          PropertyEntity propertyEntity = PropertyEntity.builder()
              .realEstate(realEstate)
              .title(genericData.getWebsite_title())
              .rent(genericData.getTotal_rent())
              .pictureUrl(genericData.getPicture_full())
              .url(baseUrl + genericData.getUrl())
              .neighborhood(genericData.getNeighborhood())
              .build();

          properties.add(propertyEntity);
        }
      });

      return properties;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
