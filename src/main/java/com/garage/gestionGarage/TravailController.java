package com.garage.gestionGarage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travail")
public class TravailController {

    @Autowired
    TravailRepository travailRepository;

    @Autowired
    HistoriqueModificationRepository historiqueRepository;

    @PostMapping
    public String ajouterTravail(@RequestBody Travail travail) {
        travail.setActif(true);
        if (travail.getPreuvePhotoBase64() != null) {
            String propre = travail.getPreuvePhotoBase64().replaceAll("\\s+", "");
            byte[] imagesBytes = Base64.getDecoder().decode(propre);
            travail.setPreuvePhoto(imagesBytes);
        }
        travailRepository.save(travail);
        return "Travail Enregistré avec succès";
    }

    @GetMapping
    public List<TravailDTO> afficherTravail() {
        List<Travail> travaux = travailRepository.findByActifTrue(); // ❗ seulement les actifs
        List<TravailDTO> dtoList = new ArrayList<>();
        for (Travail t : travaux) {
            Long id = t.getId();
            String nomTech = t.getTechnicien().getNom();
            String prenomTech = t.getTechnicien().getPrenom();
            String typeMachine = t.getMachine().getType();
            String modeleMachine = t.getMachine().getModele();
            String heureDebut = t.getHeureDebut() != null ? t.getHeureDebut().toString() : null;
            String heureFin = t.getHeureFin() != null ? t.getHeureFin().toString() : null;
            String heureTravail = t.getHeureTravail() != null ? t.getHeureTravail().toString() : null;
            String preuve64 = t.getPreuvePhoto() != null ? Base64.getEncoder().encodeToString(t.getPreuvePhoto()) : null;
            dtoList.add(new TravailDTO(id, nomTech, prenomTech, typeMachine, modeleMachine, heureDebut, heureFin, heureTravail, preuve64));
        }
        return dtoList;
    }
    
    @GetMapping("/desactiver")
    public List<TravailDTO> afficherTravailDesactiver() {
        List<Travail> travaux = travailRepository.findByActifFalse();
        List<TravailDTO> dtoList = new ArrayList<>();
        for (Travail t : travaux) {
            Long id = t.getId();
            String nomTech = t.getTechnicien().getNom();
            String prenomTech = t.getTechnicien().getPrenom();
            String typeMachine = t.getMachine().getType();
            String modeleMachine = t.getMachine().getModele();
            String heureDebut = t.getHeureDebut() != null ? t.getHeureDebut().toString() : null;
            String heureFin = t.getHeureFin() != null ? t.getHeureFin().toString() : null;
            String heureTravail = t.getHeureTravail() != null ? t.getHeureTravail().toString() : null;
            String preuve64 = t.getPreuvePhoto() != null ? Base64.getEncoder().encodeToString(t.getPreuvePhoto()) : null;
            dtoList.add(new TravailDTO(id, nomTech, prenomTech, typeMachine, modeleMachine, heureDebut, heureFin, heureTravail, preuve64));
        }
        return dtoList;
    }

    @PutMapping("/{id}")
    public String modifierTravail(@PathVariable Long id, @RequestBody Travail travailModifie) {
        return travailRepository.findById(id).map(t -> {
            LocalDateTime now = LocalDateTime.now();

            if (!t.getMachine().getId().equals(travailModifie.getMachine().getId())) {
                enregistrerHistorique("Travail", id, "machineId", String.valueOf(t.getMachine().getId()), String.valueOf(travailModifie.getMachine().getId()), now);
                t.setMachine(travailModifie.getMachine());
            }

            if (!t.getTechnicien().getId().equals(travailModifie.getTechnicien().getId())) {
                enregistrerHistorique("Travail", id, "technicienId", String.valueOf(t.getTechnicien().getId()), String.valueOf(travailModifie.getTechnicien().getId()), now);
                t.setTechnicien(travailModifie.getTechnicien());
            }

            if (!t.getHeureDebut().equals(travailModifie.getHeureDebut())) {
                enregistrerHistorique("Travail", id, "heureDebut", t.getHeureDebut().toString(), travailModifie.getHeureDebut().toString(), now);
                t.setHeureDebut(travailModifie.getHeureDebut());
            }

            if (!t.getHeureFin().equals(travailModifie.getHeureFin())) {
                enregistrerHistorique("Travail", id, "heureFin", t.getHeureFin().toString(), travailModifie.getHeureFin().toString(), now);
                t.setHeureFin(travailModifie.getHeureFin());
            }

            if (travailModifie.getPreuvePhotoBase64() != null) {
                String propre = travailModifie.getPreuvePhotoBase64().replaceAll("\\s+", "");
                byte[] imagesBytes = Base64.getDecoder().decode(propre);
                t.setPreuvePhoto(imagesBytes);
                // Pas d’historique pour les images
            }

            travailRepository.save(t);
            return "Travail modifié avec succès";
        }).orElse("Travail non trouvé");
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverTravail(@PathVariable Long id) {
        return travailRepository.findById(id).map(t -> {
            if (t.isActif()) {
                enregistrerHistorique("Travail", id, "actif", "true", "false", LocalDateTime.now());
                t.setActif(false);
                travailRepository.save(t);
            }
            return ResponseEntity.ok("Travail désactivé avec succès");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> historiqueGlobalTravaux() {
        return historiqueRepository.findByEntite("Travail");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> historiqueParTravail(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("Travail", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ, String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}