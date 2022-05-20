package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.stitch.blog.dto.CategoryDTO;
import run.stitch.blog.entity.Category;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.CategoryRepository;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.util.Copy;

import java.util.Arrays;
import java.util.List;

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

    /**
     * 保存或更新分类，带id字段为更新，不带id字段为保存
     *
     * @param categoryDTO id,name字段
     * @return 数据库中name的id
     */
    @Override
    public Integer saveOrUpdateCategory(CategoryDTO categoryDTO) throws BizException {
        if (categoryDTO.getName() == null) {
            throw new BizException(403, "分类名不能为空");
        }
        Category dbCategory = categoryRepository.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, categoryDTO.getName()));
        Category category = Copy.copyObject(categoryDTO, Category.class);
        if (dbCategory == null) {
            if (categoryDTO.getId() == null) {
                categoryRepository.insert(category);
                return category.getId();
            }
            categoryRepository.updateById(category);
            return category.getId();
        }
        return dbCategory.getId();
    }

    @Override
    public boolean deleteCategories(String[] ids) {
        return categoryRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}