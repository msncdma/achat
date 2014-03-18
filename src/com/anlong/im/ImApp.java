package com.anlong.im;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.anlong.im.constant.Constant;
import com.anlong.im.constant.DataConstant;
import com.anlong.im.dao.db.DatabaseAdapter;
import com.anlong.im.dao.db.model.AppListDBInfo;
import com.anlong.im.dao.db.model.SystemParameterDBinfo;
import com.anlong.im.service.CoreService;
import com.anlong.im.ui.util.MsgHandleProxy;
import com.anlong.im.ui.util.SIPControl;
import com.anlong.im.util.ImLog;
import com.anlong.im.util.SharedPreferencesSave;
import com.anlong.im.vo.FaceInfo;
import com.anlong.im.vo.MessageInfo;
import com.anlong.imsghandle.vo.OnlineUser;
import com.anlong.imsghandle.vo.UserMessage;

/**
 * 
 * @Title: ImApp.java
 * @Package com.anlong.im
 * @Description: TODO Im 应用总类
 * @author anlong
 * @date 2013-4-27 上午9:27:05
 * @version V1.0
 * 
 *  
 */
public class ImApp extends Application {


	/**
	 * 是否退出快快，默认为false 没有退出;
	 */
	public static boolean hasExited = false;

	/**
	 * 是否同步历史记录
	 */
	private  boolean isSynchroHistroy= false;


	public  boolean isSynchroHistroy() {
		return isSynchroHistroy;
	}



	public  void setSynchroHistroy(boolean isSynchroHistroy) {
		this.isSynchroHistroy = isSynchroHistroy;
	}

	private LinkedList<Activity> activityList = new LinkedList<Activity>();

	/**
	 * 是否刷新app列表页面
	 */
	public static Boolean isRefreshAppList = false;
	/**
	 * 是否加载app应用列表
	 */
	public static Boolean isLoadAppList = false;
	
	public static Boolean isShowHead = false;

	/**
	 * app 应用类表，用在自定义拖动后保存
	 */
	public static List<AppListDBInfo> appList = new ArrayList<AppListDBInfo>();

	/**
	 * 应用实例对象
	 */
	public static ImApp sInstance;

	/**
	 * 读取应为表情的正则
	 */
	public static String FACE_ZHENGZE_EN = "";
	/**
	 * 表情内容中文转换的正则
	 */
	public static String FACE_ZHEGNZE_ZW="";

	/** 
	 * 表情对应关系Map 名称对应id
	 */
	public static HashMap<String, Integer> FACE_MAP = new HashMap<String, Integer>();
	/**
	 * 表情中英文对应的map 中文匹配英文
	 */
	public static HashMap<String, String> FACE_NAME_MAP_ZW_TO_EN = new HashMap<String, String>();
	/**
	 * 表情中英文对应的map 英文匹配中文
	 */
	public static HashMap<String, String> FACE_NAME_MAP_EN_TO_ZN = new HashMap<String, String>();
	/**
	 * 语音存放的Map
	 */
	public static HashMap<String, String> VOICE_MAP= new HashMap<String, String>();

	/**
	 * 表情集合
	 */
	public static List<FaceInfo> FACE_LIST;
	/**
	 * 图文解析后的 MessageInfo集合
	 */
	//public static HashMap<String,List<MessageInfo>> MSGLIST_FROM_PIC_MAP = new HashMap<String, List<MessageInfo>>();
	/**
	 * 图片，语音上传前的对象
	 * 上传后要发送的对象
	 */
	public static HashMap<String, UserMessage> sendMessageMap = new HashMap<String, UserMessage>();

	public  Bitmap samplePictureBitmap=null;

	/**
	 * 登录用户id
	 */
	private int userid;
	/**
	 * 密钥
	 */
	private int token;

	/**
	 * 登录用户账号
	 */
	private String username;

	/**
	 * 登录用户的结构id
	 */
	private int depid;

