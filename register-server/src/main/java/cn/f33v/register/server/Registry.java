package cn.f33v.register.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册表
 */
public class Registry {
    //注册表是一个单例
    private static Registry instance=new Registry();
    private Registry(){

    }
    /**
     * 核心的内存数据结构,注册表
     * Map: key是服务名称,value是服务实例map
     * Map<String,ServiceInstance>
     *     key是服务实例id,value是服务实例
     * </String,ServiceInstance>
     */
    private Map<String ,Map<String,ServiceInstance>> registry=new HashMap<>();
    //服务注册
    public void register(ServiceInstance serviceInstance){
        Map<String, ServiceInstance> serviceInstanceMap =
                registry.computeIfAbsent(serviceInstance.getServiceName(), k -> new HashMap<>());
        serviceInstanceMap.put(serviceInstance.getServiceInstanceId(),
                serviceInstance);
        System.out.println("服务实例["+serviceInstance+"],完成注册....");
        System.out.println("注册表: "+registry);
    }

    /**
     * 获取服务实例
     * @param serviceName 服务名称
     * @param serviceInstanceId 服务实例id
     * @return 服务实例
     */
    public ServiceInstance getServiceInstance(String serviceName,String serviceInstanceId){
        Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceName);
        return serviceInstanceMap.get(serviceInstanceId);
    }
    public static Registry getInstance(){
        return instance;
    }

    /**
     * 获取整个注册表
     */
    public Map<String, Map<String, ServiceInstance>> getRegistry() {
        return registry;
    }
    /**
     * 从注册表中删除某个服务实例
     */
    public void remove(String serviceName,String serviceInstanceId){
        System.out.println("服务实例["+serviceInstanceId+"],从注册表中摘除");

        Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceName);
        serviceInstanceMap.remove(serviceInstanceId);
    }

}