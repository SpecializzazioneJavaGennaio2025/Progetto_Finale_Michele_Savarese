package it.aulab.progetto_finale.dtos;

import java.time.LocalDate;

import it.aulab.progetto_finale.models.Category;
import it.aulab.progetto_finale.models.User;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
private Long id;
private String title;
private String subtitle;
private String body;
private LocalDate publishDate;
private User user;
private Category category;


}
