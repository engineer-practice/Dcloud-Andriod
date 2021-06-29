package com.example.daoyun09.http;

import com.alibaba.fastjson.JSONObject;
import com.example.daoyun09.dto.CourseDto;
import com.example.daoyun09.entity.ServerResponse;
import com.example.daoyun09.entity.User;
import com.example.daoyun09.entity.loginByPasswordVo;
import com.example.daoyun09.httpBean.CourseInfoBean;
import com.example.daoyun09.httpBean.CoursesListBean;
import com.example.daoyun09.httpBean.DefaultResultBean;
import com.example.daoyun09.httpBean.DictInfoListBean;
import com.example.daoyun09.httpBean.LoginBean;
import com.example.daoyun09.httpBean.RegisterBean;
import com.example.daoyun09.httpBean.SearchListBean;
import com.example.daoyun09.httpBean.StudentsListBean;
import com.example.daoyun09.httpBean.SystemBean;
import com.example.daoyun09.httpBean.UploadAvatarBean;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {


    /**
     * 注册接口
     *
     * @param params 手机号和密码
     * @return 是否成功
     */
    @Multipart
    @POST("user/register")
    Observable<RegisterBean> httpRegisterInterface(@PartMap Map<String, String> params);

    @Multipart
    @POST("registerByMessage")
    Observable<String> httpFastRegisterInterface(@Part("telephone") String tel);

    @Multipart
    @POST("registerByQQnumber")
    Observable<String> httpQQRegisterInterface(@Part("nickname") String nicknme,@Part("image") String image);

    @Multipart
    @GET
    Observable<String> getUid();

    /**
     * 登录接口
     *
     * @return
     */
    @Multipart
    @POST("loginByPassword")
    Observable<ServerResponse<loginByPasswordVo>> httpLoginInterface(@Part("account") String account, @Part("password") String password);

    /**
     * 修改密码接口
     * @return
     */
    @Multipart
    @POST("user/updatePassword1")
    Observable<String> httpupdatePwdInterface(@Part("newPassword") String newPassword,@Part("oldPassword") String oldPassword,
                                              @Part("repeatNewPassword") String repeatNewPassword,@Part("telephone") String telephone);

    @Multipart
    @PUT("user/info1")
    Observable<String> httpUpdateUserInfoInterface(@Part("SchoolCode") String SchoolCode,@Part("birth") String birth,@Part("image") String image,@Part("name") String name,
                                                   @Part("nickname") String nickname,@Part("role_id") int role_id,@Part("sex") int sex,@Part("sno") String sno,@Part("telephone") String telephone);

    @GET("user/info")
    Observable<String> httpGetUserInfoInterface(@Query("account") String account);

    @GET("schools/getCode1")
    Observable<JSONObject> httpGetSchoolInfoInterface(@Query("code") String code);

    @Multipart
    @PUT("attendence/historyAttendence")
    Observable<JSONObject> httpGetHistoryInterface(@Part("code") String code);

    /**
     * 查看系统信息
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("system/infos")
    Observable<SystemBean> httpGetSystemInfo(@PartMap Map<String, String> params);
    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @Multipart
    @POST("sendCode")
    Observable<String> httpSendEmailInterface(@Part("email") String email);

    /**
     * 发送短信
     * @return
     */
    @Multipart
    @POST("sendMessage")
    Observable<String> httpSendTelCodeInterface(@Part("telephone") String email);

    /**
     * 上传头像
     * @param params
     * @param file
     * @return
     */
    @Multipart
    @POST("user/avatar")
    Observable<UploadAvatarBean> httpUploadAvatarInterface(@PartMap Map<String, String> params, @Part MultipartBody.Part file);

    /**
     * 学生添加课程到课表
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("course/add")
    Observable<DefaultResultBean<Object>> httpAddCourseInterface(@PartMap Map<String, String> params);

    /**
     * 获取课程学生列表
     *
     * @return
     */
    @GET("courses/getMember")
    Observable<JSONObject> httpGetStudentsListInterface(@Query("code") String code);


    /**
     * 搜索课程
     *
     * @return
     */
    @GET("course/search")
    Observable<SearchListBean> httpSearchCourseInterface(@QueryMap Map<String, String> params);

    @GET("courses/hasCreated")
    Observable<JSONObject> httphasCreatedCourseInterface(@Query("telephone") String telephone);

    @GET("courses/hasJoined")
    Observable<JSONObject> httphasJoinCourseInterface(@Query("telephone") String telephone);

    @GET("schools/getSchools")
    Observable<JSONObject> httphschoolsInterface();

    @GET("schools/getAcademies")
    Observable<JSONObject> httphgetDepartmentInterface(@Query("schoolCode") String schoolCode);

    /**
     * 获取课程信息
     * @return
     */
    @GET("courses/courseInfo")
    Observable<JSONObject> httpGetCourseInfoInterface(@Query("code") String code);

    /**
     * 修改课程信息
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("course/info")
    Observable<DefaultResultBean<Object>> httpModifyCourseInfoInterface(@PartMap Map<String, String> params);

    /**
     * 从课程删除学生
     *
     * @param params
     * @return
     */
    @Multipart
    @DELETE("course/students")
    Observable<DefaultResultBean<Object>> httpDeleteStudentInterface(@PartMap Map<String, String> params);

    /**
     * 删除签到信息
     *
     * @param params
     * @return
     */
    @Multipart
    @DELETE("course/checklist")
    Observable<DefaultResultBean<Object>> httpDeleteCheckInterface(@PartMap Map<String, String> params);

    /**
     * 修改学生信息
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("course/students")
    Observable<DefaultResultBean<Object>> httpModifyStudentInterface(@PartMap Map<String, String> params);

    /**
     * 修改签到信息
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("course/checklist")
    Observable<DefaultResultBean<Object>> httpModifyCheckInterface(@PartMap Map<String, String> params);

    /**
     * 添加学生到课程
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("course/stu2course")
    Observable<DefaultResultBean<Object>> httpAddStu2CourseInterface(@PartMap Map<String, String> params);

    /**
     * 获取课程列表
     *
     * @param params
     * @return
     */
    @GET("course/course")
    Observable<CoursesListBean> httpGetCoursesListInterface(@QueryMap Map<String, String> params);

    @Multipart
    @POST("courses")
    Observable<String> httpJoinCourseInterface(@Part("code") String code, @Part("telephone") String telephone);

    @Multipart
    @HTTP(method = "DELETE", path = "courses", hasBody = true)
    Observable<String> httpTeacher_delCourseInterface(@Part("code") String code);

    @Multipart
    @HTTP(method = "DELETE", path = "courses", hasBody = true)
    Observable<String> httpStu_delCourseInterface(@Part("code") String code, @Part("telephone") String telephone);

    /**
     * 创建课程
     */
    @Multipart
    @POST("courses/CreateClass")
    Observable<String> httpCreateCourseInterface(@Part("className") String className, @Part("examination") String examination, @Part("isFinish") int isFinish, @Part("name") String name,
                                                 @Part("process") String process, @Part("require") String require, @Part("school") String school, @Part("telephone") String telephone, @Part("term") String term);

    @Multipart
    @PATCH("courses")
    Observable<String> httpUpdateCourseInterface(@Part("className") String className, @Part("code") String code,@Part("examination") String examination, @Part("isFinish") int isFinish, @Part("isJoin") int isJoin,
                                                 @Part("name") String name, @Part("process") String process, @Part("require") String require, @Part("school") String school,  @Part("term") String term);


    /**
     * 创建签到
     * @return
     */
    @Multipart
    @POST("attendence/createAttendence")
    Observable<Integer> httpCheckInterface(@Part("attendance_type") int attendance_type, @Part("code") String code, @Part("count") int count, @Part("end_time") String end_time,
                                                              @Part("latitude") double latitude, @Part("longitude") double longitude, @Part("start_time") String start_time, @Part("telephone") String telephone);


    /**
     * 学生签到
     * @return
     */
    @Multipart
    @PUT("attendence/checkIn")
    Observable<String> httpStu_CheckInterface(@Part("attend_time") String attend_time, @Part("code") String code, @Part("latitude") double latitude, @Part("longitude") double longitude,  @Part("telephone") String telephone);


    @Multipart
    @POST("check/startCheck")
    Observable<DefaultResultBean<Object>> httpStartCheckInterface(@PartMap Map<String, String> params);

    /**
     * 停止签到
     *
     */
    @Multipart
    @POST("attendence/end")
    Observable<String> httpStopCheckInterface(@Part("code") String code);

    /**
     * 查询是否能签到
     */
    @Multipart
    @POST("attendence/isEnd")
    Observable<String> httpCanCheckInterface(@Part("code") String code);

    /**
     * 获取字典内容
     *
     * @param token
     * @param typename
     * @return
     */
    @GET("dict/infos4name")
    Observable<DictInfoListBean> httpGetDictInfoInterface(@Query("token") String token, @Query("typename") String typename);

}
