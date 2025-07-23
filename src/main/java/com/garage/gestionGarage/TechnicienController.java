package com.garage.gestionGarage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/technicien")
public class TechnicienController {

    @Autowired
    private TechnicienRepository technicienRepository;

    @Autowired
    private HistoriqueModificationRepository historiqueRepository;

    @PostMapping
    public String ajouterTechnicien(@RequestBody Technicien technicien) {
        technicien.setActif(true); // Par défaut actif
        technicienRepository.save(technicien);
        return "Technicien enregistré avec succès";
    }

    @GetMapping
    public List<Technicien> getAllTechniciensActifs() {
        return technicienRepository.findByActifTrue();
    }

    @GetMapping("/desactiver")
    public List<Technicien> getTechniciensDesactives() {
        return technicienRepository.findByActifFalse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technicien> getById(@PathVariable Long id) {
        Optional<Technicien> optional = technicienRepository.findById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public String modifierTechnicien(@PathVariable Long id, @RequestBody Technicien technicienModifie) {
        return technicienRepository.findById(id).map(technicien -> {

            LocalDateTime now = LocalDateTime.now();

            if (!technicien.getNom().equals(technicienModifie.getNom())) {
                enregistrerHistorique("Technicien", id, "nom", technicien.getNom(), technicienModifie.getNom(), now);
                technicien.setNom(technicienModifie.getNom());
            }

            if (!technicien.getPrenom().equals(technicienModifie.getPrenom())) {
                enregistrerHistorique("Technicien", id, "prenom", technicien.getPrenom(), technicienModifie.getPrenom(), now);
                technicien.setPrenom(technicienModifie.getPrenom());
            }

            if (technicien.getAge() != technicienModifie.getAge()) {
                enregistrerHistorique("Technicien", id, "age", String.valueOf(technicien.getAge()), String.valueOf(technicienModifie.getAge()), now);
                technicien.setAge(technicienModifie.getAge());
            }

            if (!technicien.getRole().equals(technicienModifie.getRole())) {
                enregistrerHistorique("Technicien", id, "role", technicien.getRole(), technicienModifie.getRole(), now);
                technicien.setRole(technicienModifie.getRole());
            }

            technicienRepository.save(technicien);
            return "Technicien modifié avec succès.";
        }).orElse("Technicien non trouvé.");
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverTechnicien(@PathVariable Long id) {
        Optional<Technicien> optional = technicienRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Technicien technicien = optional.get();

        if (technicien.isActif()) {
            enregistrerHistorique("Technicien", technicien.getId(), "actif", "true", "false", LocalDateTime.now());
            technicien.setActif(false);
            technicienRepository.save(technicien);
        }

        return ResponseEntity.ok("Technicien désactivé (déclassé) avec succès.");
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> getHistoriqueGlobalTechniciens() {
        return historiqueRepository.findByEntite("Technicien");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> getHistoriqueParTechnicien(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("Technicien", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ, String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}