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

}
