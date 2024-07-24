package br.com.endersonlg.bot_properties_pouso_alegre.dto;

import java.util.List;

import lombok.Data;

@Data
public class GenericRealEstateResponseDataDTO {
  private List<Property> data;

  @Data
  public class Property {
    private String website_title;
    private Double total_rent;
    private String picture_full;
    private String url;
    private String neighborhood;
    private List<String> area;
    private List<String> bedrooms;
    private String updated_at;
  }

}
