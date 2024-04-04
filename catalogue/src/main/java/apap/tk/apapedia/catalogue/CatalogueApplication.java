package apap.tk.apapedia.catalogue;

import apap.tk.apapedia.catalogue.model.Catalogue;
import apap.tk.apapedia.catalogue.model.Category;
import apap.tk.apapedia.catalogue.repository.CatalogueRepo;
import apap.tk.apapedia.catalogue.repository.CategoryRepo;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.*;

@SpringBootApplication
public class CatalogueApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogueApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initDatabase(CategoryRepo categoryRepo) {
        return args -> {
            List<Category> categoryNames = Arrays.asList(
                    new Category(UUID.randomUUID(), "Aksesoris Fashion", null),
                    new Category(UUID.randomUUID(), "Buku & Alat Tulis", null),
                    new Category(UUID.randomUUID(), "Elektronik", null),
                    new Category(UUID.randomUUID(),  "Fashion Bayi & Anak", null),
                    new Category(UUID.randomUUID(), "Fashion Muslim", null),
                    new Category(UUID.randomUUID(), "Fotografi", null),
                    new Category(UUID.randomUUID(), "Hobi & Koleksi", null),
                    new Category(UUID.randomUUID(), "Jam Tangan", null),
                    new Category(UUID.randomUUID(), "Perawatan & Kecantikan", null),
                    new Category(UUID.randomUUID(), "Makanan & Minuman", null),
                    new Category(UUID.randomUUID(), "Otomotif", null),
                    new Category(UUID.randomUUID(), "Perlengkapan Rumah", null),
                    new Category(UUID.randomUUID(), "Souvenir & Party Supplies", null)
            );

            List<Category> categories = categoryRepo.findAll();
            if (categories.isEmpty()) {
                categoryRepo.saveAll(categoryNames);
            }
        };
    }

    @Profile("dev")
    @Bean
    @Transactional
    CommandLineRunner fakeCatalogue(CategoryRepo categoryRepo, CatalogueRepo catalogueRepo) {
        return args -> {
            List<Category> categories = categoryRepo.findAll();

            Faker faker = new Faker(new Locale("in-ID"));
            Catalogue catalogue = new Catalogue();

            catalogue.setPrice(faker.number().numberBetween(10000, 999999));
            catalogue.setImage("https://picsum.photos/600");
            catalogue.setStock(faker.number().randomDigit());
            catalogue.setProductName(faker.commerce().productName());
            catalogue.setProductDescription(faker.lorem().sentence());
            catalogue.setSellerId(UUID.randomUUID());
            catalogue.setCategory(categories.get(new Random().nextInt(categories.size() - 1)));
            catalogueRepo.save(catalogue);
        };
    }
}
