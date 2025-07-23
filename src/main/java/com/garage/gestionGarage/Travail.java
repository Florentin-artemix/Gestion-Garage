package com.garage.gestionGarage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient; 
@Entity
@Table(name="travail")
public class Travail{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="machineId",nullable=false)
	private Machine machine;
	@ManyToOne
	@JoinColumn(name="technicienId",nullable=false)
	private Technicien technicien;
	private LocalDateTime heureDebut;
	private LocalDateTime heureFin;
	@Column(nullable=false)
    private boolean actif=true;
	@Column(name="preuve_photo")
	@JsonIgnore
	private byte[] preuvePhoto;
	@Transient
	private String preuvePhotoBase64;
	@JsonProperty(access=Access.READ_ONLY)
	@Column(precision=5,scale=2)
	private BigDecimal heureTravail;
	@PrePersist
	@PreUpdate
	private void calculerHeureTravail() {
		if(heureDebut!=null && heureFin!=null) {
			long minutes=java.time.Duration.between(heureDebut,heureFin).toMinutes();
			double heure=minutes/60.0;
			this.heureTravail=BigDecimal.valueOf(heure).setScale(2,RoundingMode.HALF_UP);
		}else {
			this.heureTravail=null;
		}
	}
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
	public LocalDateTime getHeureDebut() {
		return heureDebut;
	}
	public void setHeureDebut(LocalDateTime heureDebut) {
		this.heureDebut = heureDebut;
	}
	public LocalDateTime getHeureFin() {
		return heureFin;
	}
	public void setHeureFin(LocalDateTime heureFin) {
		this.heureFin = heureFin;
	}
	public BigDecimal getHeureTravail() {
		return heureTravail;
	}
	public void setHeureTravail(BigDecimal heureTravail) {
		this.heureTravail = heureTravail;
	}
	public byte[] getPreuvePhoto() {
		return preuvePhoto;
	}
	public void setPreuvePhoto(byte[] preuvePhoto) {
		this.preuvePhoto = preuvePhoto;
	}
	public String getPreuvePhotoBase64() {
		return preuvePhotoBase64;
	}
	public void setPreuvePhotoBase64(String preuvePhotoBase64) {
		this.preuvePhotoBase64 = preuvePhotoBase64;
	}
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
}

