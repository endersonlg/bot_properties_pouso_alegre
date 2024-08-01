package br.com.endersonlg.bot_properties_pouso_alegre.dto;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PousoAlegreImoveisDataDTO {
  private List<Property> data;

  @Data
  public class Property {

    private String id;
    private String price;
    private String transaction;
    private boolean isPreviousPrice;
    private Map<String, Area> areas;
    private Map<String, Room> rooms;
    @SerializedName("title_formatted")
    private String titleFormatted;
    private String url;
    private Address address;
    private List<Image> images;
  }

  @Data
  public class Area {
    private String name;
    private String title;
    private String value;
    private String measure;
  }

  @Data
  public class Room {
    private String name;
    private String title;
    private int value;
    private String titleFormatted;
  }

  @Data
  public class Address {
    private String formatted;
  }

  @Data
  public class Image {
    private String id;
    private int gallery;
    private String caption;
    private int order;
    @SerializedName("file_url")
    private FileUrl fileUrl;
    private boolean isExternal;
  }

  @Data
  public class FileUrl {
    private String large;
    private String medium;
  }
}