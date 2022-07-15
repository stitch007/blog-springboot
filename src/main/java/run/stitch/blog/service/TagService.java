package run.stitch.blog.service;

import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.dto.params.SaveTagParam;
import run.stitch.blog.dto.params.UpdateTagParam;

import java.util.List;

public interface TagService {
    List<TagDTO> getTags();

    List<TagDTO> getTagsByIds(String[] ids);

    Integer saveTag(SaveTagParam saveTagParam);

    Integer updateTag(UpdateTagParam updateTagRequest);

    List<Integer> saveTags(List<UpdateTagParam> updateTagRequests);

    Boolean deleteTags(String[] ids);
}
