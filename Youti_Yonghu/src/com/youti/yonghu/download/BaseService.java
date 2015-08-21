/**   
* @Title: BaseService.java
* @Package com.cloud.coupon.service
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-6-26 上午9:35:01
* @version V1.0
*/ 
package com.youti.yonghu.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** 
 * @ClassName: BaseService
 * @Description: 服务 基类
 * @author 陈红建
 * @date 2013-6-26 上午9:35:01
 * 
 */
public class BaseService extends Service implements ContentValue , AgentConstant
{

	/** (非 Javadoc) 
	 * Title: onBind
	 * Description:
	 * @param intent
	 * @return
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	/** (非 Javadoc) 
	* Title: onStart
	* Description:
	* @param intent
	* @param startId
	* @see android.app.Service#onStart(android.content.Intent, int)
	*/ 
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}
	/** (非 Javadoc) 
	* Title: onCreate
	* Description:
	* @see android.app.Service#onCreate()
	*/ 
	@Override
	public void onCreate()
	{
		super.onCreate();
	}

}

