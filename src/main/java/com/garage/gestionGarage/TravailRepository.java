package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TravailRepository extends JpaRepository<Travail, Long> {
	List<Travail> findByActifTrue();
	List<Travail> findByActifFalse();
}