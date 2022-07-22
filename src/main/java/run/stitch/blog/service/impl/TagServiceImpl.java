package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.entity.Tag;
import run.stitch.blog.repository.TagRepository;
import run.stitch.blog.service.TagService;
import run.stitch.blog.utils.CopyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagRepository, Tag> implements TagService {
    @Autowired
    TagRepository tagRepository;

    @Override
    public List<TagDTO> getTags() {
        List<Tag> tags = tagRepository.selectList(new LambdaQueryWrapper<Tag>().select(Tag::getId, Tag::getName));
        return CopyUtil.copyList(tags, TagDTO.class);
    }

    @Override
    public List<TagDTO> getTagsByIds(String[] ids) {
        List<Tag> tags = tagRepository.selectBatchIds(Arrays.asList(ids));
        return CopyUtil.copyList(tags, TagDTO.class);
    }

    @Override
    public Integer saveOrUpdateTag(TagDTO tagDTO) {
        if (ObjectUtils.isEmpty(tagDTO.getName())) {
            return null;
        }
        Tag dbTag = tagRepository.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getName, tagDTO.getName()));
        if (!ObjectUtils.isEmpty(dbTag)) {
            return dbTag.getId();
        }
        Tag tag = CopyUtil.copyObject(tagDTO, Tag.class);
        if (this.saveOrUpdate(tag)) {
            return tag.getId();
        }
        return null;
    }

    @Override
    public Boolean deleteTags(String[] ids) {
        return tagRepository.deleteBatchIds(Arrays.asList(ids)) >= 0;
    }

    @Override
    public List<Integer> saveTags(List<TagDTO> tagDTOs) {
        List<Integer> res = new ArrayList<>();
        List<String> tagNames = new ArrayList<>(tagDTOs.stream().map(TagDTO::getName).toList());
        if (!tagNames.isEmpty()) {
            List<Tag> dbTags = tagRepository.selectList(new LambdaQueryWrapper<Tag>().in(Tag::getName, tagNames));
            res.addAll(dbTags.stream().map(Tag::getId).toList());
            tagNames.removeAll(dbTags.stream().map(Tag::getName).toList());
            if (!tagNames.isEmpty()) {
                List<Tag> tags = tagNames.stream().map(tagName -> Tag.builder().name(tagName).build()).toList();
                if (tagRepository.insertBatch(tags) < 0) {
                    return null;
                }
                res.addAll(tags.stream().map(Tag::getId).toList());
            }
        }
        return res;
    }
}
