package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<ProduitUtiliser, Long> {
   List<ProduitUtiliser> findByActifTrue();
   List<ProduitUtiliser> findByActifFalse();
}
