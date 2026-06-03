package pharmacie.service;

import org.springframework.stereotype.Service;
import pharmacie.dao.MedicamentRepository;
import pharmacie.entity.Fournisseur;
import pharmacie.entity.Medicament;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovisionnementService {

    private final MedicamentRepository medicamentRepository;
    private final EmailService emailService;

    public ApprovisionnementService(MedicamentRepository medicamentRepository,
                                    EmailService emailService) {
        this.medicamentRepository = medicamentRepository;
        this.emailService = emailService;
    }

    public List<String> lancerApprovisionnement() {

        List<Medicament> aReappro = medicamentRepository.findAll()
            .stream()
            .filter(m -> m.getUnitesEnStock() < m.getNiveauDeReappro())
            .toList();

        Map<Fournisseur, List<Medicament>> map = new HashMap<>();

        for (Medicament m : aReappro) {
            for (Fournisseur f : m.getCategorie().getFournisseurs()) {
                map.computeIfAbsent(f, k -> new ArrayList<>()).add(m);
            }
        }

        List<String> fournisseursContactes = new ArrayList<>();

        for (Map.Entry<Fournisseur, List<Medicament>> entry : map.entrySet()) {

            Fournisseur f = entry.getKey();
            List<Medicament> meds = entry.getValue();

            String message = construireMessage(meds);

            // envoi du mail
            emailService.sendEmail(f.getEmail(), "Demande de devis", message);

            fournisseursContactes.add(f.getNom());
        }

        return fournisseursContactes;
    }

    private String construireMessage(List<Medicament> meds) {

        Map<String, List<Medicament>> parCategorie =
            meds.stream().collect(Collectors.groupingBy(
                m -> m.getCategorie().getLibelle()
            ));

        StringBuilder sb = new StringBuilder();

        for (String cat : parCategorie.keySet()) {
            sb.append("Catégorie : ").append(cat).append("\n");

            for (Medicament m : parCategorie.get(cat)) {
                sb.append("- ").append(m.getNom()).append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}
