package net.suntrans.ebuilding.api;

import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.bean.Ammeter3Eneity;
import net.suntrans.ebuilding.bean.AreaDetailEntity;
import net.suntrans.ebuilding.bean.AreaEntity;
import net.suntrans.ebuilding.bean.ChangedPasswordEntity;
import net.suntrans.ebuilding.bean.ControlEntity;
import net.suntrans.ebuilding.bean.DeviceEntity;
import net.suntrans.ebuilding.bean.DeviceInfoResult;
import net.suntrans.ebuilding.bean.EnergyEntity;
import net.suntrans.ebuilding.bean.EnergyUsedEntity;
import net.suntrans.ebuilding.bean.EnvDetailEntity;
import net.suntrans.ebuilding.bean.LoginResult;
import net.suntrans.ebuilding.bean.SceneChannelResult;
import net.suntrans.ebuilding.bean.SceneEntity;
import net.suntrans.ebuilding.bean.SensusEntity;
import net.suntrans.ebuilding.bean.UserInfo;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Looney on 2017/1/4.
 */

public interface Api {

    /**
     * 登录api
     *
     * @param grant_type    默认填password
     * @param client_id     默认填6
     * @param client_secret 默认填test
     * @param username      账号
     * @param password      密码
     * @return
     */
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<LoginResult> login(@Field("grant_type") String grant_type,
                                  @Field("client_id") String client_id,
                                  @Field("client_secret") String client_secret,
                                  @Field("username") String username,
                                  @Field("password") String password);

    @POST("home/scene")
    Observable<SceneEntity> getHomeScene();

    @FormUrlEncoded
    @POST("scene/show")
    Observable<SceneChannelResult> getSceneChannel(@Field("id") String id);

    @FormUrlEncoded
    @POST("switch/channel")
    Observable<ControlEntity> switchChannel(@Field("id") String id,
                                            @Field("datapoint") String datapoint,
                                            @Field("din") String din,
                                            @Field("cmd") String cmd);


    @POST("home/light")
    Observable<DeviceEntity> getAllDevice();

    @FormUrlEncoded
    @POST("switch/scene")
    Observable<ControlEntity> switchScene(@Field("id") String id);


    @POST("house/index")
    Observable<AreaEntity> getHomeHouse();

    @FormUrlEncoded
    @POST("house/area")
    Observable<AreaDetailEntity> getRoomChannel(@Field("id") String id);

    @FormUrlEncoded
    @POST("energy/ammeter3")
    Observable<Ammeter3Eneity> getAmmeter3Detail(@Field("sno") String sno);

    @POST("energy/index")
    Observable<EnergyEntity> getEnergyIndex();

    @POST("home/sensus")
    Observable<SensusEntity> getHomeSceneNew();


    @POST("device/index")
    Observable<DeviceInfoResult> getDevicesInfo();

    @FormUrlEncoded
    @POST("sensus/show")
    Observable<EnvDetailEntity> getEnvDetail(@Field("din") String din);


    @POST("user/info")
    Observable<UserInfo> getUserInfo();


    @FormUrlEncoded
    @POST("user/password")
    Observable<ChangedPasswordEntity> changedPassword(@Field("oldpassword") String oldPassword,
                                                      @Field("newpassword") String newPassword);

    @FormUrlEncoded
    @POST("user/guestbook")
    Observable<ChangedPasswordEntity> commitGusetBook(@Field("contents") String oldPassword);


    @FormUrlEncoded
    @POST("user/info")
    Observable<EnergyUsedEntity> getEnergyUsed(@Field("time") String date,@Field("type") String type);

    @FormUrlEncoded
    @POST("scene/add")
    Observable<SampleResult> addScene(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("scene/delete")
    Observable<SampleResult> deleteScene(@Field("id") String id);

    @FormUrlEncoded
    @POST("scene/update")
    Observable<SampleResult> deleteScene(@FieldMap Map<String,String> map);
}
