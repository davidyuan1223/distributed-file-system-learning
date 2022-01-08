package cn.f33v.register.server;

import java.util.Map;

/**
 * 微服务存活状态监控组件
 */
public class ServiceAliveMonitor {
    //检查服务实例是否存活的间隔
    public static final Long CHECK_ALIVE_INTERVAL=60*1000L;
    private Daemon daemon;

    public ServiceAliveMonitor() {
        this.daemon = new Daemon();
        //只要设置了这个标志为就代表这个线程是一个daemon线程,后台线程
        //非daemon线程一般叫做工作线程
        //如果工作线程都结束,daemon线程也会跟着一起结束
        daemon.setDaemon(true);
        daemon.setName("ServiceAliveMonitor");

    }

    /**
     * 启动后台线程
     */
    public void start(){
        daemon.start();

    }
    /**
     * 负责监控微服务存活状态的后台线程
     */
    private class Daemon extends Thread{
        private Registry registry=Registry.getInstance();
        @Override
        public void run() {
            Map<String, Map<String, ServiceInstance>> registryMap =null;
            while (true) {
                //每隔60s运行一次来检查,如果某个服务超过90秒还没发送心跳请求来进行续约
                //那么就摘除服务实例
                try {
                    registryMap=this.registry.getRegistry();
                    for (String serviceName : registryMap.keySet()) {
                        Map<String, ServiceInstance> serviceInstanceMap = registryMap.get(serviceName);
                        for (ServiceInstance serviceInstance : serviceInstanceMap.values()) {
                            //说明服务实例距离上一次发送心跳已经超过90s了
                            //认为这个服务掉线,从注册表中摘除这个服务实例
                            if (!serviceInstance.isAlive()) {
                                registry.remove(serviceName,serviceInstance.getServiceInstanceId());
                            }
                        }
                    }
                    Thread.sleep(CHECK_ALIVE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}