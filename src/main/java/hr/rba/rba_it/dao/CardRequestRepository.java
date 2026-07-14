package hr.rba.rba_it.dao;

import hr.rba.rba_it.model.CardRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequestEntity, UUID> {

    Optional<CardRequestEntity> findByOib(String oib);

    void deleteByOib(String oib);
}
