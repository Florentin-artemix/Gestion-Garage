package com.garage.gestionGarage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProduitUtiliser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="intervention_id")
    private Intervention intervention;
    @ManyToOne
    @JoinColumn(name="stock_id")
    private Stock stock;
    @Column(nullable=false)
    private boolean actif=true;
    private double quantiteUtiliser;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Intervention getIntervention() {
		return intervention;
	}
	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public double getQuantiteUtiliser() {
		return quantiteUtiliser;
	}
	public void setQuantiteUtiliser(double quantiteUtiliser) {
		this.quantiteUtiliser = quantiteUtiliser;
	}
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
    
}