	private String server_ip;

	/**
	 * 登录密码
	 */
	private    String password;
	/**
	 * 真实姓名
	 * 中文名称
	 */
	private String realName;
	/**
	 * 性别
	 */
	private int sex=0;

	/**
	 * 手机屏幕分辨率 宽度
	 */
	private int widthPixels;
	

	/**
	 * 手机屏幕分辨率 高度
	 */
	private int heightPixels;



	/**
	 * 心跳时间
	 */
	private  static long heartbeattime = 0;

	/**
	 * 系统时间
	 * 
	 */
	private static long system_time = System.currentTimeMillis();

	/**
	 *  系统参数
	 */
	private SystemParameterDBinfo systemParameter;

	/**
	 * 群组个数上限
	 */
	private int groupLimit = 0; 


	/**
	 * 网络地址
	 */
	private  String icon_url;

	private  String headImage;


	@Override
	public void onCreate() {

		super.onCreate();
		sInstance = this;
		//注册实例化异常处理类
		//		CrashHandler crashHandler = CrashHandler.getInstance();  
		//		crashHandler.init(getApplicationContext());  
	}

	public int getSex() {
		sex=SharedPreferencesSave .getInstance(this)
				.getIntValue(DataConstant.SHAREDPREFERENCES_USER_SEX, 0);

		return sex;
	}

	public void setSex(int sex) {
		SharedPreferencesSave .getInstance(this).
		saveIntValue(DataConstant.SHAREDPREFERENCES_USER_SEX, sex);
		this.sex = sex;
	}


	public int getWidthPixels() {
		widthPixels = SharedPreferencesSave .getInstance(this)
				.getIntValue(DataConstant.SHAREDPREFERENCES_WIDTH_PIXELS, 0);
		return widthPixels;
	}



	public void setWidthPixels(int widthPixels) {
		SharedPreferencesSave .getInstance(this).
		saveIntValue(DataConstant.SHAREDPREFERENCES_WIDTH_PIXELS, widthPixels);
		this.widthPixels = widthPixels;
		
	}



	public int getHeightPixels() {
		heightPixels = SharedPreferencesSave .getInstance(this)
				.getIntValue(DataConstant.SHAREDPREFERENCES_HEIGHT_PIXELS, 0);
		return heightPixels;
	}



	public void setHeightPixels(int heightPixels) {
		SharedPreferencesSave .getInstance(this).
		saveIntValue(DataConstant.SHAREDPREFERENCES_HEIGHT_PIXELS, heightPixels);
		this.heightPixels = heightPixels;
	}
	
	
	public String getRealName() {
		realName  = SharedPreferencesSave .getInstance(this).getStringValue(DataConstant.SHAREDPREFERENCES_USER_REALNAME, "");
		return realName;
	}

	public void setRealName(String realName) {
		SharedPreferencesSave .getInstance(this).saveStringValue(DataConstant.SHAREDPREFERENCES_USER_REALNAME, realName);
		this.realName = realName;
	}
	
	public String getHeadImage() {
		headImage  = SharedPreferencesSave .getInstance(this).getStringValue(DataConstant.SHAREDPREFERENCES_USER_HEAD, "");
		return headImage;
	}



	public void setHeadImage(String headImage) {
		SharedPreferencesSave .getInstance(this).saveStringValue(DataConstant.SHAREDPREFERENCES_USER_HEAD, headImage);

		this.headImage = headImage;
	}


	public int getDepid() {
		depid=SharedPreferencesSave .getInstance(this)
				.getIntValue(DataConstant.SHAREDPREFERENCES_LOGIN_DEPID, 0);
		if(depid==0){
			ImLog.e("ImApp","depid失效");
		}
		return depid;
	}

	public void setDepid(int depid) {
		SharedPreferencesSave .getInstance(this).
		saveIntValue(DataConstant.SHAREDPREFERENCES_LOGIN_DEPID, depid);
		this.depid = depid;
	}

