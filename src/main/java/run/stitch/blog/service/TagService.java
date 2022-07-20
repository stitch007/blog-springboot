package run.stitch.blog.service;

import run.stitch.blog.dto.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> getTags();

    List<TagDTO> getTagsByIds(String[] ids);

    Integer saveOrUpdateTag(TagDTO tagDTO);

    List<Integer> saveTags(List<TagDTO> tagDTOs);

    Boolean deleteTags(String[] ids);
}
