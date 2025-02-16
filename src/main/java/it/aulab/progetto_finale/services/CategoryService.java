package it.aulab.progetto_finale.services;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


import it.aulab.progetto_finale.dtos.CategoryDto;
import it.aulab.progetto_finale.models.Category;
import it.aulab.progetto_finale.repositories.CategoryRepository;

@Service

public class CategoryService implements CrudService<CategoryDto, Category, Long>{


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDto> readAll() {
        List<CategoryDto> dtos = new ArrayList<CategoryDto>();
        for(Category category: categoryRepository.findAll()) {
            dtos.add(modelMapper.map(category, CategoryDto.class));
        }
        return dtos;
    }

@Override
public CategoryDto read(Long key) {
throw new UnsupportedOperationException("Unimplemented method 'read'");
}


@Override
public CategoryDto create(Category model, Principal principal, MultipartFile file) {
throw new UnsupportedOperationException("Unimplemented method 'create'");
}


@Override
public CategoryDto update(Long key, Category model, MultipartFile file) {
throw new UnsupportedOperationException("Unimplemented method 'update'");
}

@Override
public void delete(Long key) {
throw new UnsupportedOperationException("Unimplemented method 'delete'");
}

}
