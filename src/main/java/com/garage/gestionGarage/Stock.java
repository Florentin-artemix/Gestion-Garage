package com.garage.gestionGarage;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String nomProduit;
    private double quantite;
    private String fournisseur;
    @Column(precision=6,scale=2)
    private BigDecimal prixUnitaire;
    private int seuilAlerte;
    @Column(nullable=false)
    private boolean actif=true;
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomProduit() {
		return nomProduit;
	}
	public void setNomProduit(String nomProduit) {
		this.nomProduit = nomProduit;
	}
	public double getQuantite() {
		return quantite;
	}
	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}
	public String getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(String fournisseur) {
		this.fournisseur = fournisseur;
	}
	public BigDecimal getPrixUnitaire() {
		return prixUnitaire;
	}
	public void setPrixUnitaire(BigDecimal prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}
	public int getSeuilAlerte() {
		return seuilAlerte;
	}
	public void setSeuilAlerte(int seuilAlerte) {
		this.seuilAlerte = seuilAlerte;
	}
    
}
