package com.garage.gestionGarage;

import java.time.LocalDateTime;
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
@RequestMapping("/api/intervention")
public class InterventionController {

    @Autowired
    InterventionRepository interventionRepository;

    @Autowired
    ProduitRepository produitRepository;

    @Autowired
    HistoriqueModificationRepository historiqueRepository;

    @GetMapping
    public List<InterventionDTO> afficherIntervention() {
        return interventionRepository.findByActifTrue().stream()
            .filter(i -> i.getTechnicien() != null && i.getMachine() != null)
            .map(i -> new InterventionDTO(
                i.getId(),
                i.getTechnicien().getNom(),
                i.getTechnicien().getPrenom(),
                i.getMachine().getType(),
                i.getMachine().getModele(),
                i.getTypeIntervention(),
                i.getDescription(),
                i.getKmOuHeureMoteur(),
                i.getPhotoIntervention(),
                i.getSignatures(),
                i.isValidationChefGarage(),
                i.getDateIntervention()
            )).toList();
    }
    
    @GetMapping("/desactiver")
    public List<InterventionDTO> afficherInterventionDesactiver() {
        return interventionRepository.findByActifFalse().stream()
            .filter(i -> i.getTechnicien() != null && i.getMachine() != null)
            .map(i -> new InterventionDTO(
                i.getId(),
                i.getTechnicien().getNom(),
                i.getTechnicien().getPrenom(),
                i.getMachine().getType(),
                i.getMachine().getModele(),
                i.getTypeIntervention(),
                i.getDescription(),
                i.getKmOuHeureMoteur(),
                i.getPhotoIntervention(),
                i.getSignatures(),
                i.isValidationChefGarage(),
                i.getDateIntervention()
            )).toList();
    }

    @PostMapping
    public String ajouterIntervention(@RequestBody Intervention intervention) {
        intervention.setActif(true);
        interventionRepository.save(intervention);
        return "Intervention enregistrée avec succès";
    }

    @PutMapping("/{id}")
    public String modifierIntervention(@PathVariable Long id, @RequestBody Intervention interModifiee) {
        return interventionRepository.findById(id).map(i -> {
            LocalDateTime now = LocalDateTime.now();

            if (!i.getMachine().getId().equals(interModifiee.getMachine().getId())) {
                enregistrerHistorique("Intervention", id, "machineId",
                        String.valueOf(i.getMachine().getId()), String.valueOf(interModifiee.getMachine().getId()), now);
                i.setMachine(interModifiee.getMachine());
            }

            if (!i.getTechnicien().getId().equals(interModifiee.getTechnicien().getId())) {
                enregistrerHistorique("Intervention", id, "technicienId",
                        String.valueOf(i.getTechnicien().getId()), String.valueOf(interModifiee.getTechnicien().getId()), now);
                i.setTechnicien(interModifiee.getTechnicien());
            }

            if (!i.getDescription().equals(interModifiee.getDescription())) {
                enregistrerHistorique("Intervention", id, "description",
                        i.getDescription(), interModifiee.getDescription(), now);
                i.setDescription(interModifiee.getDescription());
            }

            if (i.getKmOuHeureMoteur() != interModifiee.getKmOuHeureMoteur()) {
                enregistrerHistorique("Intervention", id, "kmOuHeureMoteur",
                        String.valueOf(i.getKmOuHeureMoteur()), String.valueOf(interModifiee.getKmOuHeureMoteur()), now);
                i.setKmOuHeureMoteur(interModifiee.getKmOuHeureMoteur());
            }

            if (!i.getDateIntervention().equals(interModifiee.getDateIntervention())) {
                enregistrerHistorique("Intervention", id, "dateIntervention",
                        i.getDateIntervention().toString(), interModifiee.getDateIntervention().toString(), now);
                i.setDateIntervention(interModifiee.getDateIntervention());
            }

            if (i.isValidationChefGarage() != interModifiee.isValidationChefGarage()) {
                enregistrerHistorique("Intervention", id, "validationChefGarage",
                        String.valueOf(i.isValidationChefGarage()), String.valueOf(interModifiee.isValidationChefGarage()), now);
                i.setValidationChefGarage(interModifiee.isValidationChefGarage());
            }
            i.setPhotoIntervention(interModifiee.getPhotoIntervention());
            i.setSignatures(interModifiee.getSignatures());

            interventionRepository.save(i);
            return "Intervention modifiée avec succès";
        }).orElse("Intervention non trouvée");
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<String> validerIntervention(@PathVariable Long id) {
        return interventionRepository.findById(id).map(intervention -> {
            if (!intervention.isValidationChefGarage()) {
                enregistrerHistorique("Intervention", id, "validationChefGarage",
                        "false", "true", LocalDateTime.now());
            }
            intervention.setValidationChefGarage(true);
            interventionRepository.save(intervention);
            return ResponseEntity.ok("Intervention validée par le chef de garage");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverIntervention(@PathVariable Long id) {
        return interventionRepository.findById(id).map(i -> {
            if (i.isActif()) {
                enregistrerHistorique("Intervention", id, "actif", "true", "false", LocalDateTime.now());
                i.setActif(false);
                interventionRepository.save(i);
            }
            return ResponseEntity.ok("Intervention désactivée avec succès");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> historiqueGlobal() {
        return historiqueRepository.findByEntite("Intervention");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> historiqueParId(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("Intervention", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ,
                                       String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}