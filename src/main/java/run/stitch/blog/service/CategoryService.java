package run.stitch.blog.service;

import run.stitch.blog.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategories();

    List<CategoryDTO> getCategoriesByIds(String[] ids);

    Integer saveOrUpdateCategory(CategoryDTO categoryDTO);

    boolean deleteCategories(String[] ids);
}
