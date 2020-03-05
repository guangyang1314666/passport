package com.guangyang.development.social;

/**
 * 视图uuid，防止用户在注册页面进行回退，黑客回利用这个缺点对你进行疯狂的攻击
 */
public class ViewUuid {

    /**
     * 用户将已有的账号绑定到qq账号上的页面uuid
     */
    private String qqBindUserUuid;

    public String getQqBindUserUuid() {
        return qqBindUserUuid;
    }

    public void setQqBindUserUuid(String qqBindUserUuid) {
        this.qqBindUserUuid = qqBindUserUuid;
    }
}
