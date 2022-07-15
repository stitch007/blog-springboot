package run.stitch.blog.service;

import run.stitch.blog.dto.CategoryDTO;
import run.stitch.blog.dto.params.SaveCategoryParam;
import run.stitch.blog.dto.params.UpdateCategoryParam;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategories();

    List<CategoryDTO> getCategoriesByIds(String[] ids);

    Integer updateCategory(UpdateCategoryParam updateCategoryRequest);

    Integer saveCategory(SaveCategoryParam saveCategoryParam);

    boolean deleteCategories(String[] ids);
}
