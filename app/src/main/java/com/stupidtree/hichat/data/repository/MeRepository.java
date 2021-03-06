package com.stupidtree.hichat.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.stupidtree.hichat.HiApplication;
import com.stupidtree.hichat.data.model.UserLocal;
import com.stupidtree.hichat.data.source.UserPreferenceSource;


/**
 * 层次：Repository
 * ”我的“页面的Repository
 */
public class MeRepository {

    //也是单例模式
    private static MeRepository instance;

    //数据源：SharedPreference性质的本地状态数据源
    UserPreferenceSource mePreferenceSource;

    MeRepository(){
        //初始化数据源
        mePreferenceSource = new UserPreferenceSource(HiApplication.getContext());
    }

    public static MeRepository getInstance(){
        if(null == instance){
            instance = new MeRepository();
        }
        return instance;
    }


    /**
     * 登出
     */
    public void logout(){
        mePreferenceSource.clearLocalUser();
    }

    /**
     * 获得当时本地缓存的已登录用户
     * @return LiveData形式
     */
    public MutableLiveData<UserLocal> getLoggedInUser(){
       final MutableLiveData<UserLocal> result = new MutableLiveData<>();
       UserLocal userLocal = mePreferenceSource.getLocalUser();
       result.setValue(userLocal);
       return result;
    }


    /**
     * 更改该本地缓存的头像地址
     * @param newAvatar 头像地址
     */
    public void ChangeLocalAvatar(String newAvatar){
        mePreferenceSource.saveAvatar(newAvatar);
        // getThis().getSharedPreferences("Glide", Context.MODE_PRIVATE).edit().
    }

    /**
     * 更改本地缓存的昵称
     * @param nickname 新昵称
     */
    public void ChangeLocalNickname(String nickname){
        mePreferenceSource.saveNickname(nickname);
    }

    /**
     * 更改本地缓存的用户性别
     * @param gender 性别/MALE/FEMALE
     */
    public void ChangeLocalGender(String gender){
        mePreferenceSource.saveGender(gender);
    }

    /**
     * 直接获取本地已登陆的用户对象
     * 同步获取
     * @return 本地用户对象
     */
    @NonNull
    public UserLocal getLoggedInUserDirect(){
        return mePreferenceSource.getLocalUser();
    }
}
