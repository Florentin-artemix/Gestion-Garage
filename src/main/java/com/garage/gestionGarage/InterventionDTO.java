package com.garage.gestionGarage;

import java.time.LocalDate;
import java.util.Base64;

public class InterventionDTO {
	private Long id;
	private String nomTechnicien;
	private String prenomTechnicien;
	private String modeleMachine;
	private String typeMachine;
	private String typeIntervention;
	private String description;
	private int kmOuHeureMoteur;
    private String photoIntervention;
	private String signatures;
	private boolean validationChefGarage;
	private LocalDate dateIntervention;
	public InterventionDTO(Long id,String nomTechnicien,String prenomTechnicien,String typeMachine,String modeleMachine,String typeIntervention,String description,
			int kmOuHeureMoteur,byte[] photoIntervention,byte[] signatures,boolean validationChefGarage,LocalDate dateIntervention) {
		this.id=id;
		this.nomTechnicien=nomTechnicien;
		this.prenomTechnicien=prenomTechnicien;
		this.typeMachine=typeMachine;
		this.modeleMachine=modeleMachine;
		this.typeIntervention=typeIntervention;
		this.description=description;
		this.kmOuHeureMoteur=kmOuHeureMoteur;
		this.photoIntervention=photoIntervention!=null?Base64.getEncoder().encodeToString(photoIntervention):null;
		this.signatures=signatures!=null?Base64.getEncoder().encodeToString(signatures):null;
		this.dateIntervention=dateIntervention;
		this.validationChefGarage=validationChefGarage;
	}
	public String getNomTechnicien() {
		return nomTechnicien;
	}
	public String getPrenomTechnicien() {
		return prenomTechnicien;
	}
	public String getModeleMachine() {
		return modeleMachine;
	}
	public String getTypeMachine() {
		return typeMachine;
	}
	public String getTypeIntervention() {
		return typeIntervention;
	}
	public String getDescription() {
		return description;
	}
	public int getKmOuHeureMoteur() {
		return kmOuHeureMoteur;
	}
	public String getPhotoIntervention() {
		return photoIntervention;
	}
	public String getSignatures() {
		return signatures;
	}
	public boolean isValidationChefGarage() {
		return validationChefGarage;
	}
	public Long getId() {
		return id;
	}
	public LocalDate getDateIntervention() {
		return dateIntervention;
	}
	public void setDateIntervention(LocalDate dateIntervention) {
		this.dateIntervention = dateIntervention;
	}
}
