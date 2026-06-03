package pharmacie.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pharmacie.dao.MedicamentRepository;
import pharmacie.dao.UnitesParMedicament;

@Slf4j
@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    private final MedicamentRepository medicamentDao;

    public StatsRestController(MedicamentRepository medicamentDao) {
        this.medicamentDao = medicamentDao;
    }

    /**
     * Retourne une liste JSON :
     * [{"nom": "...", "unites": N}, ...]
     */
    @GetMapping("/unitesCommandeesPourCategorie/{code}")
    public List<UnitesParMedicament> unitesCommandeesPourCategorie(@PathVariable Integer code) {
        log.info("Stats : unitesCommandeesPourCategorie({})", code);
        return medicamentDao.unitesCommandeesPourCategorie(code);
    }

    /**
     * Format tableau :
     * [[nom, unites], ...]
     */
    @GetMapping("/unitesCommandeesPourCategorieV2/{code}")
    public List<Object[]> unitesCommandeesPourCategorieV2(@PathVariable Integer code) {
        log.info("Stats : unitesCommandeesPourCategorieV2({})", code);
        return medicamentDao.unitesCommandeesPourCategorieV2(code);
    }
}
