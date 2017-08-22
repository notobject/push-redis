import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * author: LongDuping
 * date  : 2017-07-27 11:36
 * email : 610215675@qq.com
 * --------------------------------------------------
 * function: 推送客户端
 */
public class PushClient {

    // Redis服务器地址
    private String host = null;
    // Redis服务端口
    private int port = 6379;
    // Redis连接密码，如果没有则设为null
    private String pass = null;
    // 监听的频道 默认为“push”
    private String channel = null;

    // 消息到达后处理接口，由使用者自己实现
    private OnPushMessageListener onPushMessageListener;

    // 监听线程
    private Thread pushLisenter = null;

    public PushClient(OnPushMessageListener onPushMessageListener) {
        this.onPushMessageListener = onPushMessageListener;
    }


    public PushClient setAddress(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public PushClient setAddress(String host) {
        this.host = host;
        return this;
    }

    public PushClient setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public PushClient setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    /**
     * 开始监听
     *
     * @return
     */
    public PushClient listen() {
        if (host == null) {
            throw new RuntimeException("你必须指定host!");
        }
        if (onPushMessageListener == null) {
            throw new RuntimeException("你必须实现处理接口onPushMessageListener，不然意义在哪里？");
        }
        if (channel == null){
            throw new RuntimeException("你必须指定订阅的频道channel!");
        }
        if (pushLisenter != null) {
            return this;
        }
        pushLisenter = new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis jedis = new Jedis(host, port);
                if (pass != null && !"".equals(pass)) {
                    jedis.auth(pass);
                }
                System.out.println("PushClient:to start listen '" + channel + "' | " + host + ":" + port);
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        onPushMessageListener.onMesssage(message);
                    }
                }, channel);
                jedis.close();
            }
        });
        pushLisenter.start();
        return this;
    }

    /**
     * 停止监听
     */
    public void stop() {
        try {
            pushLisenter.stop();
            pushLisenter = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送消息处理接口
     */
    public interface OnPushMessageListener {
        /**
         * 消息到达
         *
         * @param message 消息
         */
        void onMesssage(String message);
    }
}
