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
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    HistoriqueModificationRepository historiqueRepository;

    @PostMapping
    public String ajouterStock(@RequestBody Stock stock) {
        stock.setActif(true);
        stockRepository.save(stock);
        return "Stock ajouté avec succès";
    }

    @GetMapping
    public List<Stock> afficherStock() {
        return stockRepository.findByActifTrue();
    }

    @GetMapping("/desactiver")
    public List<Stock> afficherStocksDesactives() {
        return stockRepository.findByActifFalse();
    }

    @PutMapping("/{id}")
    public String modifierStock(@PathVariable Long id, @RequestBody Stock stockModifie) {
        return stockRepository.findById(id).map(stock -> {
            LocalDateTime now = LocalDateTime.now();

            if (!stock.getNomProduit().equals(stockModifie.getNomProduit())) {
                enregistrerHistorique("Stock", id, "nomProduit", stock.getNomProduit(), stockModifie.getNomProduit(), now);
                stock.setNomProduit(stockModifie.getNomProduit());
            }

            if (!stock.getFournisseur().equals(stockModifie.getFournisseur())) {
                enregistrerHistorique("Stock", id, "fournisseur", stock.getFournisseur(), stockModifie.getFournisseur(), now);
                stock.setFournisseur(stockModifie.getFournisseur());
            }

            if (stock.getQuantite() != stockModifie.getQuantite()) {
                enregistrerHistorique("Stock", id, "quantite", String.valueOf(stock.getQuantite()), String.valueOf(stockModifie.getQuantite()), now);
                stock.setQuantite(stockModifie.getQuantite());
            }

            if (stock.getPrixUnitaire() != stockModifie.getPrixUnitaire()) {
                enregistrerHistorique("Stock", id, "prixUnitaire", String.valueOf(stock.getPrixUnitaire()), String.valueOf(stockModifie.getPrixUnitaire()), now);
                stock.setPrixUnitaire(stockModifie.getPrixUnitaire());
            }

            if (stock.getSeuilAlerte() != stockModifie.getSeuilAlerte()) {
                enregistrerHistorique("Stock", id, "seuilAlerte", String.valueOf(stock.getSeuilAlerte()), String.valueOf(stockModifie.getSeuilAlerte()), now);
                stock.setSeuilAlerte(stockModifie.getSeuilAlerte());
            }

            stockRepository.save(stock);
            return "Stock modifié avec succès";
        }).orElse("Stock non trouvé");
    }

    @DeleteMapping("/{id}/desactiver")
    public ResponseEntity<String> desactiverStock(@PathVariable Long id) {
        return stockRepository.findById(id).map(stock -> {
            if (stock.isActif()) {
                enregistrerHistorique("Stock", id, "actif", "true", "false", LocalDateTime.now());
                stock.setActif(false);
                stockRepository.save(stock);
            }
            return ResponseEntity.ok("Stock désactivé avec succès");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historique")
    public List<HistoriqueModification> historiqueGlobalStock() {
        return historiqueRepository.findByEntite("Stock");
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueModification> historiqueParStock(@PathVariable Long id) {
        return historiqueRepository.findByEntiteAndEntiteId("Stock", id);
    }

    private void enregistrerHistorique(String entite, Long entiteId, String champ, String ancienne, String nouvelle, LocalDateTime date) {
        HistoriqueModification h = new HistoriqueModification(entite, entiteId, champ, ancienne, nouvelle, date);
        historiqueRepository.save(h);
    }
}