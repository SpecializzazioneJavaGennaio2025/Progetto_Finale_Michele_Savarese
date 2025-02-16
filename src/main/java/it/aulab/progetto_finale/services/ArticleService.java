package it.aulab.progetto_finale.services;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import it.aulab.progetto_finale.dtos.ArticleDto;
import it.aulab.progetto_finale.models.Article;
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
private ModelMapper modelMapper;

@Override
public List<ArticleDto> readAll() {

    throw new UnsupportedOperationException("Unimplemente method readAll");
}

@Override
public ArticleDto read(Long key) {
    throw new UnsupportedOperationException("Unimplemente method read");
}

@Override
public ArticleDto create(Article article, Principal principal, MultipartFile file) {

Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if ( authentication != null ){
CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
User user = (userRepository.findById(userDetails.getId())).get();
article.setUser(user);
}

ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);
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

}