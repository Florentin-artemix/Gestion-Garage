package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
	List<Stock> findByActifTrue();
	List<Stock> findByActifFalse();
}
