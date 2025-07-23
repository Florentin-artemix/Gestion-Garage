package com.garage.gestionGarage;

import java.time.LocalDateTime;

public class HistoriqueDTO {
    private String entite;
    private Long entiteId;
    private String champModifie;
    private String ancienneValeur;
    private String nouvelleValeur;
    private LocalDateTime dateModification;

    public HistoriqueDTO(String entite, Long entiteId, String champModifie, String ancienneValeur, String nouvelleValeur, LocalDateTime dateModification) {
        this.entite = entite;
        this.entiteId = entiteId;
        this.champModifie = champModifie;
        this.ancienneValeur = ancienneValeur;
        this.nouvelleValeur = nouvelleValeur;
        this.dateModification = dateModification;
    }
    
    public String getEntite() { return entite; }
    public Long getEntiteId() { return entiteId; }
    public String getChampModifie() { return champModifie; }
    public String getAncienneValeur() { return ancienneValeur; }
    public String getNouvelleValeur() { return nouvelleValeur; }
    public LocalDateTime getDateModification() { return dateModification; }
}