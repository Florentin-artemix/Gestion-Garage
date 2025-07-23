package com.garage.gestionGarage;

import java.time.LocalDate;

public class EntretienDTO {
	private Long id;
    private String nomTechnicien;
    private String prenomTechnicien;
    private String typeMachine;
    private String modeleMachine;
    private LocalDate dateEntretien;
    private String typeEntretien;
    private String description;
    public EntretienDTO(Long id,String nomTechnicien,String prenomTechnicien,String typeMachine,String modeleMachine,LocalDate dateEntretien,String typeEntretien,String description) {
    	this.id=id;
    	this.nomTechnicien=nomTechnicien;
    	this.prenomTechnicien=prenomTechnicien;
    	this.typeMachine=typeMachine;
    	this.modeleMachine=modeleMachine;
    	this.dateEntretien=dateEntretien;
    	this.typeEntretien=typeEntretien;
    	this.description=description;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomTechnicien() {
		return nomTechnicien;
	}
	public void setNomTechnicien(String nomTechnicien) {
		this.nomTechnicien = nomTechnicien;
	}
	public String getPrenomTechnicien() {
		return prenomTechnicien;
	}
	public void setPrenomTechnicien(String prenomTechnicien) {
		this.prenomTechnicien = prenomTechnicien;
	}
	public String getTypeMachine() {
		return typeMachine;
	}
	public void setTypeMachine(String typeMachine) {
		this.typeMachine = typeMachine;
	}
	public String getModeleMachine() {
		return modeleMachine;
	}
	public void setModeleMachine(String modeleMachine) {
		this.modeleMachine = modeleMachine;
	}
	public LocalDate getDateEntretien() {
		return dateEntretien;
	}
	public void setDateEntretien(LocalDate dateEntretien) {
		this.dateEntretien = dateEntretien;
	}
	public String getTypeEntretien() {
		return typeEntretien;
	}
	public void setTypeEntretien(String typeEntretien) {
		this.typeEntretien = typeEntretien;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
}
