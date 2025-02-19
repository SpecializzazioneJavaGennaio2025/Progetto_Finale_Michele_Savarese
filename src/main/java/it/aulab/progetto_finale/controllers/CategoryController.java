package it.aulab.progetto_finale.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.stream.Collectors;

import it.aulab.progetto_finale.dtos.ArticleDto;
import it.aulab.progetto_finale.dtos.CategoryDto;
// import it.aulab.progetto_finale.models.Article;
import it.aulab.progetto_finale.models.Category;
import it.aulab.progetto_finale.services.ArticleService;
import it.aulab.progetto_finale.services.CategoryService;
import jakarta.validation.Valid;

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

    List<ArticleDto> acceptedArticles = articles.stream().filter(article -> Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());

    viewModel.addAttribute("articles", acceptedArticles);

    // viewModel.addAttribute("articles", articleService.findByCategory(category));
    return "article/articles";

}

//! rotta per creazione delle categorie

@GetMapping("create")
public String categoryCreate(Model viewModel) {
    viewModel.addAttribute("title", "Crea una nuova categoria");
    viewModel.addAttribute("category", new Category());

    return "category/create";

}


//! salvataggio di una categoria

@PostMapping

public String categoryStore(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model viewModel) {
    if (bindingResult.hasErrors()) {
        viewModel.addAttribute("title", "Crea una nuova categoria");
        viewModel.addAttribute("category", category);
        return "category/create";
    }

    categoryService.create(category,null,null);
    redirectAttributes.addFlashAttribute("successMessage", "Categoria creata con successo");
    return "redirect:/admin/dashboard";
}

//! rotta per la modifica di una categoria

@GetMapping("/edit/{id}")
public String categoryEdit(@PathVariable("id") Long id, Model viewModel) {
    viewModel.addAttribute("title", "Modifica categoria");
    viewModel.addAttribute("category", categoryService.read(id));

    return "category/update";

}


//! salvataggio modifiche categoria

@PostMapping("/update/{id}")

public String categoryUpdate(@PathVariable("id")Long id, @Valid @ModelAttribute("category") Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model viewModel) {
    if (bindingResult.hasErrors()) {
        viewModel.addAttribute("title", "Modifica categoria");
        viewModel.addAttribute("category", category);
        return "category/update";
    }

    categoryService.update(id, category,null);
    redirectAttributes.addFlashAttribute("successMessage", "Categoria modificata con successo");
    return "redirect:/admin/dashboard";
}

//! rotta per la Cancellazione di una categoria

@GetMapping("/delete/{id}")
public String categoryDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
categoryService.delete(id);
redirectAttributes.addFlashAttribute("successMessage", "Categoria eliminata con successo");
return "redirect:/admin/dashboard";

}

}
