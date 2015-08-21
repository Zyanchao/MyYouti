package com.youti.utils;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.youti.view.WaitDialog;

public class Utils {

	public static Toast mToast;

	public static void showToast(Context mContext, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}
	
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	
	/**
	 * dip 转换成 px
	 * @param dip
	 * @param context
	 * @return
	 */
	public static float dip2Dimension(float dip, Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
	}
	/**
	 * @param dip
	 * @param context
	 * @param complexUnit {@link TypedValue#COMPLEX_UNIT_DIP} {@link TypedValue#COMPLEX_UNIT_SP}}
	 * @return
	 */
	public static float toDimension(float dip, Context context, int complexUnit) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(complexUnit, dip, displayMetrics);
	}

	/** 获取状态栏高度
	 * @param v
	 * @return
	 */
	public static int getStatusBarHeight(View v) {
		if (v == null) {
			return 0;
		}
		Rect frame = new Rect();
		v.getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static String getActionName(MotionEvent event) {
		String action = "unknow";
		switch (MotionEventCompat.getActionMasked(event)) {
		case MotionEvent.ACTION_DOWN:
			action = "ACTION_DOWN";
			break;
		case MotionEvent.ACTION_MOVE:
			action = "ACTION_MOVE";
			break;
		case MotionEvent.ACTION_UP:
			action = "ACTION_UP";
			break;
		case MotionEvent.ACTION_CANCEL:
			action = "ACTION_CANCEL";
			break;
		case MotionEvent.ACTION_SCROLL:
			action = "ACTION_SCROLL";
			break;
		case MotionEvent.ACTION_OUTSIDE:
			action = "ACTION_SCROLL";
			break;
		default:
			break;
		}
		return action;
	}
	
		/**
		 * 提交信息dialog
		 * @param context
		 * @param tip
		 * @return
		 */
	public static Dialog createProgressBarDialog(Context context, String tip) {
        final Dialog dialog = new Dialog(context,R.style.dialog);
        dialog.setContentView(R.layout.progressbar_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = ScreenUtils.getScreenWidth(context);
        lp.width = (int) (0.6 * width);
        
        TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);

        titleTxtv.setText(tip);
        return dialog;
    }
	
	 private static Boolean _isTablet = null;


	    public static float dpToPixel(float dp,Context context) {
		return dp * (getDisplayMetrics(context).densityDpi / 160F);
	    }


	    public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(
			Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
			displaymetrics);
		return displaymetrics;
	    }

	    public static boolean isTablet(Context context) {
		if (_isTablet == null) {
		    boolean flag;
		    if ((0xf & context.getResources()
			    .getConfiguration().screenLayout) >= 3)
			flag = true;
		    else
			flag = false;
		    _isTablet = Boolean.valueOf(flag);
		}
		return _isTablet.booleanValue();
	    }

	    public static float getScreenWidth(Context context) {
	    	return getDisplayMetrics(context).widthPixels;
	        }
	
	public static WaitDialog getWaitDialog(Activity activity, String message) {
		
		
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog_waiting);
			dialog.setMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}
	
	/**
	 * 进入app市场
	 */
	 public static void openAppInMarket(Context context) {
			if (context != null) {
			    String pckName = context.getPackageName();
			    try {
				gotoMarket(context, pckName);
			    } catch (Exception ex) {
				try {
				    String otherMarketUri = "http://market.android.com/details?id="
					    + pckName;
				    Intent intent = new Intent(Intent.ACTION_VIEW,
					    Uri.parse(otherMarketUri));
				    context.startActivity(intent);
				} catch (Exception e) {

				}
			    }
			}
	}
	 public static boolean isHaveMarket(Context context) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.APP_MARKET");
			PackageManager pm = context.getPackageManager();
			List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
			return infos.size() > 0;
		    }
	 public static void gotoMarket(Context context, String pck) {
			if (!isHaveMarket(context)) {
			    Utils.showToast(context,"你手机中没有安装应用市场！");
			    return;
			}
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + pck));
			if (intent.resolveActivity(context.getPackageManager()) != null) {
			    context.startActivity(intent);
			}
		    }
}
