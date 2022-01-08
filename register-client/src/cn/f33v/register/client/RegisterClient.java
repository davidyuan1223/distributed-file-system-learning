package cn.f33v.register.client;

import java.util.UUID;

/**
 * 在服务上被创建和启动,负责跟register-server进行通信
 */
public class RegisterClient {
    public static final String SERVICE_NAME="inventory-service";
    public static final String IP="192.168.31.208";
    public static final String HOST_NAME="inventory-1";
    public static final int PORT=9000;
    //http通信组件
    private HttpSender httpSender;
    //服务实例id
    private String serviceInstanceId;
    //心跳线程
    private HeartbeatWorker heartbeatWorker;
    //服务实例是否在运行
    private Boolean isRunning;

    public RegisterClient() {
        serviceInstanceId = UUID.randomUUID().toString().replace("-", "");
        httpSender=new HttpSender();
        this.heartbeatWorker = new HeartbeatWorker();
        this.isRunning=true;
    }

    /**
     * 停止register-client
     */
    public void shutdown(){
        //这里先修改了标志位
        this.isRunning=false;
        //这里进行打断,打断了Thread.sleep进行循环判断到为false线程停止
        this.heartbeatWorker.interrupt();
    }
    /**
     * 启动register-client
     */
    public void start(){
        try{
            //一旦启动这个组件后,就负责在服务上干两件事情
            //1.开启一个线程向register-server发送请求注册该服务
            //2.注册成功后开启另外一个线程去发送心跳
            //服务实例id
        /*
        模型简化:
        在register-client这块就开启一个线程
        这个线程刚启动的时候,第一个事情就是完成注册
        如果注册完成后就会进入一个while true死循环
        每隔30s就发送一个请求进行一个心跳
         */
            RegisterWorker registerWorker = new RegisterWorker();
            registerWorker.start();
            registerWorker.join();

            heartbeatWorker.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private class RegisterWorker extends Thread{

        @Override
        public void run() {
            //这个线程应该是获取当前机器信息
            //包括当前机器的ip地址,hostname,以及配置这个服务监听的端口
            //从配置文件中可以拿到
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setServiceName(SERVICE_NAME);
            registerRequest.setHostname(HOST_NAME);
            registerRequest.setIp(IP);
            registerRequest.setPort(PORT);
            registerRequest.setServiceInstanceId(serviceInstanceId);
            RegisterResponse response=httpSender.register(registerRequest);
            System.out.println("服务注册结果是:"+response.getStatus()+".....");
            //如果注册成功的话.finishedRegister更新为true.失败的话就抛异常/返回

        }
    }
    private class HeartbeatWorker  extends Thread{
        //是否完成服务注册标志
//        private Boolean finishedRegister;

        @Override
        public void run() {

            //如果注册成功就进入while true死循环
//            if (finishedRegister) {
                HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
                heartbeatRequest.setServiceName(SERVICE_NAME);
                heartbeatRequest.setServiceInstanceId(serviceInstanceId);
                HeartbeatResponse heartbeatResponse = null;
                while (isRunning){
                    try {
                        heartbeatResponse=httpSender.heartbeat(heartbeatRequest);
                        System.out.println("心跳结果为:"+heartbeatResponse.getStatus()+"...");
                        //每隔30s发送一次心跳
                        Thread.sleep(30000);
                        System.out.println("发送心跳");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        }
    }
}