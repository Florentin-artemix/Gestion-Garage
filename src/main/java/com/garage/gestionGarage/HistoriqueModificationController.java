package com.garage.gestionGarage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historique")
public class HistoriqueModificationController {

    @Autowired
    private HistoriqueModificationRepository historiqueRepository;

    @Autowired
    private TechnicienRepository technicienRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private StockRepository stockRepository;

    @GetMapping
    public List<HistoriqueDTO> afficherHistoriqueLisible() {
        return historiqueRepository.findAll().stream()
                .map(h -> new HistoriqueDTO(
                        h.getEntite(),
                        h.getEntiteId(),
                        h.getChampModifie(),
                        traduireValeurLisible(h.getChampModifie(), h.getAncienneValeur()),
                        traduireValeurLisible(h.getChampModifie(), h.getNouvelleValeur()),
                        h.getDateModification()
                ))
                .collect(Collectors.toList());
    }

    // Méthode de traduction lisible des IDs en noms
    private String traduireValeurLisible(String champ, String valeur) {
        if (champ.equalsIgnoreCase("technicien") || champ.equalsIgnoreCase("technicienId")) {
            try {
                Long id = Long.parseLong(valeur);
                return technicienRepository.findById(id)
                        .map(t -> t.getNom() + " " + t.getPrenom())
                        .orElse("Inconnu(ID=" + valeur + ")");
            } catch (Exception e) {
                return valeur;
            }
        }

        if (champ.equalsIgnoreCase("machine") || champ.equalsIgnoreCase("machineId")) {
            try {
                Long id = Long.parseLong(valeur);
                return machineRepository.findById(id)
                        .map(m -> m.getType() + " " + m.getModele())
                        .orElse("Inconnu(ID=" + valeur + ")");
            } catch (Exception e) {
                return valeur;
            }
        }

        if (champ.equalsIgnoreCase("produit") || champ.equalsIgnoreCase("stockId") || champ.equalsIgnoreCase("nom produit")) {
            try {
                Long id = Long.parseLong(valeur);
                return stockRepository.findById(id)
                        .map(s -> s.getNomProduit())
                        .orElse("Inconnu(ID=" + valeur + ")");
            } catch (Exception e) {
                return valeur;
            }
        }

        return valeur;
    }
}