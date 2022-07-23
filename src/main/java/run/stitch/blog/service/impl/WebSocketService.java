package run.stitch.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.ChatRecordDTO;
import run.stitch.blog.dto.RecallMessageDTO;
import run.stitch.blog.dto.WebSocketMessageDTO;
import run.stitch.blog.entity.ChatRecord;
import run.stitch.blog.enums.ChatTypeEnum;
import run.stitch.blog.repository.ChatRecordRepository;
import run.stitch.blog.utils.CopyUtil;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static run.stitch.blog.enums.ChatTypeEnum.*;

@Slf4j
@Service
@ServerEndpoint("/websocket/{token}")
public class WebSocketService {
    private static ChatRecordRepository chatRecordRepository;

    private static ConcurrentHashMap<Integer, Session> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    public void setChatRecordRepository(ChatRecordRepository chatRecordRepository) {
        WebSocketService.chatRecordRepository = chatRecordRepository;
    }

    /**
     * 连接建立成功调用的方法, token无效不会建立连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        Object loginId = StpUtil.getLoginIdByToken(token);
        if (ObjectUtils.isEmpty(loginId)) {
            String str = JSONUtil.toJsonStr(new WebSocketMessageDTO(-1, "用户未登录"));
            session.getBasicRemote().sendText(str);
            session.close();
            log.error("连接失败，无效的token: " + token);
            return;
        }
        // 把session加入到map中
        sessionMap.put(SaFoxUtil.getValueByType(loginId, Integer.class), session);
        // 更新在线人数
        updateOnlineCount();
        // 查询并发送历史聊天记录
        WebSocketMessageDTO messageDTO = WebSocketMessageDTO.builder()
                .type(HISTORY_RECORD.getType())
                .data(getChatRecords())
                .build();
        sendMessage(session, messageDTO);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        // 从map中删掉session
        for (Integer key : sessionMap.keySet()) {
            if (sessionMap.get(key).getId().equals(session.getId())) {
                sessionMap.remove(key);
            }
        }
        // 更新在线人数
        updateOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebSocketMessageDTO messageDTO = JSONUtil.parse(message).toBean(WebSocketMessageDTO.class);
        ChatTypeEnum type = getChatType(messageDTO.getType());
        if (SEND_MESSAGE.equals(type)) {
            // 发送消息
            ChatRecordDTO chatRecordDTO = JSONUtil.parse(messageDTO.getData()).toBean(ChatRecordDTO.class);
            // 过滤html标签，防止xss注入
            chatRecordDTO.setContent(HtmlUtil.cleanHtmlTag(chatRecordDTO.getContent()));
            // 插入到数据库中
            ChatRecord chatRecord = CopyUtil.copyObject(chatRecordDTO, ChatRecord.class);
            chatRecordRepository.insert(chatRecord);
            chatRecordDTO.setId(chatRecord.getId());
            chatRecordDTO.setCreateTime(chatRecord.getCreateTime());
            messageDTO.setData(chatRecordDTO);
            // 广播消息
            broadcastMessage(messageDTO);
        } else if (RECALL_MESSAGE.equals(type)) {
            RecallMessageDTO recallMessageDTO = JSONUtil.parse(messageDTO.getData()).toBean(RecallMessageDTO.class);
            ChatRecord chatRecord = chatRecordRepository.selectById(recallMessageDTO.getId());
            // 判断消息存不存在
            if (ObjectUtils.isEmpty(chatRecord)) {
                return;
            }
            Object loginId = StpUtil.getLoginIdByToken(recallMessageDTO.getToken());
            Integer userId = SaFoxUtil.getValueByType(loginId, Integer.class);
            int compare = DateUtil.compare(DateUtil.offsetMinute(new Date(), -5), chatRecord.getCreateTime());
            // 是自己发起的撤回消息且5分钟内才能撤回
            if (!chatRecord.getUserId().equals(userId) || compare > 0) {
                return;
            }
            chatRecordRepository.deleteById(chatRecord.getId());
            recallMessageDTO.setToken(null);
            messageDTO.setData(recallMessageDTO);
            // 广播消息
            broadcastMessage(messageDTO);
        } else if (HEART_BEAT.equals(type)) {
            messageDTO.setData("pong");
            sendMessage(session, messageDTO);
        }
    }

    /**
     * 加载历史聊天记录
     */
    private List<ChatRecordDTO> getChatRecords() {
        // 获取7天内的聊天记录
        List<ChatRecord> chatRecordList = chatRecordRepository.selectList(new LambdaQueryWrapper<ChatRecord>()
                .ge(ChatRecord::getCreateTime, DateUtil.offsetDay(new Date(), -7)));
        return CopyUtil.copyList(chatRecordList, ChatRecordDTO.class);
    }

    /**
     * 更新在线人数
     */
    @Async
    public void updateOnlineCount() throws IOException {
        // 获取当前在线人数
        WebSocketMessageDTO messageDTO = WebSocketMessageDTO.builder()
                .type(ONLINE_COUNT.getType())
                .data(sessionMap.size())
                .build();
        // 广播消息
        broadcastMessage(messageDTO);
    }

    /**
     * 广播消息
     */
    private void broadcastMessage(WebSocketMessageDTO messageDTO) throws IOException {
        for (Session session : sessionMap.values()) {
            synchronized (session) {
                session.getBasicRemote().sendText(JSONUtil.toJsonStr(messageDTO));
            }
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage(Session session, WebSocketMessageDTO messageDTO) throws IOException {
        synchronized (session) {
            session.getBasicRemote().sendText(JSONUtil.toJsonStr(messageDTO));
        }
    }
}
