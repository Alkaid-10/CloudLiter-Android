package com.stupidtree.hichat.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.stupidtree.hichat.ui.welcome.signup.SignUpTrigger;
import com.stupidtree.hichat.utils.JsonUtils;

import java.util.Objects;


/**
 * 缓存在本地的此用户Model
 * 暂未和服务器返回数据格式匹配，需要适配函数
 */
public class UserLocal {
    //定义性别的枚举类型
    public enum GENDER {MALE, FEMALE}

    @Nullable
    String username; //用户名
    @Nullable
    String id; //用户id
    String nickname; //用户昵称
    @Nullable
    String token; //保存用户登陆状态的token（重要）
    UserLocal.GENDER gender; //用户性别

    @Nullable
    String avatar; //用户头像链接


    /**
     * 从服务器返回的JsonObject中解析出一个UserLocal对象
     * @param responseData 来自服务器
     * @return 返回
     */
    @NonNull
    public static UserLocal getFromResponseData(JsonObject responseData) {
        UserLocal userLocal = new UserLocal();
        JsonObject info = JsonUtils.getObjectData(responseData, "info");
        String token = JsonUtils.getStringData(responseData, "token");
        if (info != null) {
            String username = JsonUtils.getStringData(info, "username");
            String nickname = JsonUtils.getStringData(info, "nickname");
            String gender = JsonUtils.getStringData(info, "gender");
            String avatar = JsonUtils.getStringData(info, "avatar");
            userLocal.setUsername(username);
            userLocal.setNickname(nickname);
            userLocal.setGender(gender);
            userLocal.setAvatar(avatar);
            userLocal.setToken(token);
            return userLocal;
        }
        return userLocal;
    }


    public boolean isValid() {
        return token != null && username != null;
    }

    public void setGender(String gender) {
        this.gender = Objects.equals(gender, "MALE") ? UserLocal.GENDER.MALE : UserLocal.GENDER.FEMALE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    public UserLocal.GENDER getGender() {
        return gender;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
