package com.garage.gestionGarage;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Entretien {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate dateEntretien;
	@Column(columnDefinition="TEXT")
	private String description;
	private String typeEntretien;
	@ManyToOne
	@JoinColumn(name = "machineId", nullable = false)
	private Machine machine;
	@ManyToOne
	@JoinColumn(name = "technicienId", nullable = false)
	private Technicien technicien;
	@Column(nullable=false)
    private boolean actif=true;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getDateEntretien() {
		return dateEntretien;
	}
	public void setDateEntretien(LocalDate date) {
		this.dateEntretien = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTypeEntretien() {
		return typeEntretien;
	}
	public void setTypeEntretien(String typeEntretien) {
		this.typeEntretien = typeEntretien;
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
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
    
}
