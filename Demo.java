/**
 * author: LongDuping
 * date  : 2017-08-22 19:19
 * email : 610215675@qq.com
 * --------------------------------------------------
 * function: 该代码基于jedis，运行前需要导入最新的jedis包
 */
public class Demo {

    // 使用示例
    public static void main(String[] args) {

        // 客户端监听消息
        PushClient client = new PushClient(new PushClient.OnPushMessageListener() {
            @Override
            public void onMesssage(String message) {
                System.out.println(message);
            }
        });
        client.setAddress("127.0.0.1");
        client.setChannel("push");
        client.listen();

        // 服务端发送消息
        PushServer pushServer = new PushServer("127.0.0.1", "push");
        pushServer.sendMessage("A");
        pushServer.sendMessage("B");
        pushServer.sendMessage("C");
    }
}
