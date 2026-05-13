package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pharmacie.entity.Medicament;

// Cette interface sera auto-implémentée par Spring

public interface MedicamentRepository extends JpaRepository<Medicament, Integer> {

    @Query("""
       SELECT m from Medicament m
       WHERE m.indisponible = false
       AND m.unitesEnStock > m.unitesCommandees
     """)
    List<Medicament> medicamentsDisponibles();

    @Query("""
        SELECT m.nom AS nom, SUM(l.quantite) AS unites
        FROM Ligne l JOIN l.medicament m
        WHERE m.categorie.code = :code
        GROUP BY m.nom
        ORDER BY SUM(l.quantite) DESC
    """)
    List<UnitesParMedicament> unitesCommandeesPourCategorie(Integer code);

    /**
     * Même statistique que {@link #unitesCommandeesPourCategorie(Integer)},
     * mais renvoyée sous forme de tableaux [nom, unites] directement
     * consommable par {@code google.visualization.arrayToDataTable}.
     *
     * @param code la clé de la catégorie
     * @return la liste [[nom, unites], ...] triée par unités décroissantes
     */
    @Query("""
        SELECT m.nom, SUM(l.quantite)
        FROM Ligne l JOIN l.medicament m
        WHERE m.categorie.code = :code
        GROUP BY m.nom
        ORDER BY SUM(l.quantite) DESC
    """)
    List<Object[]> unitesCommandeesPourCategorieV2(Integer code);

}
