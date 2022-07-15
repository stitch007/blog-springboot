package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.CategoryDTO;
import run.stitch.blog.dto.params.SaveCategoryParam;
import run.stitch.blog.dto.params.UpdateCategoryParam;
import run.stitch.blog.entity.Category;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.CategoryRepository;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.util.Copy;

import java.util.Arrays;
import java.util.List;

import static run.stitch.blog.util.StatusCode.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getCategories() {
        List<Category> categories = categoryRepository.selectList(new LambdaQueryWrapper<Category>().select(Category::getId, Category::getName));
        return Copy.copyList(categories, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getCategoriesByIds(String[] ids) {
        List<Category> categories = categoryRepository.selectBatchIds(Arrays.asList(ids));
        return Copy.copyList(categories, CategoryDTO.class);
    }

    @Override
    public Integer saveCategory(SaveCategoryParam saveCategoryParam) {
        LambdaQueryWrapper wrapper = new LambdaQueryWrapper<Category>().eq(Category::getName, saveCategoryParam.getName());
        Category dbCategory = categoryRepository.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(dbCategory)) {
            throw new BizException(EXISTED);
        }
        Category category = Category.builder().name(saveCategoryParam.getName()).build();
        if (categoryRepository.insert(category) > 0) {
            return category.getId();
        }
        return null;
    }

    @Override
    public Integer updateCategory(UpdateCategoryParam updateCategoryRequest) {
        Category dbCategory = categoryRepository.selectById(updateCategoryRequest.getId());
        if (ObjectUtils.isEmpty(dbCategory)) {
            throw new BizException(NO_EXISTED);
        }
        Category category = Copy.copyObject(updateCategoryRequest, Category.class);
        if (categoryRepository.updateById(category) >= 0) {
            return category.getId();
        }
        return null;
    }

    @Override
    public boolean deleteCategories(String[] ids) {
        return categoryRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}