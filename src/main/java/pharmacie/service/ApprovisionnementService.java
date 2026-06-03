package pharmacie.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacie.dao.MedicamentRepository;
import pharmacie.entity.Fournisseur;
import pharmacie.entity.Medicament;
import pharmacie.repository.FournisseurRepository;

@Service
public class ApprovisionnementService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private EmailService emailService;

    public List<String> lancerApprovisionnement() {

        // 1. Médicaments à réapprovisionner
        List<Medicament> aReappro = medicamentRepository.findAll()
            .stream()
            .filter(m -> m.getUnitesEnStock() < m.getNiveauDeReappro())
            .toList();

        // 2. Map fournisseur -> médicaments
        Map<Fournisseur, List<Medicament>> map = new HashMap<>();

        for (Medicament m : aReappro) {
            for (Fournisseur f : m.getCategorie().getFournisseurs()) {
                map.computeIfAbsent(f, k -> new ArrayList<>()).add(m);
            }
        }

        // 3. Envoi des mails
        List<String> fournisseursContactes = new ArrayList<>();

        for (Map.Entry<Fournisseur, List<Medicament>> entry : map.entrySet()) {
            Fournisseur f = entry.getKey();
            List<Medicament> meds = entry.getValue();

            String message = construireMessage(meds);

            emailService.sendEmail(f.getEmail(), "Demande de devis", message);

            fournisseursContactes.add(f.getNom());
        }

        return fournisseursContactes;
    }

    private String construireMessage(List<Medicament> meds) {
        StringBuilder sb = new StringBuilder();

        Map<String, List<Medicament>> parCategorie =
            meds.stream().collect(Collectors.groupingBy(m -> m.getCategorie().getLibelle()));

        for (String cat : parCategorie.keySet()) {
            sb.append("Catégorie : ").append(cat).append("\n");
            for (Medicament m : parCategorie.get(cat)) {
                sb.append("- ").append(m.getNom()).append("\n");
            }
        }

        return sb.toString();
    }
}
