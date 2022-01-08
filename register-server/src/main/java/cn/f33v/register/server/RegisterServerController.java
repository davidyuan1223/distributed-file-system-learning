package cn.f33v.register.server;

/**
 * 这个controller负责接收register-client发送过来的请求
 * 在spring cloud eureka中用的组件是jersey,在国外很常用,其本身也是mvc的restful框架,
 * 可以接收http请求
 */
public class RegisterServerController {
    private Registry registry=Registry.getInstance();
    //服务注册
    public RegisterResponse register(RegisterRequest registerRequest){
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            ServiceInstance serviceInstance = new ServiceInstance();
            serviceInstance.setServiceName(registerRequest.getServiceName());
            serviceInstance.setIp(registerRequest.getIp());
            serviceInstance.setHostname(registerRequest.getHostname());
            serviceInstance.setPort(registerRequest.getPort());
            serviceInstance.setServiceInstanceId(registerRequest.getServiceInstanceId());
            registry.register(serviceInstance);
            registerResponse.setStatus(RegisterResponse.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            registerResponse.setStatus(RegisterResponse.FAILURE);
        }
        return registerResponse;
    }

    /**
     * 发送心跳
     * @param heartbeatRequest 心跳请求
     * @return 心跳响应
     */
    public HeartbeatResponse heartbeat(HeartbeatRequest heartbeatRequest){
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        try {
            ServiceInstance serviceInstance = registry.getServiceInstance(heartbeatRequest.getServiceName(), heartbeatRequest.getServiceInstanceId());
            //进行续约
            serviceInstance.renew();
            heartbeatResponse.setStatus(RegisterResponse.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            heartbeatResponse.setStatus(RegisterResponse.FAILURE);
        }
        return heartbeatResponse;

    }
}