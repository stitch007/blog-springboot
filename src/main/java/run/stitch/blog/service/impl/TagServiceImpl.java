package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.entity.Tag;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.TagRepository;
import run.stitch.blog.service.TagService;
import run.stitch.blog.util.Copy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

    @Override
    public List<TagDTO> getTags() {
        List<Tag> tags = tagRepository.selectList(new LambdaQueryWrapper<Tag>().select(Tag::getId, Tag::getName));
        return Copy.copyList(tags, TagDTO.class);
    }

    @Override
    public List<TagDTO> getTagsByIds(String[] ids) {
        List<Tag> tags = tagRepository.selectBatchIds(Arrays.asList(ids));
        return Copy.copyList(tags, TagDTO.class);
    }

    /**
     * 保存或更新标签，带id字段为更新，不带id字段为保存
     *
     * @param tagDTO id,name字段
     * @return 数据库中name的id
     */
    @Override
    public Integer saveOrUpdateTag(TagDTO tagDTO) throws BizException {
        if (tagDTO.getName() == null) {
            throw new BizException(403, "标签名不能为空");
        }
        Tag dbTag = tagRepository.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagDTO.getName()));
        Tag tag = Copy.copyObject(tagDTO, Tag.class);
        if (dbTag == null) {
            if (tagDTO.getId() != null) {
                tagRepository.updateById(tag);
                return tag.getId();
            }
            tagRepository.insert(tag);
            return tag.getId();
        }
        return dbTag.getId();
    }

    @Override
    public Boolean deleteTags(String[] ids) {
        return tagRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }

    @Override
    public List<Integer> saveTags(List<TagDTO> tagDTOs) throws BizException {
        List<Integer> res = new ArrayList<>();
        List<String> tagNames = new ArrayList<>(tagDTOs.stream().map(TagDTO::getName).toList());
        if (!tagNames.isEmpty()) {
            List<Tag> dbTags = tagRepository.selectList(new LambdaQueryWrapper<Tag>().in(Tag::getName, tagNames));
            res.addAll(dbTags.stream().map(Tag::getId).toList());
            tagNames.removeAll(dbTags.stream().map(Tag::getName).toList());
            if (!tagNames.isEmpty()) {
                List<Tag> tags = tagNames.stream().map(tagName -> Tag.builder().name(tagName).build()).toList();
                tagRepository.insertBatch(tags);
                res.addAll(tags.stream().map(Tag::getId).toList());
            }
        }
        return res;
    }
}
