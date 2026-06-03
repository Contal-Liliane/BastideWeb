package pharmacie.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacie.service.ApprovisionnementService;

import java.util.List;

@RestController
@RequestMapping("/api/approvisionnement")
public class ApprovisionnementController {

    private final ApprovisionnementService service;

    public ApprovisionnementController(ApprovisionnementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> lancer() {

        try {
            List<String> fournisseurs = service.lancerApprovisionnement();
            return ResponseEntity.ok(fournisseurs);

        } catch (Exception e) {
            return ResponseEntity
                .status(500)
                .body("Erreur lors de l'approvisionnement : " + e.getMessage());
        }
    }
}
