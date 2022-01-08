package cn.f33v.register.client;

/**
 * 负责发送各种http请求的组件
 */
public class HttpSender {
    /**
     * 发起注册请求
     * @param request
     * @return
     */
    public RegisterResponse register(RegisterRequest request){
        //这里实际上会基于类似HttpClient这种开源网络包
        //开源构造一个请求,里面放入这个服务实例信息.比如服务名称,ip地址,端口号
        //通过请求发送过去
        System.out.println("服务实例 ["+request+"],发起请求进行注册...");
        //收到register-server响应后封装一个response对象
        return new RegisterResponse(RegisterResponse.SUCCESS);
    }
    /**
     * 发起心跳请求
     */
    public HeartbeatResponse heartbeat(HeartbeatRequest request){
        //这里实际上会基于类似HttpClient这种开源网络包
        //开源构造一个请求,里面放入这个服务实例信息.比如服务名称,ip地址,端口号
        //通过请求发送过去
        System.out.println("服务实例 ["+request.getServiceInstanceId()+"],发起心跳请求...");
        //收到register-server响应后封装一个response对象
        return new HeartbeatResponse(HeartbeatResponse.SUCCESS);
    }
}