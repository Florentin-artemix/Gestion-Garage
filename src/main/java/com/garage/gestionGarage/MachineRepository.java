package com.garage.gestionGarage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
   List<Machine> findByStatutActuelNot(Statut statut);
   List<Machine> findByStatutActuel(Statut statut);
}