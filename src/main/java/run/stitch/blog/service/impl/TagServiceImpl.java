package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.dto.params.SaveTagParam;
import run.stitch.blog.dto.params.UpdateTagParam;
import run.stitch.blog.entity.Tag;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.TagRepository;
import run.stitch.blog.service.TagService;
import run.stitch.blog.util.Copy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static run.stitch.blog.util.StatusCode.*;

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

    @Override
    public Integer saveTag(SaveTagParam saveTagParam) {
        Tag dbTag = tagRepository.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, saveTagParam.getName()));
        if (!ObjectUtils.isEmpty(dbTag)) {
            throw new BizException(EXISTED);
        }
        Tag tag = Tag.builder().name(saveTagParam.getName()).build();
        if (tagRepository.insert(tag) > 0) {
            return tag.getId();
        }
        return null;
    }

    @Override
    public Integer updateTag(UpdateTagParam updateTagRequest) {
        Tag dbTag = tagRepository.selectById(updateTagRequest.getId());
        if (ObjectUtils.isEmpty(dbTag)) {
            throw new BizException(NO_EXISTED);
        }
        Tag tag = Copy.copyObject(updateTagRequest, Tag.class);
        if (tagRepository.updateById(tag) > 0) {
            return tag.getId();
        }
        return null;
    }

    @Override
    public Boolean deleteTags(String[] ids) {
        return tagRepository.deleteBatchIds(Arrays.asList(ids)) >= 0;
    }

    @Override
    public List<Integer> saveTags(List<UpdateTagParam> updateTagRequests) {
        List<Integer> res = new ArrayList<>();
        List<String> tagNames = new ArrayList<>(updateTagRequests.stream().map(UpdateTagParam::getName).toList());
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
