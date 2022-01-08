package cn.f33v.register.client;

/**
 * register-client组件测试类
 */
public class RegisterClientTest {
    public static void main(String[] args) throws InterruptedException {
        RegisterClient registerClient = new RegisterClient();
        registerClient.start();
        Thread.sleep(5*1000);
        registerClient.shutdown();
    }
}