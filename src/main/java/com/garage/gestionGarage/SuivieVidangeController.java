package com.garage.gestionGarage;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suivieVidange")
public class SuivieVidangeController {

    @Autowired
    private SuivieVidangeRepository suivieRepo;

    @Autowired
    private MachineRepository machineRepo;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private HistoriqueModificationRepository historiqueRepository;

    @GetMapping
    public List<SuivieVidangeDTO> getAll() {
        return suivieRepo.findByActifTrue().stream().map(s ->
                new SuivieVidangeDTO(
                        s.getId(),
                        s.getMachine().getType(),
                        s.getMachine().getModele(),
                        s.getDernierSceanceKm(),
                        s.getFrequence(),
                        s.getProchainSceanceKm(),
                        s.getStock().getNomProduit(),
                        s.getQuantiteUtiliser(),
                        s.isAlerteVidange()
                )
        ).toList();
    }

    @GetMapping("/desactiver")
    public List<SuivieVidangeDTO> getAllDesactiver() {
        return suivieRepo.findByActifFalse().stream().map(s ->
                new SuivieVidangeDTO(
                        s.getId(),
                        s.getMachine().getType(),
                        s.getMachine().getModele(),
                        s.getDernierSceanceKm(),
                        s.getFrequence(),
                        s.getProchainSceanceKm(),
                        s.getStock().getNomProduit(),
                        s.getQuantiteUtiliser(),
                        s.isAlerteVidange()
                )
        ).toList();
    }

    @PostMapping
    public String addSuivie(@RequestBody SuivieVidange s) {
        Machine machine = machineRepo.findById(s.getMachine().getId()).orElse(null);
        Stock stock = stockRepo.findById(s.getStock().getId()).orElse(null);

        if (machine == null) return "Machine non trouvée";
        if (stock == null) return "Produit non trouvé";

        double disponible = stock.getQuantite();
        double aUtiliser = s.getQuantiteUtiliser();
        if (aUtiliser > disponible) return "Quantité insuffisante en stock";

        stock.setQuantite(disponible - aUtiliser);
        stockRepo.save(stock);

        s.setMachine(machine);
        s.setStock(stock);
        s.setProchainSceanceKm(s.getDernierSceanceKm() + s.getFrequence());
        s.setAlerteVidange(false);
        s.setActif(true);

        suivieRepo.save(s);
        return "Suivi vidange enregistré avec mise à jour du stock";
    }

    @PutMapping("/{id}")
    public String updateSuivie(@PathVariable Long id, @RequestBody SuivieVidange s) {
        return suivieRepo.findById(id).map(existing -> {
            LocalDateTime now = LocalDateTime.now();

            Machine newMachine = machineRepo.findById(s.getMachine().getId()).orElse(null);
            Stock newStock = stockRepo.findById(s.getStock().getId()).orElse(null);

            if (newMachine == null) return "Machine non trouvée";
            if (newStock == null) return "Produit non trouvé";

            // Historique Machine
            if (!existing.getMachine().getId().equals(newMachine.getId())) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Machine",
                        String.valueOf(existing.getMachine().getId()), String.valueOf(newMachine.getId()), now);
            }

            // Historique Stock
            if (!existing.getStock().getId().equals(newStock.getId())) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Produit",
                        String.valueOf(existing.getStock().getId()), String.valueOf(newStock.getId()), now);
            }

            // Historique Dernier Sceance
            if (existing.getDernierSceanceKm() != s.getDernierSceanceKm()) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Dernier Sceance KM",
                        String.valueOf(existing.getDernierSceanceKm()), String.valueOf(s.getDernierSceanceKm()), now);
            }

            // Historique Fréquence
            if (existing.getFrequence() != s.getFrequence()) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Fréquence",
                        String.valueOf(existing.getFrequence()), String.valueOf(s.getFrequence()), now);
            }

            // Historique Prochain Sceance
            int nouveauProchainKm = s.getDernierSceanceKm() + s.getFrequence();
            if (existing.getProchainSceanceKm() != nouveauProchainKm) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Prochain Sceance KM",
                        String.valueOf(existing.getProchainSceanceKm()), String.valueOf(nouveauProchainKm), now);
            }

            // Historique Quantité utilisée
            double ancienneQte = existing.getQuantiteUtiliser();
            double nouvelleQte = s.getQuantiteUtiliser();
            if (ancienneQte != nouvelleQte) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Quantité Utilisée",
                        String.valueOf(ancienneQte), String.valueOf(nouvelleQte), now);
            }

            // Historique alerte
            if (existing.isAlerteVidange() != s.isAlerteVidange()) {
                enregistrerHistorique("SuivieVidange", existing.getId(), "Alerte Vidange",
                        String.valueOf(existing.isAlerteVidange()), String.valueOf(s.isAlerteVidange()), now);
            }

            // Mise à jour du stock
            double difference = nouvelleQte - ancienneQte;
            if (difference > 0 && difference > newStock.getQuantite())
                return "Pas assez de stock pour augmenter la quantité utilisée";
            newStock.setQuantite(newStock.getQuantite() - difference);
            stockRepo.save(newStock);

            // Enregistrement final
            existing.setMachine(newMachine);
            existing.setStock(newStock);
            existing.setDernierSceanceKm(s.getDernierSceanceKm());
            existing.setFrequence(s.getFrequence());
            existing.setQuantiteUtiliser(nouvelleQte);
            existing.setProchainSceanceKm(nouveauProchainKm);
            existing.setAlerteVidange(s.isAlerteVidange());

            suivieRepo.save(existing);
            return "Suivi vidange modifié et stock ajusté";
        }).orElse("Suivi introuvable");
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverSuivie(@PathVariable Long id) {
        return suivieRepo.findById(id).map(suivie -> {
            if (suivie.isActif()) {
                enregistrerHistorique("SuivieVidange", id, "actif", "true", "false", LocalDateTime.now());
                suivie.setActif(false);
                suivieRepo.save(suivie);
            }
            return ResponseEntity.ok("Suivi vidange désactivé avec succès");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> historiqueGlobalSuivieVidange() {
        return historiqueRepository.findByEntite("SuivieVidange");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> historiqueParSuivie(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("SuivieVidange", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ, String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}