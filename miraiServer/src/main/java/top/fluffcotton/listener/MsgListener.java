package top.fluffcotton.listener;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotEvent;
import org.jetbrains.annotations.NotNull;
import top.fluffcotton.service.ListenerService;


/**
 * @Description 用于监听全部事件
 * @Author zyc
 * @Date 2020/11/22 16:42
 */
public class MsgListener extends SimpleListenerHost {


    private final ListenerService listenerService;

    public MsgListener() {
        super();
        listenerService = ListenerService.getListenerService();
    }

    public MsgListener(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        listenerService = ListenerService.getListenerService();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
        // 处理事件处理时抛出的异常
    }

    /**
     * Bot 登录完成	 BotOnlineEvent
     * Bot 离线	 BotOfflineEvent
     * 主动	 Active
     * 被挤下线	 Force
     * 被服务器断开或因网络问题而掉线	 Dropped
     * 服务器主动要求更换另一个服务器	 RequireReconnect
     * Bot 重新登录	 BotReloginEvent
     * Bot 头像改变	 BotAvatarChangedEvent
     * (1.2.0+) Bot 昵称改变	 BotNickChangedEvent
     * (1.3.0+) Bot 被戳	 BotNudgedEvent
     * <p>
     * 1.1.0+) 主动发送消息前	 MessagePreSendEvent
     * 群消息	 GroupMessagePreSendEvent
     * 好友消息	 FriendMessagePreSendEvent
     * 群临时会话消息	 TempMessagePreSendEvent
     * (1.1.0+) 主动发送消息后	 MessagePostSendEvent
     * 群消息	 GroupMessagePostSendEvent
     * 好友消息	 FriendMessagePostSendEvent
     * 群临时会话消息	 TempMessagePostSendEvent
     * 消息撤回	 MessageRecallEvent
     * 好友撤回	 FriendRecall
     * 群撤回	 GroupRecall
     * 群撤回	 TempRecall
     * 图片上传前	 BeforeImageUploadEvent
     * 图片上传完成	 ImageUploadEvent
     * 图片上传成功	 Succeed
     * 图片上传失败	 Failed
     * <p>
     * 好友昵称改变	 FriendRemarkChangeEvent
     * 成功添加了一个新好友	 FriendAddEvent
     * 好友已被删除	 FriendDeleteEvent
     * 一个账号请求添加机器人为好友	 NewFriendRequestEvent
     * 好友头像改变	 FriendAvatarChangedEvent
     * (1.2.0+) 好友昵称改变	 FriendNickChangedEvent
     * (1.2.0+) 好友输入状态改变	 FriendInputStatusChangedEvent
     * (1.3.0+) 好友被戳	 FriendNudgedEvent
     * 处理消息事件
     * 群消息  GroupMessageEvent
     * 讨论组消息    TempMessageEvent
     * 好友消息     FriendMessageEvent
     *
     * @param event 事件
     * @throws Exception 抛出异常将在 handleException 中被处理
     */
    @EventHandler
    public void onMessage(@NotNull BotEvent event) throws Exception {
        // 可以抛出任何异常, 将在 handleException 处理
        listenerService.execute(event);
        // 无返回值, 表示一直监听事件.
    }


}
