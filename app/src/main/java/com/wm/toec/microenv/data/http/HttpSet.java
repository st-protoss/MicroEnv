package com.wm.toec.microenv.data.http;

import com.wm.toec.microenv.app.App;
import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.util.CheckNetwork;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wm on 2018/6/6.
 * Http封装类
 */

public class HttpSet {
    private Retrofit mRetrofit = null;
    private OkHttpClient mOkHttpClient = null;

    private static class HttpHoler{
        private static HttpSet INSTANCE = new HttpSet();
    }
    private HttpSet(){
        init();
    }

    public static HttpSet getInstance(){
        return HttpHoler.INSTANCE;
    }
    /**
     * 初始化单例的HttpSet
     */
    private void init(){
        initOkHttp();
        initRetrofit();
    }

    /**
     * 供外部调用的创建Observable的方法
     * @param tClass 泛型参数类
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> tClass){
        return mRetrofit.create(tClass);
    }

    /**
     * 供外部直接调用返回网络接口方法
     * @return
     */
    public IToecServerApi getToecServer(){
        return mRetrofit.create(IToecServerApi.class);
    }
    /**
     * 初始化retrofit
     */
    private void initRetrofit(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.TOEC_SERVER_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    /**
     * 初始化okhttp
     */
    private void initOkHttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(Constants.NET_CACHE_URL);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!CheckNetwork.isNetworkConnected(App.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            Response.Builder newBuilder = response.newBuilder();
            //加入响应头中对于内容的要求 为json utf-8
            newBuilder.addHeader("Content-Type","application/json;charset=utf-8");
            if (CheckNetwork.isNetworkConnected(App.getAppContext())) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                newBuilder.addHeader("Cache-Control", "public, max-age=" + maxAge);

            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                newBuilder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return newBuilder.build();
        };
        builder.cache(cache);
        builder.addInterceptor(cacheInterceptor);
        if (Constants.DEBUG) {
            //builder.addNetworkInterceptor(new StethoInterceptor());
        }
        //设置超时
        builder.connectTimeout(2, TimeUnit.SECONDS);
        builder.readTimeout(2, TimeUnit.SECONDS);
        builder.writeTimeout(2, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        mOkHttpClient = builder.build();
    }
}
