package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicienRepository extends JpaRepository<Technicien, Long> {
	List<Technicien> findByActifTrue();
	List<Technicien> findByActifFalse();
}
