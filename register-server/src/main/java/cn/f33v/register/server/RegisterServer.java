package cn.f33v.register.server;

import java.util.UUID;

/**
 * 代表了服务注册中心
 */
public class RegisterServer {
    public static void main(String[] args) {
//        System.out.println(Thread.currentThread().getName()+"线程的线程组是: "+Thread.currentThread().getThreadGroup());
        RegisterServerController registerServerController = new RegisterServerController();
        String serviceInstanceId=UUID.randomUUID().toString().replace("-","");
        //模拟发起一个服务注册的请求
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setHostname("inventory-service-01");
        registerRequest.setIp("102.168.0.0");
        registerRequest.setPort(9000);
        registerRequest.setServiceName("inventory-service");
        registerRequest.setServiceInstanceId(serviceInstanceId);
        registerServerController.register(registerRequest);
        //模拟进行一次心跳,完成续约
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
        heartbeatRequest.setServiceName("inventory-service");
        heartbeatRequest.setServiceInstanceId(serviceInstanceId);
        registerServerController.heartbeat(heartbeatRequest);
        //开启一个后台线程,检查微服务
        ServiceAliveMonitor serviceAliveMonitor = new ServiceAliveMonitor();
        serviceAliveMonitor.start();
//        System.out.println(Thread.currentThread().getName()+"线程的线程组是: "+Thread.currentThread().getThreadGroup());
        /*
         * 一般来说像register-server不会只有一个main线程作为工作线程,
         * 它一般是一个web工程,部署在一个web服务器中,
         * 最核心的工作线程就是专门用于接收和处理register-client发送过来的请求的那些工作线程,
         * 正常来说只要有工作线程是不会随便退出的,
         * 当然如果说工作线程都停止了那么daemon线程会跟着jvm进程一起退出
         * 在这部分代码中暂时没有网络所以暂时在main方法中是while true让main工作线程不会退出保证服务一直运行,daemon线程来正常工作
         * 如果说register-server中的核心工作线程就是用于接收和处理请求的工作线程
         * 它们都结束停止工作了,销毁了
         * 此时register-server里面就只剩下daemon线程,就会跟着jvm进程一块退出
         */


        while (true) {
            try {
                //每隔30s发送一次心跳
                Thread.sleep(30*1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}