	public int getUserid() {
		userid=SharedPreferencesSave .getInstance(this)
				.getIntValue(DataConstant.SHAREDPREFERENCES_LOGIN_ID, 0);
		if(userid==0){
			ImLog.e("ImApp","用户id失效");
		}
		return userid;
	}

	public void setUserid(int userid) {
		SharedPreferencesSave .getInstance(this).
		saveIntValue(DataConstant.SHAREDPREFERENCES_LOGIN_ID, userid);
	}

	public int getToken() {
		token=SharedPreferencesSave .getInstance(this)
				.getIntValue(DataConstant.SHAREDPREFERENCES_LOGIN_TOKEN, 0);
		if(token==0){
			ImLog.e("ImApp","用户密匙失效");
		}
		return token;
	}

	public void setToken(int token) {
		SharedPreferencesSave .getInstance(this).
		saveIntValue(DataConstant.SHAREDPREFERENCES_LOGIN_TOKEN, token);
		this.token = token;
	}




	public String getIcon_url() {
		icon_url= SharedPreferencesSave .getInstance(this)
				.getStringValue(DataConstant.SHAREDPREFERENCES_USER_ICON_URL, "");
		return icon_url;
	}



	public void setIcon_url(String icon_url) {
		SharedPreferencesSave .getInstance(this).
		saveStringValue(DataConstant.SHAREDPREFERENCES_USER_ICON_URL, icon_url);
		this.icon_url = icon_url;
	}



	public String getServer_ip() {
		server_ip=	SharedPreferencesSave .getInstance(this).
				getStringValue(Constant.SHAREDPREFERENCES_KEY_SERVER_IP, "");
		return server_ip;
	}



	public String getPassword() {
		password= SharedPreferencesSave .getInstance(this)
				.getStringValue(DataConstant.SHAREDPREFERENCES_USER_PWD, "");
		return password;
	}


	public void setPassword(String password) {
		SharedPreferencesSave .getInstance(this).
		saveStringValue(DataConstant.SHAREDPREFERENCES_USER_PWD, password);
		this.password = password;
	}
  
	   /**
     * 获取登录账户（服务器返回的）
     * **/
	public String getUsername() {
		username= SharedPreferencesSave .getInstance(this)
				.getStringValue(DataConstant.SHAREDPREFERENCES_USER_ACCONTNAME, "");
		return username;
	}
    /**
     * 保存登录账户（服务器返回的）
     * **/
	public void setUsername(String username) {
		SharedPreferencesSave .getInstance(this).
		saveStringValue(DataConstant.SHAREDPREFERENCES_USER_ACCONTNAME, username);
		this.username = username;
	}

	public long getHeartbeattime() {
		 long htime=SharedPreferencesSave .getInstance(this)
			.getLongValue(DataConstant.SHAREDPREFERENCES_NOW_HEARTBEAT_TIME, 0);
		 if(htime>0){heartbeattime=htime;}
		return heartbeattime;
	}
	public void setHeartbeattime(long heartbeattime) {  
		ImApp.heartbeattime = heartbeattime;
		 SharedPreferencesSave .getInstance(this)
		.saveLongValue(DataConstant.SHAREDPREFERENCES_NOW_HEARTBEAT_TIME,heartbeattime);
	} 

	public long getSystem_time() {
		 long stime=SharedPreferencesSave .getInstance(this)
					.getLongValue(DataConstant.SHAREDPREFERENCES_NOW_SYSTEM_TIME, 0);
				 if(stime>0){system_time=stime;}
		return system_time;
	}
 
	public void setSystem_time(long system_time) {
		ImApp.system_time = system_time;
	 	 SharedPreferencesSave .getInstance(this)
	 	 .saveLongValue(DataConstant.SHAREDPREFERENCES_NOW_SYSTEM_TIME,system_time);
			
	}


