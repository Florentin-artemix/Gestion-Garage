package com.garage.gestionGarage;

public class TravailDTO {
	public Long id;
    public String nomTechnicien;
    public String prenomTechnicien;
    public String typeMachine;
    public String modeleMachine;
    public String heureDebut;
    public String heureFin;
    public String heureTravail;
    public String preuvePhotoBase64;
    public TravailDTO(Long id,String nomTechnicien,String prenomTechnicien,String typeMachine,String modeleMachine,String heureDebut,String heureFin,String heureTravail,String preuvePhotoBase64) {
    	this.id=id;
    	this.nomTechnicien=nomTechnicien;
    	this.prenomTechnicien=prenomTechnicien;
    	this.typeMachine=typeMachine;
    	this.modeleMachine=modeleMachine;
    	this.heureDebut=heureDebut;
    	this.heureFin=heureFin;
    	this.heureTravail=heureTravail;
    	this.preuvePhotoBase64=preuvePhotoBase64;
    }
}
