package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {
	List<Entretien> findByActifTrue();
	List<Entretien> findByActifFalse();
}
