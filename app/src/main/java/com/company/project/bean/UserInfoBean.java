package com.company.project.bean;

/**
 * @author Administrator
 * @date 2017/11/22
 */

public class UserInfoBean {

    String token;
    /**
     * 真实姓名
     */
    String realName;
    long userId;
    String phone;
    /**
     * 身份证
     */
    String idCard;
    /**
     * 用户头像路径
     */
    String avatar;
    String email;
    /**
     * 性别（1：男，2：女）
     */
    String sex;
    /**
     * 角色 0-默认 100-投资人 110-投资人VIP 200-借款人 300-管理员 310-风控管理员 320-投资管理员 999-root管理员
     */
    int role;
    /**
     * 状态（0-未激活，1-已激活，2-锁定）
     */
    int status;

    String from;

    int versionCode;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
