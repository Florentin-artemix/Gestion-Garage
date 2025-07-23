package com.garage.gestionGarage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/machine")
public class MachineController {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private HistoriqueModificationRepository historiqueRepository;

    @PostMapping
    public String ajouterMachine(@RequestBody Machine machine) {
        machineRepository.save(machine);
        return "Machine Enregistrer avec succes: " + machine.getModele();
    }

    @GetMapping
    public List<Machine> getMachine() {
        return machineRepository.findByStatutActuelNot(Statut.DECLASSEE);
    }

    @GetMapping("/declassees")
    public List<Machine> getMachineDeclassee() {
        return machineRepository.findByStatutActuel(Statut.DECLASSEE);
    }

    @PutMapping("/{id}")
    public String modifierMachine(@PathVariable Long id, @RequestBody Machine machineModifier) {
        return machineRepository.findById(id).map(machine -> {

            LocalDateTime now = LocalDateTime.now();

            if (!machine.getType().equals(machineModifier.getType())) {
                enregistrerHistorique("Machine", machine.getId(), "type", machine.getType(), machineModifier.getType(), now);
                machine.setType(machineModifier.getType());
            }

            if (!machine.getModele().equals(machineModifier.getModele())) {
                enregistrerHistorique("Machine", machine.getId(), "modele", machine.getModele(), machineModifier.getModele(), now);
                machine.setModele(machineModifier.getModele());
            }

            if (!machine.getNumeroImmatriculation().equals(machineModifier.getNumeroImmatriculation())) {
                enregistrerHistorique("Machine", machine.getId(), "numeroImmatriculation", machine.getNumeroImmatriculation(), machineModifier.getNumeroImmatriculation(), now);
                machine.setNumeroImmatriculation(machineModifier.getNumeroImmatriculation());
            }

            if (!machine.getDateMiseEnService().equals(machineModifier.getDateMiseEnService())) {
                enregistrerHistorique("Machine", machine.getId(), "dateMiseEnService", machine.getDateMiseEnService().toString(), machineModifier.getDateMiseEnService().toString(), now);
                machine.setDateMiseEnService(machineModifier.getDateMiseEnService());
            }

            if (machine.getHeureServiceMoteur() != machineModifier.getHeureServiceMoteur()) {
                enregistrerHistorique("Machine", machine.getId(), "heureServiceMoteur", String.valueOf(machine.getHeureServiceMoteur()), String.valueOf(machineModifier.getHeureServiceMoteur()), now);
                machine.setHeureServiceMoteur(machineModifier.getHeureServiceMoteur());
            }

            if (!machine.getDateProchainVidange().equals(machineModifier.getDateProchainVidange())) {
                enregistrerHistorique("Machine", machine.getId(), "dateProchainVidange", machine.getDateProchainVidange().toString(), machineModifier.getDateProchainVidange().toString(), now);
                machine.setdateProchainVidange(machineModifier.getDateProchainVidange());
            }

            if (!machine.getStatutActuel().equals(machineModifier.getStatutActuel())) {
                enregistrerHistorique("Machine", machine.getId(), "statutActuel", machine.getStatutActuel().name(), machineModifier.getStatutActuel().name(), now);
                machine.setStatutActuel(machineModifier.getStatutActuel());
            }

            machineRepository.save(machine);
            return "Machine Modifier avec succès.";
        }).orElse("Machine non Trouvée");
    }

    @DeleteMapping("/{id}/declasser")
    public ResponseEntity<String> declasserMachine(@PathVariable Long id) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if (optionalMachine.isEmpty()) return ResponseEntity.notFound().build();

        Machine machine = optionalMachine.get();

        if (!machine.getStatutActuel().equals(Statut.DECLASSEE)) {
            enregistrerHistorique("Machine", machine.getId(), "statutActuel", machine.getStatutActuel().name(), Statut.DECLASSEE.name(), LocalDateTime.now());
            machine.setStatutActuel(Statut.DECLASSEE);
            machineRepository.save(machine);
        }

        return ResponseEntity.ok("Machine declassée avec succès");
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ, String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> getTousLesHistoriquesMachines() {
        return historiqueRepository.findByEntite("Machine");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> getHistoriqueParMachine(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("Machine", id);
    }
}