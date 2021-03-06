package com.stupidtree.hichat.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.stupidtree.hichat.data.model.UserLocal;
import com.stupidtree.hichat.data.model.UserProfile;
import com.stupidtree.hichat.data.repository.MeRepository;
import com.stupidtree.hichat.data.repository.ProfileRepository;
import com.stupidtree.hichat.ui.base.DataState;

import java.util.Objects;

/**
 * 层次：ViewModel
 * 其他用户（好友、搜索结果等）的资料页面绑定的ViewModel
 */
public class ProfileViewModel extends ViewModel {

    /**
     * 数据区
     */
    //数据本体：用户资料
    LiveData<DataState<UserProfile>> profileLiveData;
    //数据本体：我和这个用户是否是好友
    LiveData<DataState<Boolean>> relationLiveData;
    //Trigger：控制↑两个的刷新
    MutableLiveData<ProfileTrigger> profileController = new MutableLiveData<>();

    //状态数据：添加好友的结果
    LiveData<DataState<Boolean>> makeFriendsResult;
    //Trigger：控制添加好友的请求
    MutableLiveData<ProfileTrigger> makeFriendsController = new MutableLiveData<>();


    /**
     * 仓库区
     */
    //用户资料仓库
    private ProfileRepository repository;
    //本地用户仓库
    private MeRepository meRepository;

    public ProfileViewModel() {
        repository = ProfileRepository.getInstance();
        meRepository = MeRepository.getInstance();
    }

    public LiveData<DataState<UserProfile>> getUserProfileLiveData() {
        if (profileLiveData == null) {
            profileLiveData = Transformations.switchMap(profileController, input -> {
                UserLocal user = meRepository.getLoggedInUserDirect();
                if (input.isActioning()) {
                    if (user.isValid()) {
                        //从用户资料仓库中拉取数据
                        return repository.getUserProfile(input.getId(), Objects.requireNonNull(user.getToken()));
                    }else{
                        return new MutableLiveData<>(new DataState<>(DataState.STATE.NOT_LOGGED_IN));
                    }
                }
                return new MutableLiveData<>();
            });
        }
        return profileLiveData;
    }

    public LiveData<DataState<Boolean>> getRelationLiveData(){
        if(relationLiveData==null){
            relationLiveData = Transformations.switchMap(profileController, input -> {
                UserLocal user = meRepository.getLoggedInUserDirect();
                if (input.isActioning()) {
                    if (user.isValid()) {
                        //通知用户资料仓库进行好友判别
                        return repository.isMyFriend(Objects.requireNonNull(user.getToken()),user.getId(),input.getId());
                    }else{
                        return new MutableLiveData<>(new DataState<>(DataState.STATE.NOT_LOGGED_IN));
                    }
                }
                return new MutableLiveData<>();
            });
        }
        return relationLiveData;
    }

    public LiveData<DataState<Boolean>> getMakeFriendsResult() {
        if(makeFriendsResult==null){
            makeFriendsResult = Transformations.switchMap(makeFriendsController, input -> {
                UserLocal user = meRepository.getLoggedInUserDirect();
                if (input.isActioning()) {
                    if (user.isValid()) {
                        //也是通过这个仓库进行好友建立
                        return repository.makeFriends(Objects.requireNonNull(user.getToken()),input.getId());
                    }else{
                        return new MutableLiveData<>(new DataState<>(DataState.STATE.NOT_LOGGED_IN));
                    }
                }
                return new MutableLiveData<>();
            });
        }
        return makeFriendsResult;
    }


    /**
     * 开始页面刷新
     * @param id 这个页面是谁的资料
     */
    public void startRefresh(String id){
        profileController.setValue(ProfileTrigger.getActioning(id));
    }

    /**
     * 开始建立朋友关系
     * @param id 这个页面是谁的
     */
    public void startMakingFriends(String id){
        makeFriendsController.setValue(ProfileTrigger.getActioning(id));
    }
}
