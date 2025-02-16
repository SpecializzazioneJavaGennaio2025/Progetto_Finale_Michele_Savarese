package it.aulab.progetto_finale.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import it.aulab.progetto_finale.dtos.ArticleDto;
import it.aulab.progetto_finale.dtos.CategoryDto;
// import it.aulab.progetto_finale.models.Article;
import it.aulab.progetto_finale.models.Category;
import it.aulab.progetto_finale.services.ArticleService;
import it.aulab.progetto_finale.services.CategoryService;

@Controller
@RequestMapping("/categories")


public class CategoryController {

@Autowired
private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

//! ricerca per categoria

@GetMapping("/search/{id}")
public String categorySearch(@PathVariable Long id, Model viewModel) {
    CategoryDto category = categoryService.read(id);
    viewModel.addAttribute("title", "Articoli della categoria " + category.getName());

    List<ArticleDto> articles = articleService.searchByCategory(modelMapper.map(category, Category.class));


    viewModel.addAttribute("articles", articles);

    // viewModel.addAttribute("articles", articleService.findByCategory(category));
    return "article/articles";

}
}
