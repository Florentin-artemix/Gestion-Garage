package com.garage.gestionGarage;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoriqueModificationRepository extends JpaRepository<HistoriqueModification, Long> {
    List<HistoriqueModification> findByEntiteAndEntiteId(String entite, Long entiteId);
    List<HistoriqueModification> findByEntite(String entite);
}