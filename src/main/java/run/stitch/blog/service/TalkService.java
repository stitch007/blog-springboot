package run.stitch.blog.service;

import run.stitch.blog.dto.TalkDTO;

import java.util.List;

public interface TalkService {
    List<TalkDTO> getTalks();

    List<TalkDTO> getTalksByIds(String[] ids);

    Integer saveOrUpdateTalk(TalkDTO talkDTO);

    Boolean deleteTalks(String[] ids);
}
