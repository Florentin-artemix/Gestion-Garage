package com.garage.gestionGarage;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Intervention {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="machineId")
    private Machine machine;
    @ManyToOne
    @JoinColumn(name="technicienId")
    private Technicien technicien;
    private String typeIntervention;
    @Column(columnDefinition="TEXT")
    private String description;
    private int kmOuHeureMoteur;
    private byte[] photoIntervention;
    private byte[] signatures;
    @Column(name="date_intervention")
    private LocalDate dateIntervention;
    private boolean validationChefGarage=false;
    @Column(nullable=false)
    private boolean actif=true;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Machine getMachine() {
		return machine;
	}
	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	public Technicien getTechnicien() {
		return technicien;
	}
	public void setTechnicien(Technicien technicien) {
		this.technicien = technicien;
	}
	public String getTypeIntervention() {
		return typeIntervention;
	}
	public void setTypeIntervention(String typeIntervention) {
		this.typeIntervention = typeIntervention;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getKmOuHeureMoteur() {
		return kmOuHeureMoteur;
	}
	public void setKmOuHeureMoteur(int kmOuHeureMoteur) {
		this.kmOuHeureMoteur = kmOuHeureMoteur;
	}
	public byte[] getPhotoIntervention() {
		return photoIntervention;
	}
	public void setPhotoIntervention(byte[] photoIntervention) {
		this.photoIntervention = photoIntervention;
	}
	public byte[] getSignatures() {
		return signatures;
	}
	public void setSignatures(byte[] signatures) {
		this.signatures = signatures;
	}
	public boolean isValidationChefGarage() {
		return validationChefGarage;
	}
	public void setValidationChefGarage(boolean validationChefGarage) {
		this.validationChefGarage = validationChefGarage;
	}
	public LocalDate getDateIntervention() {
		return dateIntervention;
	}
	public void setDateIntervention(LocalDate dateIntervention) {
		this.dateIntervention = dateIntervention;
	}
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
    
}
