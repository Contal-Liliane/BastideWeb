package pharmacie.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pharmacie.service.ApprovisionnementService;

import java.util.List;

@RestController
@RequestMapping("/approvisionnement")
public class ApprovisionnementController {

    @Autowired
    private ApprovisionnementService service;

    @PostMapping
    public ResponseEntity<?> lancer() {
        try {
            List<String> fournisseurs = service.lancerApprovisionnement();
            return ResponseEntity.ok(fournisseurs);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur : " + e.getMessage());
        }
    }
}
