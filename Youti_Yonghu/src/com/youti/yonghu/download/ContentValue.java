/**   
* @Title: ContentValue.java
* @Package com.cloud.coupon.contentvalue
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-6-25 上午10:57:50
* @version V1.0
*/ 
package com.youti.yonghu.download;

/** 
 * @ClassName: ContentValue
 * @Description: 公共常量
 * @author 陈红建
 * @date 2013-6-25 上午10:57:50
 * 
 */
public interface  ContentValue 
{

	String SERVICE_TYPE_NAME = "servicetype"; //通过Intent获取启动服务类型的名字
	String DOWNLOAD_TAG_BY_INTENT = "downloadurl"; // 通过Intent获取url的名字
	String CACHE_DIR = "cacheDir"; // 配置文件中,存储目录名称
	int START_DOWNLOAD_MOVIE = 99; // 启动下载电影模块
	int ERROR_CODE = -1; //出错
	int START_DOWNLOAD_LOADITEM = 10; //从数据库中装载下载任务
	int START_DOWNLOAD_ALLSUSPEND = 11; //将数据库中所有的下载状态设置为 暂停
	
	
	//下载状态
	int DOWNLOAD_STATE_DOWNLOADING = 2; //正在下载
	int DOWNLOAD_STATE_SUSPEND = 3; //暂停
	int DOWNLOAD_STATE_WATTING = 4; // 等待
	int DOWNLOAD_STATE_FAIL = 5; // 下载失败
	int DOWNLOAD_STATE_SUCCESS = 6; // 下载成功
	int DOWNLOAD_STATE_START = 7;//开始下载
	int DOWNLOAD_STATE_DELETE = 8; //任务被删除
	int DOWNLOAD_STATE_CLEAR = 9; //清除所有任务
	int DOWNLOAD_STATE_NONE = 0; //未下载的状态
	int DOWNLOAD_STATE_EXCLOUDDOWNLOAD = 12;//如果当前状态不在三个之内的下载队列中 讲自身设置为"等待中"
	

}

