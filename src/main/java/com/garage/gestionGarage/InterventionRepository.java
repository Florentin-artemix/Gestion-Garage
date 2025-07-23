package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
	List<Intervention> findByActifTrue();
	List<Intervention> findByActifFalse();
}
