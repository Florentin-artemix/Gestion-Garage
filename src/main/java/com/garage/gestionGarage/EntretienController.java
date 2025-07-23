package com.garage.gestionGarage;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
@RequestMapping("/api/entretien")
public class EntretienController {

    @Autowired
    EntretienRepository entretienRepository;

    @Autowired
    HistoriqueModificationRepository historiqueRepository;

    @PostMapping
    public String ajouterEntretien(@RequestBody Entretien entretien) {
        entretien.setActif(true);
        entretienRepository.save(entretien);
        return "Entretien enregistré avec succès Type: " + entretien.getTypeEntretien();
    }

    @GetMapping
    public List<EntretienDTO> afficherEntretien() {
        List<Entretien> entretiens = entretienRepository.findByActifTrue();
        List<EntretienDTO> listDto = new ArrayList<>();
        for (Entretien e : entretiens) {
            listDto.add(new EntretienDTO(
                e.getId(),
                e.getTechnicien().getNom(),
                e.getTechnicien().getPrenom(),
                e.getMachine().getType(),
                e.getMachine().getModele(),
                e.getDateEntretien(),
                e.getTypeEntretien(),
                e.getDescription()
            ));
        }
        return listDto;
    }
    
    @GetMapping("/desactiver")
    public List<EntretienDTO> afficherEntretienDesactiver() {
        List<Entretien> entretiens = entretienRepository.findByActifFalse();
        List<EntretienDTO> listDto = new ArrayList<>();
        for (Entretien e : entretiens) {
            listDto.add(new EntretienDTO(
                e.getId(),
                e.getTechnicien().getNom(),
                e.getTechnicien().getPrenom(),
                e.getMachine().getType(),
                e.getMachine().getModele(),
                e.getDateEntretien(),
                e.getTypeEntretien(),
                e.getDescription()
            ));
        }
        return listDto;
    }

    @PutMapping("/{id}")
    public String modifierEntretien(@PathVariable Long id, @RequestBody Entretien entretienModifie) {
        return entretienRepository.findById(id).map(e -> {
            LocalDateTime now = LocalDateTime.now();

            if (!e.getDateEntretien().equals(entretienModifie.getDateEntretien())) {
                enregistrerHistorique("Entretien", id, "dateEntretien",
                        e.getDateEntretien().toString(), entretienModifie.getDateEntretien().toString(), now);
                e.setDateEntretien(entretienModifie.getDateEntretien());
            }

            if (!e.getTypeEntretien().equals(entretienModifie.getTypeEntretien())) {
                enregistrerHistorique("Entretien", id, "typeEntretien",
                        e.getTypeEntretien(), entretienModifie.getTypeEntretien(), now);
                e.setTypeEntretien(entretienModifie.getTypeEntretien());
            }

            if (!e.getDescription().equals(entretienModifie.getDescription())) {
                enregistrerHistorique("Entretien", id, "description",
                        e.getDescription(), entretienModifie.getDescription(), now);
                e.setDescription(entretienModifie.getDescription());
            }

            if (!e.getTechnicien().getId().equals(entretienModifie.getTechnicien().getId())) {
                enregistrerHistorique("Entretien", id, "technicienId",
                        String.valueOf(e.getTechnicien().getId()), String.valueOf(entretienModifie.getTechnicien().getId()), now);
                e.setTechnicien(entretienModifie.getTechnicien());
            }

            if (!e.getMachine().getId().equals(entretienModifie.getMachine().getId())) {
                enregistrerHistorique("Entretien", id, "machineId",
                        String.valueOf(e.getMachine().getId()), String.valueOf(entretienModifie.getMachine().getId()), now);
                e.setMachine(entretienModifie.getMachine());
            }

            entretienRepository.save(e);
            return "Entretien modifié avec succès";
        }).orElse("Entretien non trouvé");
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverEntretien(@PathVariable Long id) {
        return entretienRepository.findById(id).map(e -> {
            if (e.isActif()) {
                enregistrerHistorique("Entretien", id, "actif", "true", "false", LocalDateTime.now());
                e.setActif(false);
                entretienRepository.save(e);
            }
            return ResponseEntity.ok("Entretien désactivé (déclassé) avec succès");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> historiqueGlobal() {
        return historiqueRepository.findByEntite("Entretien");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> historiqueParEntretien(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("Entretien", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ,
                                       String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}