package com.example.daoyun09.http;


import android.support.annotation.NonNull;

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

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Part;

public class HttpUtil extends HttpBase {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static Retrofit mRetrofit;

    /**
     * 初始化Retrofit
     *
     * @return
     */
    @NonNull
    private static Retrofit init() {
        if (mRetrofit != null) return mRetrofit;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(HTTP_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit;
    }

    @NonNull
    private static Retrofit init2(String url) {
        if (mRetrofit != null) return mRetrofit;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit;
    }

    /**
     * 用户模块 <==================================================================================>
     */

    public static void registerUser(Map<String, String> params, BaseObserver<RegisterBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpRegisterInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void FastregisterUser(String tel, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpFastRegisterInterface(tel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void QQregisterUser(String nickName, String image, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpQQRegisterInterface(nickName,image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getUid(String url, BaseObserver<String> callback) {
        Retrofit retrofit = init2(url);
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.getUid()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }


    public static void login(String account,String password, BaseObserver<ServerResponse<loginByPasswordVo>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpLoginInterface(account,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void updatePwd(String newPassword,String oldPassword,String repeatNewPassword,String telephone, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpupdatePwdInterface(newPassword,oldPassword,repeatNewPassword,telephone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getUserInfo(String account, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetUserInfoInterface(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getHistoryInfo(String code, BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetHistoryInterface(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    //发送邮件
    public static void sendEmail(String email, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpSendEmailInterface(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void sendTelCode(String tel, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpSendTelCodeInterface(tel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    //修改个人信息
    public static void UpdateUserInfo(String SchoolCode,String birth,String image,String name, String nickname,
                                      int role_id,int sex,String sno,String telephone, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpUpdateUserInfoInterface(SchoolCode,birth,image,name, nickname,role_id, sex,sno,telephone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    //获取学校院系
    public static void getSchoolInfo(String code, BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetSchoolInfoInterface(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 获取系统参数
     * @param params
     * @param callback
     */
    public static void getSystemInfo(Map<String, String> params, BaseObserver<SystemBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetSystemInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
    /**
     * 上传头像，这个后台还有问题，先不做了
     *
     * @param params
     * @param fileUrl  图片url
     * @param callback
     */
    public static void uploadAvatarInfo(Map<String, String> params, String fileUrl, BaseObserver<UploadAvatarBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);

        File file = new File(fileUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        service.httpUploadAvatarInterface(params, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 课程模块  <=============================================================================>
     */

    public static void addCourse(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpAddCourseInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getStudentsList(String course_id, BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetStudentsListInterface(course_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void searchCourse(Map<String, String> params, BaseObserver<SearchListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpSearchCourseInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void hascreateCourse(String tel, BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httphasCreatedCourseInterface(tel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void hasJoinCourse(String tel, BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httphasJoinCourseInterface(tel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getSchools(BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httphschoolsInterface()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getDepartment(String code,BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httphgetDepartmentInterface(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getCourseInfo(String course_id, BaseObserver<JSONObject> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetCourseInfoInterface(course_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyCourseInfo(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyCourseInfoInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void deleteStudent(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpDeleteStudentInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void deleteCheck(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpDeleteCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyStudent(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyStudentInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyCheck(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void addStu2Course(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpAddStu2CourseInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getCoursesList(Map<String, String> params, BaseObserver<CoursesListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetCoursesListInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    //加入班课
    public static void JoinCourse(String code, String telephone,BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpJoinCourseInterface(code,telephone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    //老师删除班课
    public static void Teacher_delCourse(String code, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpTeacher_delCourseInterface(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    //学生退出班课
    public static void Stu_delCourse(String code, String telephone,BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpStu_delCourseInterface(code,telephone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void createCourse(String className, String examination, int isFinish, String name, String process,
                                    String require, String school, String telephone,String term, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpCreateCourseInterface(className,examination,isFinish,name,process,require,school,telephone,term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void updateCourse(String className, String code,String examination, int isFinish,int isJoin, String name, String process,
                                    String require, String school,String term, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpUpdateCourseInterface(className,code,examination,isFinish,isJoin,name,process,require,school,term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 签到模块  <=============================================================================>
     */

    public static void check(int attendance_type,String code, int count, String end_time, double latitude, double longitude,
                             String start_time, String telephone, BaseObserver<Integer> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpCheckInterface(attendance_type,code, count, end_time,latitude, longitude, start_time,telephone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    public static void stuCheck(String attend_time,String code, double latitude, double longitude, String telephone, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpStu_CheckInterface(attend_time,code,latitude, longitude,telephone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void startCheck(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpStartCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void stopCheck(String code, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpStopCheckInterface(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void canCheck(String code, BaseObserver<String> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpCanCheckInterface(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 数据字典模块  <=============================================================================>
     */
    public static void getDictInfo(String token, String typename, BaseObserver<DictInfoListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetDictInfoInterface(token, typename)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
}
