package cn.f33v.register.server;

/**
 * 注册响应
 */
public class RegisterResponse {
    public static final String SUCCESS="success";
    public static final String FAILURE="failure";
    //注册响应状态
    private String status;
    //注册响应码
    private Integer code;
    //注册响应消息: 一般不放什么
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(String status) {
        this.status = status;
        if (status.equals(SUCCESS)) {
            this.code=200;
        }else {
            this.code=400;
        }
        this.message="";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}