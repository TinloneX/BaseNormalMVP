package com.company.project.http;


import com.company.project.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.company.project.MyApplication;
import com.company.project.config.Config;
import com.company.project.util.SharedPreferencesUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 13265969699
 * 123456abc
 *
 * @author Administrator
 */

public class HttpClient {
    private static HttpClient mRetrofitHttp;
    /**
     * 发现界面有入口,打包时关闭
     * 充值界面生产环境 RechargeActivity
     * DiscoverFragment 后台窗口关闭tv_times
     */
    private String BASE_URL = Config.BaseUrls.BASE_URL;
    private Retrofit retrofit;

    private HttpClient() {

    }

    public static HttpClient getInstance() {
        if (mRetrofitHttp == null) {
            synchronized (HttpClient.class) {
                if (mRetrofitHttp == null) {
                    mRetrofitHttp = new HttpClient();
                }
            }
        }
        return mRetrofitHttp;
    }


    private void initRetrofit() {
        if (retrofit == null) {
            //缓存
            File cacheFile = new File(MyApplication.getAppContext().getCacheDir(), "cache");
            //100Mb
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
            //增加头部信息
            Interceptor headerInterceptor = chain -> {
                Request.Builder builder = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json");

                String cookies[] = ((String) SharedPreferencesUtil.getParam(MyApplication.getAppContext(), "cookies", "")).split("-");
                for (String cookie : cookies) {
                    builder.addHeader("Cookie", cookie);
                }

                Request request = builder.build();
                return chain.proceed(request);
            };

            //增加cookie信息
            Interceptor cookieInterceptor = chain -> {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (String cookie : originalResponse.headers("Set-Cookie")) {
                        sb.append(cookie).append("-");
                    }
                    SharedPreferencesUtil.setParam(MyApplication.getAppContext(), "cookies", sb.length() > 0 ? sb.subSequence(0, sb.length() - 1).toString() : "");
                }

                return originalResponse;
            };
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.addInterceptor(headerInterceptor);
            builder.addInterceptor(cookieInterceptor);
            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(new StethoInterceptor());
            }
            builder.cache(cache);

            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(builder.build())
                    .build();
        }
    }

    /**
     * 方便测试期间后门更改 IP : port
     */
    public static void updateUrl() {
        getInstance().retrofit = null;
        getInstance().initRetrofit();
    }

    public HttpService getApiService() {
        initRetrofit();
        return retrofit.create(HttpService.class);
    }

    private String getBaseUrl() {
        return BASE_URL == null ? Config.BaseUrls.BASE_URL : BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
        retrofit = null;
    }
}
