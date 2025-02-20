package it.aulab.progetto_finale.controllers;

// import java.lang.ProcessBuilder.Redirect;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.stream.Collectors;

import it.aulab.progetto_finale.dtos.ArticleDto;
import it.aulab.progetto_finale.dtos.CategoryDto;
import it.aulab.progetto_finale.models.Article;
import it.aulab.progetto_finale.models.Category;
import it.aulab.progetto_finale.services.ArticleService;
import it.aulab.progetto_finale.services.CrudService;
import it.aulab.progetto_finale.repositories.ArticleRepository;
// import jakarta.mail.Multipart;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto,Category,Long> categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public String articlesIndex(Model viewModel){
        viewModel.addAttribute("title", "All articles");

        List<ArticleDto> articles = new ArrayList<>();
        for(Article article : articleRepository.findByIsAcceptedTrue()){
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }

    @GetMapping("/create")
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Create Article");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "article/create";
    }

    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article") Article article,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Principal principal,
                                MultipartFile file,
                                Model viewModel){
        if(result.hasErrors()){
            viewModel.addAttribute("title", "Create Article");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            return "article/create";
        }
        articleService.create(article, principal, file);
        redirectAttributes.addFlashAttribute("successMessage", "Article created with success!");

        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detailArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title", "Article Detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "article/detail";
    }

    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title", "Edit Article");
        viewModel.addAttribute("article", articleService.read(id));
        viewModel.addAttribute("categories", categoryService.readAll());
        return "article/edit";
    }

    @PostMapping("/update/{id}")
    public String articleUpdate(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute("article") Article article,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal,
                                    MultipartFile file,
                                    Model viewModel){
        if(result.hasErrors()){
            viewModel.addAttribute("title", "Edit article");
            article.setImage(articleService.read(id).getImage());
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            return "article/edit";
        }

        articleService.update(id, article, file);
        redirectAttributes.addFlashAttribute("successMessage", "Article updated with success");
        return "redirect:/articles";
    }

    @GetMapping("/delete/{id}")
    public String articleDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        articleService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Article deleted with success");
        return "redirect:/writer/dashboard";
    }

    @GetMapping("revisor/detail/{id}")
    public String revisorDetailArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title", "Article detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "revisor/detail";
    }

    @PostMapping("/accept")
    public String articleSetAccepted(@RequestParam("action") String action, @RequestParam("articleId") Long articleId, RedirectAttributes redirectAttributes){
        if (action.equals("accept")) {
            articleService.setIsAccepted(true, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Article accepted");
        } else if(action.equals("rejected")){
            articleService.setIsAccepted(false, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Article rejected");
        } else {
            redirectAttributes.addFlashAttribute("resultMessage", "What are you doing?");
        }
        return "redirect:/revisor/dashboard";
    }

    @GetMapping("/search")
    public String articleSearch(@Param("keyword") String keyword, Model viewModel){
        viewModel.addAttribute("title", "All articles searched");
        List<ArticleDto> articles = articleService.search(keyword);
        List<ArticleDto> acceptedArticles = articles.stream().filter(article->Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());
        viewModel.addAttribute("articles", acceptedArticles);
        return "article/articles";
    }
}