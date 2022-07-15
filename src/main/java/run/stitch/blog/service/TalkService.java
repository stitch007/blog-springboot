package run.stitch.blog.service;

import run.stitch.blog.dto.TalkDTO;
import run.stitch.blog.dto.params.SaveTalkParam;
import run.stitch.blog.dto.params.UpdateTalkParam;

import java.util.List;

public interface TalkService {
    List<TalkDTO> getTalks();

    List<TalkDTO> getTalksByIds(String[] ids);

    Integer saveTalk(SaveTalkParam saveTalkParam);

    Integer updateTalk(UpdateTalkParam updateTalkParam);

    Boolean deleteTalks(String[] ids);
}
