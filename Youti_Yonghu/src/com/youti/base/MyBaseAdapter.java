/**   
* @Title: MyBaseAdapter.java
* @Package com.cloud.coupon.adapter
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-6-25 上午10:54:01
* @version V1.0
*/ 
package com.youti.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.youti.yonghu.download.ContentValue;

/** 
 * @ClassName: MyBaseAdapter
 * @Description: 适配器基类
 * @author 陈红建
 * @date 2013-6-25 上午10:54:01
 * 
 */
public class MyBaseAdapter extends BaseAdapter implements ContentValue
{

	
	/** (非 Javadoc) 
	 * Title: getCount
	 * Description:
	 * @return
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	 public int getCount()
	{
		return 0;
	}

	/** (非 Javadoc) 
	 * Title: getItem
	 * Description:
	 * @param position
	 * @return
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		return null;
	}

	/** (非 Javadoc) 
	 * Title: getItemId
	 * Description:
	 * @param position
	 * @return
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	/** (非 Javadoc) 
	 * Title: getView
	 * Description:
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return null;
	}

}

