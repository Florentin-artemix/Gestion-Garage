package com.garage.gestionGarage;

import jakarta.persistence.*;

@Entity
public class SuivieVidange {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="machineId")
	private Machine machine;
	private int dernierSceanceKm;
	private int frequence=2500;
	@Column(nullable=false)
    private boolean actif=true;
	private int prochainSceanceKm;
	@ManyToOne
	@JoinColumn(name="stock_id")
	private Stock stock;
	private double quantiteUtiliser;
	private boolean alerteVidange=false;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public Machine getMachine() {
		return machine;
	}
	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	public int getDernierSceanceKm() {
		return dernierSceanceKm;
	}
	public void setDernierSceanceKm(int dernierSceanceKm) {
		this.dernierSceanceKm = dernierSceanceKm;
	}
	public int getFrequence() {
		return frequence;
	}
	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}
	public int getProchainSceanceKm() {
		return prochainSceanceKm;
	}
	public void setProchainSceanceKm(int prochainSceanceKm) {
		this.prochainSceanceKm = prochainSceanceKm;
	}
	public double getQuantiteUtiliser() {
		return quantiteUtiliser;
	}
	public void setQuantiteUtiliser(double quantiteUtiliser) {
		this.quantiteUtiliser = quantiteUtiliser;
	}
	public boolean isAlerteVidange() {
		return alerteVidange;
	}
	public void setAlerteVidange(boolean alerteVidange) {
		this.alerteVidange = alerteVidange;
	}
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
}
