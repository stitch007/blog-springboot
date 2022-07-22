package run.stitch.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatTypeEnum {
    ONLINE_COUNT(1, "在线人数"),
    HISTORY_RECORD(2, "历史记录"),
    SEND_MESSAGE(3, "发送消息"),
    RECALL_MESSAGE(4, "撤回消息"),
    HEART_BEAT(5, "心跳消息");

    private final Integer type;

    private final String desc;

    public static ChatTypeEnum getChatType(Integer type) {
        for (ChatTypeEnum chatType : ChatTypeEnum.values()) {
            if (chatType.getType().equals(type)) {
                return chatType;
            }
        }
        return null;
    }
}
