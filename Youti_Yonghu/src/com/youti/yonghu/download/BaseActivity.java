package com.youti.yonghu.download;

import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.youti.appConfig.YoutiApplication;

/**
 * @ClassName: BaseActivity
 * @Description: Activity基类
 * @author 陈红建
 * @date 2013-3-21 下午11:04:03
 * 
 */
public class BaseActivity extends FragmentActivity implements ContentValue
{

	private SharedPreferences sp;
	private Context mContext;
	private YoutiApplication myApp;
	private Editor edit;

	/**
	 * @Title: showPrigressDialog
	 * @Description: 显示一个等待风格的对话框
	 * @param mContext
	 *            上下文环境
	 * @param title
	 *            标题
	 * @param msg
	 *            消息
	 * @param cancelable
	 *            是否可取消
	 * @return 返回ProgressDialog这个对象
	 * @author 陈红建
	 */
	public ProgressDialog showProgressDialog(Context mContext, String title,
			String msg, boolean cancelable)
	{
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setCancelable(cancelable);
		dialog.show();
		return dialog;

	}

	/**
	 * @Title: getServerIntent
	 * @Description: 获得服务的意图
	 * @param
	 * @return Intent
	 * @author 陈红建
	 * @throws
	 */
	public Intent getServerIntent()
	{
		Intent i = new Intent(getmContext(), DownloadService.class);
		i.putExtra(CACHE_DIR, getStringValueByConfigFile(CACHE_DIR)); // 设置缓存路径
		return i;
	}


	/**
	 * @Title: putString*ToConfigFile
	 * @Description: 想配置文件中增加一个字段
	 * @param key
	 * @param value
	 **/
	public boolean putStringValueToConfigFile(String key, String value)
	{
		Editor e = getSp().edit();
		e.putString(key, value);
		return e.commit();
	}

	public boolean putStringValueToConfigFile(String key, int value)
	{
		Editor e = getSp().edit();
		e.putInt(key, value);
		return e.commit();
	}

	public boolean putBooleanValueToConfigFile(String key, boolean value)
	{
		Editor e = getSp().edit();
		e.putBoolean(key, value);
		return e.commit();
	}

	public String getStringValueByConfigFile(String key)
	{
		return sp.getString(key, "");
	}

	public int getIntegerValueByConfigFile(String key)
	{
		return getSp().getInt(key, -1);
	}

	public boolean getBooleanValueByConfigFile(String key)
	{
		return getSp().getBoolean(key, false);
	}


	/**
	 * @Title: showMyDialog
	 * @Description: 显示一个对话框
	 * @param mContext
	 *            上下文环境
	 * @param title
	 *            对话框标题
	 * @param msg
	 *            对话框内容
	 * @param positiveButtonStr
	 *            (主要按钮名称)
	 * @param negativeButtonStr
	 *            (次要按钮名称)
	 * @param negativeButtonStrListener
	 *            主要按钮的单击事件
	 * @param positiveButtonStrListener
	 *            次要按钮单机事件
	 * @param resourceId
	 *            是否有图标,指定图标ID,否则为0
	 * @return void
	 * @author 陈红建
	 */
	public void showMyDialog(Context mContext, String title, String msg,
			String positiveButtonStr, String negativeButtonStr,
			OnClickListener negativeButtonStrListener,
			OnClickListener positiveButtonStrListener, int resourceId)
	{
		Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setPositiveButton(positiveButtonStr, positiveButtonStrListener);
		dialog.setNegativeButton(negativeButtonStr, negativeButtonStrListener);
		if (resourceId != 0)
		{
			dialog.setIcon(resourceId);
		}
		dialog.show();
	}

	/**
	 * @Title: onClick
	 * @Description: 单机事件
	 * @param view
	 *            系统回调的view
	 * @return void
	 * @author 陈红建
	 */
	public void onClick(View view)
	{

	}

	/**
	 * @Title: showTost
	 * @Description: 弹出一个吐司提示
	 * @param msg
	 *            需要显示的消息
	 * @param mContext
	 *            接收一个环境
	 * @return void
	 * @author 陈红建
	 */
	public void showTost(String msg, Context mContext)
	{
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @Title: getUrlByParameter
	 * @Description: 根据参数,和类型返回一个URL
	 * @param param
	 *            接收一个封装了参数的Map对象
	 * @param type
	 *            指定返回url 类型
	 * @return 返回指定类型的url
	 * @author 陈红建
	 */
	public String getUrlByParameter(Map<String, String> param, int type)
	{
		String url = "";
		switch (type)
		{
		default:
			break;
		}
		return url;
	}


	/**
	 * @Title: initView
	 * @Description: 初始化控件
	 * @return void
	 * @author 陈红建
	 */
	public void initView()
	{
		YoutiApplication app = (YoutiApplication) getApplication();
		setSp(getSharedPreferences("config", MODE_PRIVATE));
		this.edit = getSp().edit();
		setMyApp(app);
	}

	
	public Context getmContext()
	{
		return mContext;
	}

	public void setmContext(Context mContext)
	{
		this.mContext = mContext;
	}

	public SharedPreferences getSp()
	{
		return sp;
	}

	public void setSp(SharedPreferences sp)
	{
		this.sp = sp;
	}

	public YoutiApplication getMyApp()
	{
		return myApp;
	}

	public void setMyApp(YoutiApplication myApp)
	{
		this.myApp = myApp;
	}

	public Editor getEdit()
	{
		return edit;
	}

	public void setEdit(Editor edit)
	{
		this.edit = edit;
	}

}
