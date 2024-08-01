package br.com.endersonlg.bot_properties_pouso_alegre.dto;

import java.util.List;

import lombok.Data;

@Data
public class MottaResponseDataDTO {
  private List<Property> lista;
  private Integer quantidade;

  @Data
  public class Property {
    private String codigo;

    private String titulo;

    private String valor;

    private String urlfotoprincipalp;

    private String bairro;

    private String endereco;

    private String numeroquartos;

    private String areaprincipal;

    private String datahoracadastro;

  }
}
