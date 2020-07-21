## 写在前面
做了一段时间的外包项目，碍于难以保证历史遗留代码的正确性及技术栈更新问题，在保证各项目正常开发的同时，进行了基础功能的抽离和系统架构的重构，整理出一般项目MVP基本模板，文档旨在描述模板项目各类作用，包含基本抽象化MVP模块、各版本适配、常用第三方框架、常用工具类，项目地址：[https://github.com/TinloneX/BaseNormalMVP](https://github.com/TinloneX/BaseNormalMVP)，看完文档有兴趣的可以尝试下载使用，此模板适合一般性项目，使用时直接clone导入并更改包名，删除*/.git*缓存目录，修改gradle部分配置即可。
## 项目配置说明
项目主要开发语言为Java，编译工具使用Gradle，主要使用RxJava、Retrofit、Glide、ButterKnife及其他优秀的开源框架。

***TODO:文档补充*

**Java：**
由于大量使用便利的lambda表达式，故jdk版本应为**1.8+**，同时项目编译版本指定为Java1.8：

```Groovy
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```
**Gradle：** 
选用编译速度和脚本更简约高效的 ***3.2.+*** 版本：

```Groovy
dependencies {
    classpath 'com.android.tools.build:gradle:3.2.1'
}
```
同时，建议适配版本为Android4.4以上，多数工具类已适配至API28：

```Groovy
defaultConfig {
    applicationId "com.company.project"
    minSdkVersion 19
    targetSdkVersion 28
    versionCode 100
    versionName "1.0.0"
    multiDexEnabled true
    buildConfigField "String", "APP_NAME", '"application"'
}
```
## 模块介绍
### MVP部分
在保证功能正常使用同时为了MVP结构的可阅读性，建议使用契约类去包含MVP的接口类，类似的：

```Java
import com.company.project.base.BaseResponse;
import com.company.project.base.IPresenter;
import com.company.project.bean.AdvertisementBean;
import com.company.project.mvp.IModel;
import com.company.project.mvp.IView;

import java.util.HashMap;

/**
 * @author Tinlone
 * @date 2018/3/23.
 */
public interface AdvertisementContract {

    interface IAdvertisementView extends IView<AdvertisementBean> {

    }

    interface IAdvertisementPresenter extends IPresenter<IAdvertisementView> {
        /**
         * 获取启动页广告
         */
        void getAdvertisement();
    }

    interface IAdvertisementModel extends IModel {

        /**
         * 获取广告
         *
         * @param params   参数
         * @param callBack 回调
         */
        void getAdvertisement(HashMap<String, Object> params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack);
    }
}
```
#### View层接口
简单分析此类，MVP通用逻辑抽离为顶级接口类。特别的，为简化View界面只有一类数据请求的，View层使用泛型约定Data类型，使得View层仅需定义接口，无需额外申明响应方法：

```Java
interface IAdvertisementView extends IView<AdvertisementBean> {

}
```
此接口实际包含内容为：

```Java
public interface IView<DATA> {
    /**
     * 获取到数据
     *
     * @param resultData 数据
     */
    void onLoadData(DATA resultData);

    /**
     * 加载数据失败
     *
     * @param resultMsg  失败返回信息
     * @param resultCode 失败返回码
     */
    void onLoadFail(String resultMsg, String resultCode);
}
```
如若View层有多请求多数据类型，则可在View接口层声明新的响应即可，类似的：
```Java
interface IAdvertisementView extends IView<AdvertisementBean> {
    void onBookListResponse(List<Book> books);
    void onListLoadFail(String code, String msg);
}
```
此时，View层即可响应AdvertisementBean的数据也可以响应Book的数据，当然抽象的同时会影响对于AdvertisementBean响应的可读性（没有显式的声明响应方法）。
#### Presenter层接口
Presenter接口声明接口调用，提供给View层调用：

```Java
interface IAdvertisementPresenter extends IPresenter<IAdvertisementView> {
    /**
     * 获取启动页广告
     */
    void getAdvertisement();
}
```
IPresenter指定对应View层接口的泛型，Presenter层顶级接口包含以下内容：

```Java
public interface IPresenter<V extends IView> {

    /**
     * 绑定V层
     *
     * @param view view
     */
    void attachView(V view);

    /**
     * 解绑V
     */
    void dettachView();
}
```
其中绑定和解绑View的实现将有Presenter公共基类BasePresenter实现，以**保证View层基类在生成界面是绑定Presenter及销毁界面前解绑Presenter，防止可能发生的内存泄漏和空指针异常。** 基类实现将在后续介绍。
#### Model层接口
声明了业务请求必须实现的方法：

```Java
interface IAdvertisementModel extends IModel {

    /**
     * 获取广告
     * @param params   参数
     * @param callBack 回调
     */
    void getAdvertisement(HashMap<String, Object> params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack);
}
```
方法入参包含接口参数部分*params* 及观察者回调*callback* ，其中callback约定返回解析数据类型，Callback被定义在IModel接口中：

```Java
public interface IModel {

    /**
     * 取消请求
     */
    void cancelRequest();

    interface AsyncCallBack<Data> {

        /**
         * 成功
         * @param resultData 数据
         */
        void onSuccess(Data resultData);

        /**
         * 失败
         * @param resultMsg  信息
         * @param resultCode 错误码
         */
        void onFailed(String resultMsg, String resultCode);
    }
}
```
Model接口中定义取消请求的方法，用于防止Presenter与View解绑后无意义的响应回调，其具体逻辑由Model层基类BaseModel实现。
#### MVP基类浅析之BaseActivity

```Java
public abstract class BaseActivity<P extends IPresenter, DATA> extends AppCompatActivity implements IView<DATA> {
    
}
```
BaseAcitvity的继承链hin长hin长，我也hin无奈。

onCreate()中内容：

```Java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layoutId());
    initImmersionBar();
    unbinder = ButterKnife.bind(this);
    mPresenter = getPresenter();
    if (mPresenter != null) {
        // 谁能帮我解决下类型问题~！
        mPresenter.attachView(this);
    }
    initView();
    initData();
}
```
主要完成了一下事务：
- 通过抽象方法*layoutId()* 获取界面xml的id；
- 初始化沉浸式状态栏框架及相关配置；
- 注册ButterKnife；
- 通过抽象方法*getPresenter()* 获取Presenter具体实现，将Presenter层插拔操作下放到子类；
- Presenter层绑定View对象
- 控件/数据请求初始化等操作（欢迎尝试置换先后顺序，会有有意思的发现哦）。

View接口的实现：

```Java
/**
 * 成功响应
 *
 * @param resultData 数据
 */
@Override
public void onLoadData(DATA resultData) {
    hideLoading();
}

/**
 * 失败响应
 *
 * @param resultMsg  失败返回信息
 * @param resultCode 失败返回码
 */
@Override
public void onLoadFail(String resultMsg, String resultCode) {
    hideLoading();
    ToastUtils.showShort(resultMsg);
}
```
一般的统一处理失败响应，关闭加载动画并提示错误信息，如需自行处理不同的错误重写OnLoadFail()即可；成功响应仅包含关闭动画，集体数据处理逻辑须各View处理，由于不是所有界面都有网络请求，故未做强制重写（声明abstract）；

界面跳转：

```Java
/**
 * 打开新界面
 *
 * @param clazz  界面类
 * @param bundle 数据
 */
public void startActivity(@NonNull Class<? extends BaseActivity> clazz, @Nullable Bundle bundle, boolean right) {
    if (noDoubleClick()) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        lastClick = System.currentTimeMillis();
        startActivity(intent);
        if (right) {
            rightStart();
        } else {
            leftStart();
        }
    }
}
```
特别的，做了防止抖动连点打开多个界面的判断，及打开界面转场动画的修改（部分手机无效，有解决办法的，希望大佬们给意见），防抖间隔时间可通过配置文件同意修改。

onDestory():

```Java
@Override
protected void onDestroy() {
    if (mPresenter != null) {
        mPresenter.dettachView();
    }
    if (unbinder != null) {
        unbinder.unbind();
        unbinder = null;
    }
    lastClick = 0L;
    super.onDestroy();
}
```
默认的，在界面销毁时我们做了Presenter层与View层的解绑及ButterKnife的解绑操作！
BaseFragment与BaseActivity类似，此处不做赘述。
#### MVP基类浅析之BasePresenter

```Java
public abstract class BasePresenter<V extends IView, M extends IModel> implements IPresenter<V> {
}
```
仅指定顶级类泛型，做到View和Model可插拔，Presenter层可理解为Model层的工厂类及View层调度类：

```Java
protected M mModel = null;
protected V mView = null;
@Override
public void attachView(V view) {
    mView = view;
    setModel();
}
public abstract void setModel();
```
在View层绑定的同时获取Model对象，Model对象的插拔由子类实现；如果你的请求有公共参数，你可能会用到以下方法：

```Java
private HashMap<String, Object> baseParams;
protected HashMap<String, Object> getBaseParams() {
    if (baseParams == null) {
        baseParams = new HashMap<>(16);
    } else {
        baseParams.clear();
    }
    if (UserInfoUtil.isLogin()) {
        baseParams.put("token", UserInfoUtil.getUserInfo().getToken());
        baseParams.put("userId", UserInfoUtil.getUserInfo().getUserId());
    }
    baseParams.put("from", "android");
    baseParams.put("version", BuildConfig.VERSION_CODE);
    return baseParams;
}
```
例如我的项目一般会默认传递应用版本号和来源等信息，此部分请根据各自项目灵活修改（非必须使用）。
在解绑View时，一般的，我选择取消请求，如若不需要取消，请重写以下方法：

```
@Override
public void dettachView() {
    if (mModel != null) {
        mModel.cancelRequest();
        mModel = null;
    }
    mView = null;
}
```
重写建议仅将View置空。我们为了更愉(lan)快(duo)的处理响应，定义了简化版的响应处理：

```Java
protected abstract class BaseAsyncCallback<Data> implements IModel.AsyncCallBack<Data> {

    @Override
    public abstract void onSuccess(Data resultData);

    @Override
    public void onFailed(String resultMsg, String resultCode) {
        if (mView != null) {
            mView.onLoadFail(resultMsg, resultCode);
        }
    }
}
```
统一下发失败的响应，仅下放成功响应给子类，使用时，一般为以下情形：

```Java
/**
 * Presenter层完整实现
 */
public class AdvertisementPresenter extends BasePresenter<AdvertisementContract.IAdvertisementView, AdvertisementContract.IAdvertisementModel> implements AdvertisementContract.IAdvertisementPresenter {
    @Override
    public void setModel() {
        mModel = new AdvertisementModel();
    }
    @Override
    public void getAdvertisement() {
        mModel.getAdvertisement(getBaseParams(), new BaseAsyncCallback<BaseResponse<AdvertisementBean>>() {
            @Override
            public void onSuccess(BaseResponse<AdvertisementBean> resultData) {
                if (Check.hasContent(resultData, mView)) {
                    mView.onLoadData(resultData.getResultData());
                }
            }
        });
    }
}
```
配置Model实现，实现数据请求业务，由于存在View被释放的可能，故在向View反馈响应时建议做空判断，一般仅需判断mView不为空即可，仅result中data为对象或集合时可使用Check.hasContent()方法，否则响应可能会被拦截，原因详见hasContent()的实现。
#### MVP基类浅析之BaseModel实现
在Model的实现中，我们常常使用一句话即可完成数据请求，类似的：

```Java
bindObservable(mService.getAdvertisement(params), callBack);
```
这得益于BaseModel对于观察者模式绑定的高度抽离：

```Java
protected HttpService mService = HttpClient.getInstance().getApiService();
private SparseArray<Observable> queue = null;
private int index = 0;

protected <T> void bindObservable(@NonNull Observable<BaseResponse<T>> call, @NonNull AsyncCallBack<BaseResponse<T>> callBack) {
    if (queue == null) {
        queue = new SparseArray<>();
    }
    queue.append(index, call);
    index++;
    call.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(HttpObserver.getInstance().createObserver(callBack));
}
```
主要完成了一下事务：
- 记录网络请求队列；
- 绑定观察者与被观察者。

对应的，BaseModel同样也有解除绑定的实现：

```Java
@Override
@SuppressLint("CheckResult")
public void cancelRequest() {
    if (queue != null && queue.size() > 0) {
        for (int i = 0; i < queue.size(); i++) {
            TLog.i(queue.get(i).unsubscribeOn(AndroidSchedulers.mainThread()));
        }
        queue.clear();
        index = 0;
    }
}
```
此处并非正是意义上的取消网络请求，以防部分提交数据的请求，但是阻断了回调响应流程，防止内存泄漏及可能存在的空指针。
### 常用功能类介绍
#### BaseResponse
RESTFUL-API解析基类：

```
public class BaseResponse<T> {
    /**
     * resultCode : 0
     * message : “”
     * resultData：{}
     */
    @SerializedName("resultCode")
    private String resultCode;
    @SerializedName("resultData")
    private T resultData;
    @SerializedName("message")
    private String message;
    //... setter / getter / toString 
}
```
注意，须根据实际数据修改Gson解析映射名 *@SerializedName("字段名")* 。
#### CollectionConfig / Config
个人习惯，集合类的配置信息存放于CollectionConfig中，其他各类型配置信息存放于Config中，杜绝魔法值，做到一处修改全局更改。
#### HttpClient
Retrofit HttpClient 配置类，主要用于配置全局单例HttpClient及各类拦截器，另有方便测试的一键更改全局IP的方法。
#### HttpObserver
用于将传入Model层的View层回调生成对应类型的观察者对象，核心代码为：

```Java
public <T> Observer<BaseResponse<T>> createObserver(final IModel.AsyncCallBack<BaseResponse<T>> callBack) {
    return new Observer<BaseResponse<T>>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(BaseResponse<T> response) {
            // 此处统一处理网络请求状态
            if (Config.Strings.RESPONSE_OK.equals(response.getResultCode())) {
                callBack.onSuccess(response);
            } else {
                callBack.onFailed(response.getMessage(), response.getResultCode());
            }
            TLog.i("tag", "(HttpObserver.java:45) ~ onNext:" + response.toString());
        }

        @Override
        public void onError(Throwable e) {
            callBack.onFailed(e.getMessage(), "-1");
        }

        @Override
        public void onComplete() {
        }
    };
}
```
##### HttpService
Retrofit 的 RestApiService接口文件，用于声明个请求接口，类似：

```Java
public interface HttpService {
    /**
     * 手机APP开屏页
     * from 	string 	是 	（安卓：android；水果：iOS）
     * type 	string 	是
     */
    @GET("openscreen/pic")
    Observable<BaseResponse<AdvertisementBean>> getAdvertisement(@QueryMap() Map<String, Object> params);

}
```
#### ODownloadService/OpenFileReceiver/OnDownloadListener
ODownloadService：Bind方式绑定下载服务，支持进度回调OnDownloadListener，打开各类文件，OpenFileReceiver适配各版本打开各文件及安装apk。
#### FileSizeUtil/FileUtils/MyFileProvider
文件相关工具类：FileSizeUtil文件尺寸计算工具类，FileUtils代开文件相关工具类，MyFileProvider
#### MyApplication

```Java
public class MyApplication extends Application {

    private static MyApplication mContext;

    public static MyApplication getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        dealUncaughtException();
        registerReceiver(new OpenFileReceiver(), new IntentFilter(BuildConfig.APPLICATION_ID + ".open_file"));
    }

    private void dealUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            Intent intent = new Intent(mContext, LauncherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        });
    }
}
```
### 三方框架引用

```Groovy
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support:support-v4:28.0.0'
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    //retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.4.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
    //rxjava
    implementation 'io.reactivex.rxjava2:rxjava:2.2.3'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
    //glide
    implementation 'com.github.bumptech.glide:glide:4.7.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.6.1'
    //防止三方框架爆包
    implementation 'com.android.support:multidex:1.0.3'
    // util集合
    implementation 'com.blankj:utilcode:1.21.2'
    //    权限
    implementation 'com.github.jokermonn:permissions4m:2.1.2-lib'
    annotationProcessor 'com.github.jokermonn:permissions4m:2.1.2-processor'
    // 沉浸式状态栏
    implementation 'com.gyf.immersionbar:immersionbar:2.3.2-beta01'
    // 底部导航栏
    implementation 'com.github.chaychan:BottomBarLayout:1.1.2'
    // 黄油🔪
    implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    // 快捷BaseAdapter
    implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.40'
    // logging
    implementation 'com.squareup.okhttp3:logging-interceptor:3.9.0'
    // Snacky
    implementation 'com.github.matecode:Snacky:1.0.2'
}
```


