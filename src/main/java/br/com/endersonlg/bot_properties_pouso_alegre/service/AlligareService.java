package br.com.endersonlg.bot_properties_pouso_alegre.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;
import br.com.endersonlg.bot_properties_pouso_alegre.repositories.PropertyRepository;

@Service
public class AlligareService {

  @Autowired
  PropertyRepository propertyRepository;

  public List<PropertyEntity> execute() {
    try {
      // URL da primeira página
      String url = "https://alligareimoveis.com.br/busca/?listagem=grid&pagina=1&ordenar=menor_preco";

      Document doc = Jsoup.connect(url).get();

      String lastPage = doc.select(".pagination > li:nth-child(6) >a").text();

      Integer lastPageNumber = Integer.parseInt(lastPage);

      // Iterar por todas as páginas (ajuste o número de páginas conforme necessário)

      List<PropertyEntity> listProperties = new ArrayList<PropertyEntity>();

      for (int page = 1; page <= lastPageNumber; page++) {
        doc = Jsoup.connect(url.replace("pagina=1", "pagina=" + page)).get();

        Elements properties = doc.select("[data-valor-locacao]");

        for (Element property : properties) {
          if (property.select(".label.forrent").text().equals("Aluguel")) {
            String rentText = property.select(".valor-imovel .price").text().substring(3).replaceAll("\\.", "")
                .replaceAll(",", ".");
            Double rent = Double.parseDouble(rentText);

            String style = property.select(".img-destaque-imovel").attr("style");
            Integer imageUrlIndex = style.lastIndexOf("url('");
            String imageUrl = style.substring(imageUrlIndex + 5, style.length() - 2);

            String neighborhood = property.select("address").text();

            String area = property.select(".pull-left > li").text();

            if (!area.isBlank() && area.contains("Área:")) {
              area = area.split(":")[1].trim();
            }

            String propertyUrl = property.select(".property-thumb-info-image > a").attr("href");

            String title = property.select(".property-thumb-info-content").text();

            if (!imageUrl.isBlank()) {
              if (!propertyRepository.findByRealEstateAndPictureUrl("alligare", imageUrl).isPresent()) {
                PropertyEntity propertyEntity = PropertyEntity.builder().title(title).area(area).pictureUrl(imageUrl)
                    .url(propertyUrl).rent(rent).neighborhood(neighborhood).realEstate("alligare").build();

                listProperties.add(propertyEntity);
              }
            }
          }
        }
      }

      return listProperties;
    } catch (Exception e) {
      System.out.println("caiu no catch: " + e.getMessage());
    }
    return null;

  }
}
