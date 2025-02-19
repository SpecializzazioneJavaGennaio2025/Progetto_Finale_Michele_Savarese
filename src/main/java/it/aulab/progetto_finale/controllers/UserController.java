package it.aulab.progetto_finale.controllers;

// import java.lang.ProcessBuilder.Redirect;
// import java.text.Bidi;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; //!prova
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; //!prova

import it.aulab.progetto_finale.dtos.ArticleDto;
import it.aulab.progetto_finale.dtos.UserDto;
import it.aulab.progetto_finale.models.User; //!prova
import it.aulab.progetto_finale.services.ArticleService;
import it.aulab.progetto_finale.services.UserService;
import it.aulab.progetto_finale.services.CategoryService;
import it.aulab.progetto_finale.repositories.CareerRequestRepository;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid; //!prova

@Controller
public class UserController {

@Autowired

private UserService userService;

@Autowired
private ArticleService  articleService;

@Autowired
private CareerRequestRepository careerRequestRepository;

@Autowired
private CategoryService categoryService;

//!rotta di home

@GetMapping("/")
public String home(Model viewModel) {
    List<ArticleDto> articles = articleService.readAll();

Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());

List<ArticleDto> lastThreeArticles = articles.stream().limit(3).collect(Collectors.toList());

viewModel.addAttribute("articles", lastThreeArticles);
return "home";

}


//!rotta di register

@GetMapping("/register")
public String register(Model model) {
model.addAttribute("user", new UserDto());
return "auth/register";

}


//!rotta di login

@GetMapping("/login")
public String login() {
return "auth/login";

}



//! rotta per il salvataggio della registrazione
@PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the email provided");
        }



if (result.hasErrors()) {
model.addAttribute("user", userDto);
    return "auth/register";

}
userService.saveUser(userDto,redirectAttributes,request,response);
redirectAttributes.addFlashAttribute("successMessage", "Registrazione avvenuta con successo");

// return "redirect:/register?success";

return "redirect:/";

}


//! rotta ricerca articoli per utente

@GetMapping("/search/{id}")
public String userArticlesSearch(@PathVariable("id") Long id, Model viewModel) {
    User user = userService.find(id);
    viewModel.addAttribute("title", "Articoli di " + user.getUsername());

    List<ArticleDto> articles = articleService.searchByAuthor(user);
    viewModel.addAttribute("articles", articles);
    return "article/articles";

}

//! rotta per la dashboard dell'admin
@GetMapping("/admin/dashboard")
public String adminDashboard(Model viewModel) {
    viewModel.addAttribute("title", "Richieste ricevute");
    viewModel.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
    viewModel.addAttribute("categories", categoryService.readAll());
    return "admin/dashboard";

}

}