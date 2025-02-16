package it.aulab.progetto_finale.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import it.aulab.progetto_finale.models.Article;
import it.aulab.progetto_finale.models.User;
import it.aulab.progetto_finale.models.Category;


public interface ArticleRepository extends ListCrudRepository<Article, Long> {
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    // List<Article> findByTitleContaining(String title);
    // List<Article> findByBodyContaining(String body);
    // List<Article> findByTitleContainingAndBodyContaining(String title, String body);
    // List<Article> findByTitleContainingAndCategory(String title, Category category);
    // List<Article> findByBodyContainingAndCategory(String body, Category category);
    // List<Article> findByTitleContainingAndBodyContainingAndCategory(String title, String body, Category category);

}
