package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SuivieVidangeRepository extends JpaRepository<SuivieVidange, Long> {
	List<SuivieVidange> findByActifTrue();
	List<SuivieVidange> findByActifFalse();
}
