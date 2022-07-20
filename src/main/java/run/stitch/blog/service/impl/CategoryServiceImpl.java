package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.CategoryDTO;
import run.stitch.blog.entity.Category;
import run.stitch.blog.repository.CategoryRepository;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.util.Copy;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryRepository, Category> implements CategoryService {
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
    public Integer saveOrUpdateCategory(CategoryDTO categoryDTO) {
        if (ObjectUtils.isEmpty(categoryDTO.getName())) {
            return null;
        }
        Category dbCategory = categoryRepository.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getName, categoryDTO.getName()));
        if (!ObjectUtils.isEmpty(dbCategory)) {
            return dbCategory.getId();
        }
        Category category = Copy.copyObject(categoryDTO, Category.class);
        if (this.saveOrUpdate(category)) {
            return category.getId();
        }
        return null;
    }

    @Override
    public boolean deleteCategories(String[] ids) {
        return categoryRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}