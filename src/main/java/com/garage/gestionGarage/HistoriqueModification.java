package com.garage.gestionGarage;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HistoriqueModification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entite;    
    private Long entiteId;
    private String champModifie;
    private String ancienneValeur;
    private String nouvelleValeur;
    private LocalDateTime dateModification;

    // Constructeurs
    public HistoriqueModification() {}

    public HistoriqueModification(String entite, Long entiteId, String champModifie,
                                   String ancienneValeur, String nouvelleValeur, LocalDateTime dateModification) {
        this.entite = entite;
        this.entiteId = entiteId;
        this.champModifie = champModifie;
        this.ancienneValeur = ancienneValeur;
        this.nouvelleValeur = nouvelleValeur;
        this.dateModification = dateModification;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	public Long getEntiteId() {
		return entiteId;
	}

	public void setEntiteId(Long entiteId) {
		this.entiteId = entiteId;
	}

	public String getChampModifie() {
		return champModifie;
	}

	public void setChampModifie(String champModifie) {
		this.champModifie = champModifie;
	}

	public String getAncienneValeur() {
		return ancienneValeur;
	}

	public void setAncienneValeur(String ancienneValeur) {
		this.ancienneValeur = ancienneValeur;
	}

	public String getNouvelleValeur() {
		return nouvelleValeur;
	}

	public void setNouvelleValeur(String nouvelleValeur) {
		this.nouvelleValeur = nouvelleValeur;
	}

	public LocalDateTime getDateModification() {
		return dateModification;
	}

	public void setDateModification(LocalDateTime dateModification) {
		this.dateModification = dateModification;
	}

 }