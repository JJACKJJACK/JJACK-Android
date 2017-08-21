package com.mkm.hanium.jjack.login;

/**
* Created by MIN on 2017-08-21.
*/


public class ProfileItem {
    private long userId;
    private String imagePath;
    private String nickname;
    private String email;
    private boolean login; // 로그인 상태면 true

    public ProfileItem() {
        login = false;
    }

    public long getUserId() {
        return userId;
    }
    void setUserId(long userId) {
        this.userId = userId;
    }

    public String getImagePath() {
        return imagePath;
    }
    private void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNickname() {
        return nickname;
    }
    private void setNickname(String nicname) {
        this.nickname = nicname;
    }

    public String getEmail() {
        return email;
    }
    private void setEmail(String email) {
        this.email = email;
    }

    public boolean isLogin() {
        return login;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setProfile(long userId, String imagePath, String nicname, String email, boolean login) {
        setUserId(userId);
        setImagePath(imagePath);
        setNickname(nicname);
        setEmail(email);
        setLogin(login);
    }

    public void setProfile(String imagePath, String nicname, String email) {
        setImagePath(imagePath);
        setNickname(nicname);
        setEmail(email);
    }

    public void clearProfile() {
        setUserId(-1);
        setImagePath(null);
        setNickname(null);
        setEmail(null);
        setLogin(false);
    }

    // 지금 막 가입한 경우 nicname이 비어있으나 login 변수는 true인 상태이다.
    public boolean isSignup() {
        return (login && nickname == null);
    }
}