	public SystemParameterDBinfo getSystemParameter() {
		if(systemParameter!=null){
			if(systemParameter.fileServer==null || systemParameter.fileServer==""){
				systemParameter=DatabaseAdapter.getInstance(getApplicationContext()).selectSystemParameter();
			}		
		} else{
			systemParameter=DatabaseAdapter.getInstance(getApplicationContext()).selectSystemParameter();
		}
		return systemParameter;
	}

	public void setSystemParameter(SystemParameterDBinfo systemParameter) {

		this.systemParameter = systemParameter;

	}

	public int getGroupLimit() {
		return groupLimit;
	}

	public void setGroupLimit(int groupLimit) {
		this.groupLimit = groupLimit;
	}



	/**
	 * 当主程序退出
	 */
	public void beforeKill() {
		if (hasExited) {
			return;
		}
		clearSerData();
		finishAll();
		SIPControl.exitSIP(sInstance);
		ImLog.anlong("快快已退出");
		android.os.Process.killProcess(android.os.Process.myPid()); 
	}

	/**
	 * 
	 * @Title: clearSerData 
	 * @Description: TODO  清除登录服务应用数据
	 * @author anlong 
	 * @param      
	 * @return void
	 */  
	public  void clearSerData(){
		//TODO 清除临时保存用户在线状态
		clearUseroOnlineState(getApplicationContext());
		DatabaseAdapter.getInstance(getApplicationContext()).DestroyDBHelper();
		//关闭核心服务  
		stopService(new Intent(getApplicationContext(),CoreService.class));
		setHeartbeattime(0);
		setUserid(0);
		hasExited = true; 
	}

	 /**
	  * 
	  * @Title: clearUseroOnlineState 
	  * @Description: TODO 清除用户在线
	  * @author anlong 
	  * @param @param cxt     
	  * @return void
	  */
	public static void clearUseroOnlineState(Context cxt){
		DatabaseAdapter.getInstance(cxt) .setAllUserInfoOnlineStateOnOff(); 
	}
	
	
	/**
	 * 
	 * @Description: TODO 获取当前应用 Context
	 * @return Context
	 */
	public Context getContext() {
		return getApplicationContext();
	}

	/**
	 * 主程序是否已经退出  默认false 没有退出;
	 */
	public static boolean hasExited() {
		return hasExited;
	}




	/**
	 * 
	 * @Description: TODO 获得APP实例
	 * @return
	 */
	public static ImApp getsInstance() {
		if(sInstance==null){
			sInstance=new ImApp();
		}
		return sInstance;
	}



	/*** 
	 * 结束所有的activity，但不会关闭程序的进程 
	 */  
	public   void finishAll() {  
		for (Activity act : activityList) {  
			act.finish();  
		}     
		activityList.clear();  
	} 

	/*** 
	 * 在每个Activity的onCreate中调用，用来记录打开了的activity 
	 */  
	public   void addActivity(Activity act) {  
		activityList.add(act);  
	} 

	/*** 
	 * 在每个Activity的onDestroy中调用 
	 */  
	public   void removeActivity(Activity act) {  
		activityList.remove(act);  
	}  
	/**
	 * 
	 * @Description: TODO 获取当前系统心跳时间
	 * @param time
	 * @return  
	 */  
	public Date getSystemTime() {
		return new Date(getSystem_time());
	}

	private Bitmap bitmap = null;               //临时保存会话背景图片

	public void setBitmapImage(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public Bitmap getBitmapImage(){
		return this.bitmap;
	}

	/**
	 * 选择的缩略图保存路径
	 */
	private String picThumbPath;

	public void setPicThumbPath(String picThumbPath){
		this.picThumbPath = picThumbPath;
	}

	public String getPicThumbPath(){
		return this.picThumbPath;
	}


	/**
	 * 选择的图片路径
	 */
	private String picPath;

	public void setPicPath(String picPath){
		this.picPath = picPath;
	}

	public String getPicPath(){
		return this.picPath;
	}
}
