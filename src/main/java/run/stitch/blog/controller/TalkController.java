package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.TalkDTO;
import run.stitch.blog.service.TalkService;
import run.stitch.blog.util.Result;

import java.util.HashMap;
import java.util.List;

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
    public Result<?> saveTalk(@RequestBody TalkDTO talkDTO) {
        if (talkDTO.getId() != null) {
            return Result.error(400, "新增操作不能携带id");
        }
        return Result.ok(new HashMap<>() {{
            put("id", talkService.saveOrUpdateTalk(talkDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/talks")
    public Result<?> updateTalk(@RequestBody TalkDTO talkDTO) {
        if (talkDTO.getId() == null) {
            return Result.error(400, "修改操作必须携带id");
        }
        return Result.ok(new HashMap<>() {{
            put("id", talkService.saveOrUpdateTalk(talkDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/talks/{ids}")
    public Result<?> deleteTalks(@PathVariable("ids") String[] ids) {
        if (talkService.deleteTalks(ids)) {
            return Result.ok();
        }
        return Result.error(500, "删除失败");
    }
}
