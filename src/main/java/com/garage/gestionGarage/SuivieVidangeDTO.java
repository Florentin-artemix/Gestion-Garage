
package com.garage.gestionGarage;

public record SuivieVidangeDTO(Long id,String typeMachine,String modeleMachine,int dernierSceanceKm,
		int frequence,int prochainSceance,String produitUtiliser,double quantiteUtiliser,boolean alerteVidange) {}
