package run.stitch.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.stitch.blog.dto.TalkDTO;
import run.stitch.blog.entity.Talk;
import run.stitch.blog.repository.TalkRepository;
import run.stitch.blog.service.TalkService;
import run.stitch.blog.util.Copy;

import java.util.Arrays;
import java.util.List;

@Service
public class TalkServiceImpl implements TalkService {
    @Autowired
    TalkRepository talkRepository;

    @Override
    public List<TalkDTO> getTalks() {
        List<Talk> talks = talkRepository.selectList(new LambdaQueryWrapper<Talk>().orderByDesc(Talk::getCreateTime));
        return Copy.copyList(talks, TalkDTO.class);
    }

    @Override
    public List<TalkDTO> getTalksByIds(String[] ids) {
        List<Talk> talks = talkRepository.selectBatchIds(Arrays.asList(ids));
        return Copy.copyList(talks, TalkDTO.class);
    }

    @Override
    public Integer saveOrUpdateTalk(TalkDTO talkDTO) {
        Talk talk = Copy.copyObject(talkDTO, Talk.class);
        if (talk.getId() == null) {
            talk.setUserId(Integer.parseInt(StpUtil.getLoginId().toString()));
            talkRepository.insert(talk);
            return talk.getId();
        }
        talkRepository.updateById(talk);
        return talk.getId();
    }

    @Override
    public Boolean deleteTalks(String[] ids) {
        return talkRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}
