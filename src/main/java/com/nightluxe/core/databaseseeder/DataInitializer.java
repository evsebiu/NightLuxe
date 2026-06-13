package com.nightluxe.core.databaseseeder;

import com.nightluxe.core.entity.Category;
import com.nightluxe.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Dacă baza de date este goală, populăm categoriile de bază pentru MVP
        if (categoryRepository.count() == 0) {

            // 1. Categoria principală Escorte (Generatoare de venit - requiresCredit = true)
            Category escorte = new Category();
            escorte.setName("Escorte");
            escorte.setSlug("escorte-malta");
            escorte.setRequiresCredit(true);
            categoryRepository.save(escorte);

            // 2. Subcategorie pentru Escorte
            Category escorteIndependenti = new Category();
            escorteIndependenti.setName("Independenți");
            escorteIndependenti.setSlug("escorte-independente");
            escorteIndependenti.setRequiresCredit(true);
            escorteIndependenti.setParentCategory(escorte);
            categoryRepository.save(escorteIndependenti);

            // 3. Categoria principală Auto (Anunț generalist - requiresCredit = false)
            Category auto = new Category();
            auto.setName("Auto");
            auto.setSlug("auto-malta");
            auto.setRequiresCredit(false);
            categoryRepository.save(auto);

            // 4. Categoria principală Imobiliare
            Category imobiliare = new Category();
            imobiliare.setName("Imobiliare");
            imobiliare.setSlug("imobiliare-malta");
            imobiliare.setRequiresCredit(false);
            categoryRepository.save(imobiliare);

            System.out.println(">> Baza de date a fost populată cu categoriile inițiale NightLuxe.");
        }
    }
}