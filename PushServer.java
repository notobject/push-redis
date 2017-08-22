import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * author: LongDuping
 * date  : 2017-08-22 17:32
 * email : 610215675@qq.com
 * --------------------------------------------------
 * function: 推送服务端
 */
public class PushServer {

    private String host = null;
    private int port = 6379;
    private String pass = null;
    private String channel = null;

    private JedisPool jedisPool = null;

    public PushServer(String host, int port, String pass, String channel) {
        this.host = host;
        this.port = port;
        this.pass = pass;
        this.channel = channel;
    }
    public PushServer(String host, int port, String channel) {
        this(host, port, null, channel);
    }
    public PushServer(String host, String channel) {
        this(host, 6379, null, channel);
    }

    public void sendMessage(String message) {
        if (jedisPool == null) {
            if (pass == null) {
                jedisPool = new JedisPool(host, port);
            } else {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 3000, pass);
            }
        }
        Jedis jedis = jedisPool.getResource();
        jedis.publish(channel, message);
        jedisPool.returnResource(jedis);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
