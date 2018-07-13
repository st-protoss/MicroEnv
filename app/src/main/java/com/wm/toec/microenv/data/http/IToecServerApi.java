package com.wm.toec.microenv.data.http;

import android.databinding.ObservableField;

import com.wm.toec.microenv.bean.BindResultBean;
import com.wm.toec.microenv.bean.DeviceInfoBean;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.bean.LocationBean;
import com.wm.toec.microenv.bean.LoginBean;
import com.wm.toec.microenv.bean.MemberResultBean;
import com.wm.toec.microenv.bean.RegisterBean;
import com.wm.toec.microenv.bean.VerifyCodeBean;
import com.wm.toec.microenv.bean.VersionResultBean;
import com.wm.toec.microenv.bean.WearResultBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wm on 2018/6/6.
 * retrofit的服务器接口方法
 */

public interface IToecServerApi {
    //登录验证
    @FormUrlEncoded
    @POST("app/login")
    Observable<LoginBean> login(@Field("phone")String phone, @Field("password")String password);
    //获取验证码
    @GET("app/getVerifycode")
    Observable<VerifyCodeBean> getVerifyCode(@Query("phone")String phone);
    //新用户注册
    @FormUrlEncoded
    @POST("app/register")
    Observable<RegisterBean> register(@Field("phone")String phone, @Field("verifycode")String verifycode,
                                      @Field("password")String password);
    //获取该用户的设备信息
    @GET("app/getDeviceInfo")
    Observable<DeviceInfoBean> getDeviceInfo(@Query("userId")String userId);
    //绑定设备
    @GET("app/bindDevice")
    Observable<BindResultBean> bindDevice(@Query("userId")String userId, @Query("deviceId")String deviceId);
    //解绑设备
    @GET("app/unbindDevice")
    Observable<BindResultBean> unbindDevice(@Query("userId")String userId);
    //更新设备位置
    @GET("app/changeLocation")
    Observable<LocationBean> changeLocation(@Query("userId")String userId, @Query("location")String location);

    //获取当前用户下的家庭成员列表
    @GET("app/getFamilyList")
    Observable<List<FamilyMemberBean>> getFamilyList(@Query("userId")String userId);
    //删除某家庭成员
    @GET("app/deleteMember")
    Observable<MemberResultBean> deleteMember(@Query("userId")String userId, @Query("memberId")String memberId);
    //编辑某成员
    @FormUrlEncoded
    @POST("app/editMember")
    Observable<MemberResultBean> editMember(@Query("userId") String userId,@Field("name")String name, @Field("height")String height,
                                            @Field("weight")String weight, @Field("birthday")String birthday);
    //添加某成员
    @FormUrlEncoded
    @POST("app/addMember")
    Observable<MemberResultBean> addMember(@Query("userId") String userId,@Field("name")String name, @Field("height")String height,
                                            @Field("weight")String weight, @Field("birthday")String birthday);

    //获取某成员某月的签到情况
    @GET("app/getWearMonth")
    Observable<List<WearResultBean>> getMonthWearData(@Query("userId")String userId, @Query("memberId")String memberId,
                                        @Query("year")String year, @Query("month")String month);
    //获取某成员当日的各项穿戴状况
    @GET("app/getWearDay")
    Observable<WearResultBean> getDayWearData(@Query("userId")String userId, @Query("memberId")String memberId,
                                              @Query("year")String year, @Query("month")String month,@Query("day")String day);
    //上传某成员某日的穿戴情况
    @GET("app/uploadWearDay")
    Observable<Integer> uploadDayWearData(@Query("userId")String userId, @Query("memberId")String memberId,
                                          @Body WearResultBean bean);

    //检测更新
    @GET("app/checkVersion")
    Observable<VersionResultBean> checkVersion(@Query("currentVersion")String currentVersion);
}
