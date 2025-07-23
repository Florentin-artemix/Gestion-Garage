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
@RequestMapping("/api/produit")
public class ProduitUtiliserController {

    @Autowired
    ProduitRepository produitRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired 
    InterventionRepository interventionRepository;

    @Autowired
    HistoriqueModificationRepository historiqueRepository;

    @PostMapping
    public String utiliserProduit(@RequestBody ProduitUtiliser produit) {
        Long stockId = produit.getStock().getId();
        Long interventionId = produit.getIntervention().getId();
        double quantiteDemandee = produit.getQuantiteUtiliser();

        Stock stock = stockRepository.findById(stockId).orElse(null);
        Intervention intervention = interventionRepository.findById(interventionId).orElse(null);

        if (stock == null) return "Produit non trouvé dans le stock";
        if (intervention == null) return "Intervention non trouvée";
        if (stock.getQuantite() < quantiteDemandee) return "Quantité insuffisante dans le stock";

        stock.setQuantite(stock.getQuantite() - quantiteDemandee);
        stockRepository.save(stock);

        produit.setStock(stock);
        produit.setIntervention(intervention);
        produit.setQuantiteUtiliser(quantiteDemandee);
        produit.setActif(true);

        produitRepository.save(produit);
        return "Produit utilisé enregistré avec succès";
    }

    @GetMapping
    public List<ProduitDTO> afficherProduit() {
        return produitRepository.findByActifTrue().stream().map(p ->
            new ProduitDTO(
                p.getId(),
                p.getIntervention().getTechnicien().getNom() + " " + p.getIntervention().getTechnicien().getPrenom(),
                p.getIntervention().getTypeIntervention(),
                p.getStock().getNomProduit(),
                p.getQuantiteUtiliser()
            )
        ).toList();
    }
    
    @GetMapping("/desactiver")
    public List<ProduitDTO> afficherProduitDesactiver() {
        return produitRepository.findByActifFalse().stream().map(p ->
            new ProduitDTO(
                p.getId(),
                p.getIntervention().getTechnicien().getNom() + " " + p.getIntervention().getTechnicien().getPrenom(),
                p.getIntervention().getTypeIntervention(),
                p.getStock().getNomProduit(),
                p.getQuantiteUtiliser()
            )
        ).toList();
    }

    @PutMapping("/{id}")
    public String modifierProduit(@PathVariable Long id, @RequestBody ProduitUtiliser produit) {
        return produitRepository.findById(id).map(p -> {
            Stock stock = stockRepository.findById(produit.getStock().getId()).orElse(null);
            Intervention intervention = interventionRepository.findById(produit.getIntervention().getId()).orElse(null);

            if (stock == null) return "Produit non trouvé dans le stock";
            if (intervention == null) return "Intervention non trouvée";

            double ancienneQuantite = p.getQuantiteUtiliser();
            double nouvelleQuantite = produit.getQuantiteUtiliser();
            double ajustement = nouvelleQuantite - ancienneQuantite;

            if (ajustement > 0 && stock.getQuantite() < ajustement)
                return "Quantité insuffisante pour la mise à jour";

            stock.setQuantite(stock.getQuantite() - ajustement);
            stockRepository.save(stock);

            if (!p.getStock().getId().equals(produit.getStock().getId())) {
                enregistrerHistorique("ProduitUtiliser", id, "stockId", String.valueOf(p.getStock().getId()), String.valueOf(produit.getStock().getId()), LocalDateTime.now());
            }

            if (!p.getIntervention().getId().equals(produit.getIntervention().getId())) {
                enregistrerHistorique("ProduitUtiliser", id, "interventionId", String.valueOf(p.getIntervention().getId()), String.valueOf(produit.getIntervention().getId()), LocalDateTime.now());
            }

            if (ancienneQuantite != nouvelleQuantite) {
                enregistrerHistorique("ProduitUtiliser", id, "quantiteUtiliser", String.valueOf(ancienneQuantite), String.valueOf(nouvelleQuantite), LocalDateTime.now());
            }

            p.setStock(stock);
            p.setIntervention(intervention);
            p.setQuantiteUtiliser(nouvelleQuantite);

            produitRepository.save(p);
            return "Produit utilisé modifié avec succès";
        }).orElse("Produit utilisé non trouvé");
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverProduit(@PathVariable Long id) {
        return produitRepository.findById(id).map(p -> {
            if (p.isActif()) {
                enregistrerHistorique("ProduitUtiliser", id, "actif", "true", "false", LocalDateTime.now());
                p.setActif(false);
                produitRepository.save(p);
            }
            return ResponseEntity.ok("Produit utilisé désactivé avec succès");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Historique
    @GetMapping("/historique")
    public List<HistoriqueModification> historiqueGlobal() {
        return historiqueRepository.findByEntite("ProduitUtiliser");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> historiqueParProduit(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("ProduitUtiliser", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ, String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}