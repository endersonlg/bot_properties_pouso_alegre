package br.com.endersonlg.bot_properties_pouso_alegre.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericRealEstate {
  private String url;
  private String name;
}
