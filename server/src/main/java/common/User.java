package common;

import java.io.Serializable;

public class User implements Serializable {
    private String userId ;
    private String passwd ;
    //status表示用户的请求，0表示登录，1表示注册
    private int status =0 ;



    private static final long serialVersionUID = 1L;

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
