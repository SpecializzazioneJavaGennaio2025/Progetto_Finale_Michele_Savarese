package it.aulab.progetto_finale.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import it.aulab.progetto_finale.dtos.ArticleDto;
import it.aulab.progetto_finale.models.Article;
import it.aulab.progetto_finale.models.Category;
import it.aulab.progetto_finale.models.User;
import it.aulab.progetto_finale.repositories.UserRepository;
import it.aulab.progetto_finale.repositories.ArticleRepository;


@Service
public class ArticleService implements CrudService< ArticleDto, Article, Long> {

@Autowired
private UserRepository userRepository;

@Autowired
private ArticleRepository articleRepository;

@Autowired
private ImageService imageService;

@Autowired
private ModelMapper modelMapper;




@Override
public List<ArticleDto> readAll() {
List<ArticleDto> dtos = new ArrayList<ArticleDto>();
for (Article article : articleRepository.findAll()) {
    dtos.add(modelMapper.map(article, ArticleDto.class));
}
return dtos;

    // throw new UnsupportedOperationException("Unimplemente method readAll");
}

@Override
public ArticleDto read(Long key) {
    Optional<Article> optArticle = articleRepository.findById(key);
if (optArticle.isPresent()) {
 return modelMapper.map(optArticle.get(), ArticleDto.class);
}else {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id=" + key + " not found");
}
    // throw new UnsupportedOperationException("Unimplemente method read");
}

@Override
public ArticleDto create(Article article, Principal principal, MultipartFile file) {
String url="";

Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if ( authentication != null ){
CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
User user = (userRepository.findById(userDetails.getId())).get();
article.setUser(user);
}

if (!file.isEmpty()) {
    try {
        CompletableFuture<String> futureUrl= imageService.saveImageOnCloud(file);
        url = futureUrl.get();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);
if(!file.isEmpty()) imageService.saveImageOnDB(url, article);

return dto;
}


@Override
public ArticleDto update(Long key, Article model, MultipartFile file) {
    throw new UnsupportedOperationException("Unimplemente method update");
}

@Override
public void delete(Long key) {
    throw new UnsupportedOperationException("Unimplemente method delete");
}


public List<ArticleDto> searchByCategory(Category category) {
    List<ArticleDto> dtos = new ArrayList<ArticleDto>();
    for (Article article : articleRepository.findByCategory(category)) {
        dtos.add(modelMapper.map(article, ArticleDto.class));
    }
    return dtos;
}

public List<ArticleDto> searchByAuthor(User user) {
    List<ArticleDto> dtos = new ArrayList<ArticleDto>();
    for (Article article : articleRepository.findByUser(user)) {
        dtos.add(modelMapper.map(article, ArticleDto.class));
    }
    return dtos;
}

}