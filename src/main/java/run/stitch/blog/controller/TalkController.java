package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.TalkDTO;
import run.stitch.blog.dto.params.SaveTalkParam;
import run.stitch.blog.dto.params.UpdateTalkParam;
import run.stitch.blog.service.TalkService;
import run.stitch.blog.util.Result;

import java.util.HashMap;
import java.util.List;

import static run.stitch.blog.util.StatusCode.*;

@RestController
public class TalkController {
    @Autowired
    TalkService talkService;

    @GetMapping("/talks")
    public Result<List<TalkDTO>> getTalks() {
        return Result.ok(talkService.getTalks());
    }

    @GetMapping("/talks/{ids}")
    public Result<List<TalkDTO>> getTalksByIds(@PathVariable("ids") String[] ids) {
        return Result.ok(talkService.getTalksByIds(ids));
    }

    @SaCheckRole("ADMIN")
    @PostMapping("/talks")
    public Result<?> saveTalk(@RequestBody @Validated SaveTalkParam saveTalkParam) {
        Integer id = talkService.saveTalk(saveTalkParam);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/talks")
    public Result<?> updateTalk(@RequestBody @Validated UpdateTalkParam updateTalkParam) {
        Integer id = talkService.updateTalk(updateTalkParam);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/talks/{ids}")
    public Result<?> deleteTalks(@PathVariable("ids") String[] ids) {
        if (talkService.deleteTalks(ids)) {
            return Result.ok();
        }
        return Result.error(FAIL);
    }
}
