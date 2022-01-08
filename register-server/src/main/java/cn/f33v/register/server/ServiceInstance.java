package cn.f33v.register.server;

/**
 * 代表一个服务实例
 * 里面包含一个服务实例的所有信息
 * 比如服务名称,ip地址,hostname,端口号,服务实例id
 * 还有就是服务契约信息(Lease)
 */
public class ServiceInstance {
    //判断一个服务实例不再存活的周期
    public static final Long NOT_ALIVE_PERIOD=90*1000L;
    private String serviceName;
    private String ip;
    private String hostname;
    private Integer port;
    private String serviceInstanceId;
    private Lease lease;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "serviceName='" + serviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                ", serviceInstanceId='" + serviceInstanceId + '\'' +
                ", lease=" + lease +
                '}';
    }

    public ServiceInstance() {
        this.lease=new Lease();
    }

    /**
     * 服务续约
     */
    public void renew(){
        this.lease.renew();
    }

    /**
     * 判断是否存活
     * @return
     */
    public Boolean isAlive(){
        return this.lease.isAlive();
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }
    /**
     * 服务契约
     * 维护了一个服务实例跟当前注册中心之间的联系
     * 包括了心跳时间,创建时间等等
     */
    private class Lease {
        //最近一次心跳时间
        private Long latestHeartbeatTime=System.currentTimeMillis();

        /**
         * 续约,只要发送一次心跳,就相当于把register-client和register-server之间
         * 维护的一个契约进行了续约,表示xxx还存活着,之间的契约仍然可以维持着
         */
        public void renew() {
            this.latestHeartbeatTime = System.currentTimeMillis();
            System.out.println("服务实例["+ServiceInstance.this.getServiceInstanceId()+"],进行续约: "+latestHeartbeatTime);
        }
        /**
         * 判断当前服务实例的契约是否还存活
         */
        public Boolean isAlive(){
            Long currentTime=System.currentTimeMillis();
            if (currentTime - this.latestHeartbeatTime > NOT_ALIVE_PERIOD) {
                System.out.println("服务实例["+ServiceInstance.this.getServiceInstanceId()+"],不再存活");

                return false;
            }
            System.out.println("服务实例["+ServiceInstance.this.getServiceInstanceId()+"],保持存活");
            return true;
        }
    }

}