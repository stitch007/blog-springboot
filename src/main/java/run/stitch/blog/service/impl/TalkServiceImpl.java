package run.stitch.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.stitch.blog.dto.TalkDTO;
import run.stitch.blog.dto.params.SaveTalkParam;
import run.stitch.blog.dto.params.UpdateTalkParam;
import run.stitch.blog.entity.Talk;
import run.stitch.blog.repository.TalkRepository;
import run.stitch.blog.service.TalkService;
import run.stitch.blog.utils.CopyUtil;

import java.util.Arrays;
import java.util.List;

@Service
public class TalkServiceImpl implements TalkService {
    @Autowired
    TalkRepository talkRepository;

    @Override
    public List<TalkDTO> getTalks() {
        List<Talk> talks = talkRepository.selectList(new LambdaQueryWrapper<Talk>().orderByDesc(Talk::getCreateTime));
        return CopyUtil.copyList(talks, TalkDTO.class);
    }

    @Override
    public List<TalkDTO> getTalksByIds(String[] ids) {
        List<Talk> talks = talkRepository.selectBatchIds(Arrays.asList(ids));
        return CopyUtil.copyList(talks, TalkDTO.class);
    }

    @Override
    public Integer saveTalk(SaveTalkParam saveTalkParam) {
        Talk talk = CopyUtil.copyObject(saveTalkParam, Talk.class);
        talk.setUserId(Integer.parseInt(StpUtil.getLoginId().toString()));
        if (talkRepository.insert(talk) > 0) {
            return talk.getId();
        }
        return null;
    }

    @Override
    public Integer updateTalk(UpdateTalkParam updateTalkParam) {
        Talk talk = CopyUtil.copyObject(updateTalkParam, Talk.class);
        if (talkRepository.updateById(talk) >= 0) {
            return talk.getId();
        }
        return null;
    }

    @Override
    public Boolean deleteTalks(String[] ids) {
        return talkRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}
