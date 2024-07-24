package br.com.endersonlg.bot_properties_pouso_alegre.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.endersonlg.bot_properties_pouso_alegre.entities.PropertyEntity;

public interface PropertyRepository extends JpaRepository<PropertyEntity, UUID> {
  Optional<PropertyEntity> findByRealEstateAndPictureUrl(String realEstate,
      String pictureUrl);
}
