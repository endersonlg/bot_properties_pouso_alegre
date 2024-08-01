package br.com.endersonlg.bot_properties_pouso_alegre.service;

import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.endersonlg.bot_properties_pouso_alegre.dto.MottaResponseDataDTO;
import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;
import br.com.endersonlg.bot_properties_pouso_alegre.repositories.PropertyRepository;
import br.com.endersonlg.bot_properties_pouso_alegre.utils.HttpUtils;

@Service
public class MottaService {
  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  TelegramService telegramService;

  public List<PropertyEntity> execute() {
    try {
      Integer paginas = 1;

      List<PropertyEntity> properties = new ArrayList<PropertyEntity>();

      for (Integer i = 1; i <= paginas; i++) {
        String formBody = "imovel%5Bfinalidade%5D=aluguel&imovel%5Bcodigounidade%5D=&imovel%5Bcodigosimoveis%5D=&imovel%5BcodigoTipo%5D%5Bcodigo%5D%5B%5D=2&imovel%5BcodigoTipo%5D%5Bcodigo%5D%5B%5D=1&imovel%5BcodigoTipo%5D%5Bnome%5D%5B%5D=apartamento&imovel%5BcodigoTipo%5D%5Bnome%5D%5B%5D=casa&imovel%5Bcodigocidade%5D=2&imovel%5Bcodigoregiao%5D=0&imovel%5Bcodigosbairros%5D%5Bnome%5D=0&imovel%5Bcodigosbairros%5D%5Bcodigo%5D=0&imovel%5Bendereco%5D=0&imovel%5Bnumeroquartos%5D=0-quartos&imovel%5Bnumerovagas%5D=0-vaga-ou-mais&imovel%5Bnumerobanhos%5D=0-banheiro-ou-mais&imovel%5Bnumerosuite%5D=0-suite-ou-mais&imovel%5Bnumerovaranda%5D=0&imovel%5Bnumeroelevador%5D=0&imovel%5Bvalorde%5D=0&imovel%5Bvalorate%5D=0&imovel%5Bareade%5D=0&imovel%5Bareaate%5D=0&imovel%5Bextras%5D=0&imovel%5Bextends%5D=false&imovel%5Bmobiliado%5D=false&imovel%5Bdce%5D=false&imovel%5Bpiscina%5D=false&imovel%5Bsauna%5D=false&imovel%5Bsalaofestas%5D=false&imovel%5Bacademia%5D=false&imovel%5BboxDespejo%5D=false&imovel%5Bportaria24h%5D=false&imovel%5Baceitafinanciamento%5D=false&imovel%5Barealazer%5D=false&imovel%5Bquartoqtdeexata%5D=false&imovel%5Bvagaqtdexata%5D=false&imovel%5Bdestaque%5D=0&imovel%5Bopcaoimovel%5D=4&imovel%5Bretornomapa%5D=false&imovel%5Bretornomapaapp%5D=false&imovel%5Bnumeropagina%5D=1&imovel%5Bnumeroregistros%5D=20&imovel%5Bordenacao%5D=valorasc&imovel%5Bpagina%5D="
            + i
            + "&imovel%5Bcodigocondominio%5D=0&imovel%5Bcondominio%5D%5B%5D=todos-os-condominios&imovel%5Bcondominio%5D%5B%5D=valorminimo%3D0%26valormaximo%3D0%26pagina%3D1";

        String response = HttpUtils.postHttpResponse(
            "https://www.imobiliariamotta.com.br/imoveis/ajax/",
            BodyPublishers.ofString(formBody),
            "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Gson gson = new Gson();

        MottaResponseDataDTO responseDataGeneric = gson.fromJson(response.toString(),
            MottaResponseDataDTO.class);

        List<MottaResponseDataDTO.Property> list = responseDataGeneric.getLista();

        list.forEach(mottaProperty -> {
          var propertyExists = propertyRepository.findByRealEstateAndPictureUrl("motta",
              mottaProperty.getUrlfotoprincipalp());

          if (!propertyExists.isPresent()) {
            PropertyEntity propertyEntity = PropertyEntity.builder()
                .realEstate("motta")
                .title(mottaProperty.getTitulo())
                .rent(Double.parseDouble(mottaProperty.getValor().substring(3).replaceAll("\\.", "")
                    .replaceAll(",", ".")))
                .pictureUrl(mottaProperty.getUrlfotoprincipalp())
                .url("https://www.imobiliariamotta.com.br/imovel/" + mottaProperty.getTitulo() + "/"
                    + mottaProperty.getCodigo())
                .neighborhood(mottaProperty.getEndereco() + ", " + mottaProperty.getBairro())
                .addedIn(mottaProperty.getDatahoracadastro())
                .area(mottaProperty.getAreaprincipal())
                .bedrooms(mottaProperty.getNumeroquartos())
                .build();

            properties.add(propertyEntity);
          }
        });

        paginas = (int) Math.ceil((responseDataGeneric.getQuantidade() + 0.00) / 20);

      }

      return properties;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
