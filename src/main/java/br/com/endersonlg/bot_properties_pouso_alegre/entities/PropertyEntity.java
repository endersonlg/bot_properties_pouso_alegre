package br.com.endersonlg.bot_properties_pouso_alegre.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "properties")
public class PropertyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "real_estate")
  private String realEstate;

  private String title;

  private Double rent;

  @Column(name = "picture_url", columnDefinition = "TEXT")
  private String pictureUrl;

  private String url;

  private String neighborhood;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
