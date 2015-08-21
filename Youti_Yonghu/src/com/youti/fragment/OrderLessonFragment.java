package com.youti.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.view.CustomProgressDialog;
import com.youti.view.SwipeLayout;
import com.youti.view.SwipeLayout.SwipeListener;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.MyCoachYueKeBean;
import com.youti.yonghu.bean.MyCoachYueKeBean.YueKeBean;

public class OrderLessonFragment extends Fragment implements OnClickListener {
	ListView listView;
	MyAdapter ma;
	String user_id;
	FrameLayout fl_content;
	ImageView iv;
	ProgressBar pb;
	TextView tv;
	Dialog waitDialog;
	private Dialog cancelDialog;
	private Dialog endDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public final String mPageName = "OrderLessonFragment";

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause(); 
		if (waitDialog != null) {
			waitDialog.dismiss();
			waitDialog = null;
		}
		MobclickAgent.onPageEnd(mPageName);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_systemmessage,
				null);
		fl_content = (FrameLayout) view.findViewById(R.id.fl_content);

		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv = (ImageView) v.findViewById(R.id.img_error_layout);
		tv = (TextView) v.findViewById(R.id.tv_error_layout);

		pb = (ProgressBar) v.findViewById(R.id.animProgress);
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setBackgroundColor(Color.parseColor("#eeeeee"));
		user_id = ((YoutiApplication) getActivity().getApplication()).myPreference
				.getUserId();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		requestData();

	}

	private List<YueKeBean> list;

	/**
	 * 请求约课列表数据
	 */
	private void requestData() {
		waitDialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		String str = "http://112.126.72.250/ut_app/index.php?m=User&a=my_agree";
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		HttpUtils.post(str, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson = new Gson();
				MyCoachYueKeBean fromJson = gson.fromJson(arg2,
						MyCoachYueKeBean.class);
				if (fromJson.code.equals("1")) {
					waitDialog.dismiss();
					// fl_content.removeAllViews();
					// fl_content.addView(listView);
					if (fromJson.list != null && !fromJson.list.isEmpty()) {
						list = fromJson.list;
						ma = new MyAdapter();
						listView.setAdapter(ma);
					} else {
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无约课数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						Utils.showToast(getActivity(), "无数据");
					}
				} else if (fromJson.code.equals("0")) {

				} else {
					// waitDialog.dismiss();
					Utils.showToast(getActivity(), "用户id为空");
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				waitDialog.dismiss();
				fl_content.removeAllViews();
				iv.setBackgroundResource(R.drawable.pagefailed_bg);
				iv.setVisibility(View.VISIBLE);
				tv.setText("网络连接异常，点击重试");
				pb.setVisibility(View.GONE);
				fl_content.addView(v);
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						requestData();
					}
				});
				Utils.showToast(getActivity(), "网络异常");
			}
		});

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			// requestData();
		} else {
			// 相当于Fragment的onPause

		}
	}

	class ViewHolder {
		TextView coach_name, coach_project, hour, time, end_time, tv_state;
		Button cancellesson, endlesson, bt_cancel, bt_delete;
		CircleImageView head_protrait;
		LinearLayout ll_button;
	}

	class MyAdapter extends BaseAdapter {

		HashSet<Integer> mRemoved = new HashSet<Integer>();
		HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

		public int getUnClosedCount() {
			return mUnClosedLayouts.size();
		}

		public void closeAllLayout() {
			if (mUnClosedLayouts.size() == 0)
				return;

			for (SwipeLayout l : mUnClosedLayouts) {
				l.close(true, false);
			}
			mUnClosedLayouts.clear();
		}

		OnClickListener onActionClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer p = (Integer) v.getTag();
				int id = v.getId();
				if (id == R.id.bt_cancel) {
					closeAllLayout();
				} else if (id == R.id.bt_delete) {
					closeAllLayout();
					// RequestSQTK("", "3", list.get(p).order_id);
					int index = p.intValue();
					if("0".equals(list.get(index).status)){
						
						delete(list.get(p).agree_id);
						list.remove(index);
						notifyDataSetChanged();
					}else{
						Utils.showToast(getActivity(), "暂时无法删除");
					}

				}
			}
		};

		SwipeListener mSwipeListener = new SwipeListener() {
			@Override
			public void onOpen(SwipeLayout swipeLayout) {
				// Utils.showToast(mContext, "onOpen");
				mUnClosedLayouts.add(swipeLayout);
			}

			@Override
			public void onClose(SwipeLayout swipeLayout) {
				// Utils.showToast(mContext, "onClose");
				mUnClosedLayouts.remove(swipeLayout);
			}

			@Override
			public void onStartClose(SwipeLayout swipeLayout) {
				// Utils.showToast(mContext, "onStartClose");
			}

			@Override
			public void onStartOpen(SwipeLayout swipeLayout) {
				// Utils.showToast(mContext, "onStartOpen");
				closeAllLayout();
				mUnClosedLayouts.add(swipeLayout);
			}

		};
		private long current;
		private long start;
		private String hour;

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			SwipeLayout view;
			ViewHolder vh;
			if (convertView == null) {
				view = (SwipeLayout) View.inflate(getActivity(),
						R.layout.item_orderlesson, null);
				vh = new ViewHolder();
				vh.cancellesson = (Button) view.findViewById(R.id.cancellesson);
				vh.endlesson = (Button) view.findViewById(R.id.endlesson);
				vh.head_protrait = (CircleImageView) view
						.findViewById(R.id.head_protrait);
				vh.coach_name = (TextView) view.findViewById(R.id.coach_name);
				vh.coach_project = (TextView) view
						.findViewById(R.id.coach_project);
				vh.hour = (TextView) view.findViewById(R.id.hour);
				vh.time = (TextView) view.findViewById(R.id.time);
				vh.ll_button = (LinearLayout) view.findViewById(R.id.ll_button);
				vh.end_time = (TextView) view.findViewById(R.id.end_time);
				vh.tv_state = (TextView) view.findViewById(R.id.tv_state);
				vh.bt_cancel = (Button) view.findViewById(R.id.btn_cancel);
				vh.bt_delete = (Button) view.findViewById(R.id.bt_delete);
				view.setTag(vh);

			} else {
				view = (SwipeLayout) convertView;
				vh = (ViewHolder) view.getTag();
			}

			view.close(false, false);
			view.setSwipeListener(mSwipeListener);

			// vh.bt_cancel.setTag(position);
			// vh.bt_cancel.setOnClickListener(onActionClick);

			vh.bt_delete.setTag(position);
			vh.bt_delete.setOnClickListener(onActionClick);

			ImageLoader.getInstance().displayImage(
					"http://112.126.72.250/ut_app"
							+ list.get(position).coach_img, vh.head_protrait);
			vh.coach_name.setText(list.get(position).coach_name);
			vh.coach_project.setText(list.get(position).pro_val);
			vh.hour.setText(list.get(position).long_time);
			vh.time.setText(list.get(position).start_time);
			vh.end_time.setText(list.get(position).end_time);
			if ("0".equals(list.get(position).status)) {
				// 已取消
				vh.tv_state.setVisibility(View.VISIBLE);
				vh.tv_state.setText("已取消");
				vh.cancellesson.setVisibility(View.GONE);
				vh.endlesson.setVisibility(View.GONE);
			} else {
				// 进行中
				vh.cancellesson.setVisibility(View.VISIBLE);
				vh.endlesson.setVisibility(View.VISIBLE);
				vh.tv_state.setVisibility(View.GONE);
			}

			
			vh.endlesson.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					endDialog = new Dialog(getActivity(), R.style.tkdialog);
					View view = View.inflate(getActivity(),R.layout.diglog_common, null);
					TextView tv_tousu = (TextView) view.findViewById(R.id.tv_tousu);
					TextView tv_finish = (TextView) view.findViewById(R.id.tv_finish);
					TextView tv_endlesson = (TextView) view.findViewById(R.id.tv_endlesson);
					TextView tv_close =(TextView) view.findViewById(R.id.tv_close);
					LinearLayout ll_select=(LinearLayout) view.findViewById(R.id.ll_select);
					
					current = System.currentTimeMillis();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					start = 0l;
					hour = list.get(position).long_time;
					
					try {
						start = df.parse(list.get(position).start_time).getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					
				 if(current<(start+3600000l*Integer.parseInt(hour))) {
						// 开课后，但是课程未结束
						tv_endlesson.setText("提示：未到课程结束时间，不能结束课程");
						tv_close.setVisibility(View.GONE);
						ll_select.setVisibility(View.VISIBLE);
					}else{
						//不是开课前两小时内，也不是在上课期间
						tv_endlesson.setText("请确定教练已经上课完成后，点击结束课程，并给教练本次课程做出评价。如对本次教练有所不满意之处可以点击投诉");
						tv_close.setVisibility(View.GONE);
						ll_select.setVisibility(View.VISIBLE);
					}

					tv_close.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							endDialog.dismiss();
						}
					});
					tv_tousu.setOnClickListener(OrderLessonFragment.this);
					tv_finish.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							finishLesson(position);
						}
					});
					endDialog.setContentView(view);
					endDialog.show();
				}
			});
			vh.cancellesson.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					current = System.currentTimeMillis();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					start = 0l;
					hour = list.get(position).long_time;
					
					try {
						start = df.parse(list.get(position).start_time).getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					cancelDialog = new Dialog(getActivity(), R.style.tkdialog);
					View view = View.inflate(getActivity(),
							R.layout.dialog_tuikuan, null);

					tv_sure = (TextView) view.findViewById(R.id.tv_sure);
					tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
					tv2 = (TextView) view.findViewById(R.id.tv2);
					tv3 = (TextView) view.findViewById(R.id.tv3);
					tv_title = (TextView) view.findViewById(R.id.tv_title);
					
					if ((current + 7200000l) > start&&current<start) {
						
						// 是开课前两小时内
						endDialog = new Dialog(getActivity(), R.style.tkdialog);
						View view1 = View.inflate(getActivity(),R.layout.diglog_common, null);
						TextView tv_endlesson = (TextView) view1.findViewById(R.id.tv_endlesson);
						TextView tv_close =(TextView) view1.findViewById(R.id.tv_close);
						LinearLayout ll_select=(LinearLayout) view1.findViewById(R.id.ll_select);
						tv_endlesson.setText("提示：开课前两小时内无法取消本次约课:current"+current+"start"+start+list.get(position).start_time+"position:"+position);
						tv_close.setVisibility(View.VISIBLE);
						ll_select.setVisibility(View.GONE);
						tv_close.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								endDialog.dismiss();
							}
						});
						endDialog.setContentView(view1);
						endDialog.show();
						return;
					} else if(current>start){
						Utils.showToast(getActivity(), "正在上课或者课程已结束");
						return;
					}else{
						
					}
					
					
					
					
					tv2.setText("2、教练临时有事");
					tv3.setText("3、不想上了");
					tv_title.setText("请选择取消的原因");

					ll1 = (LinearLayout) view.findViewById(R.id.ll1);
					ll2 = (LinearLayout) view.findViewById(R.id.ll2);
					ll3 = (LinearLayout) view.findViewById(R.id.ll3);
					ll4 = (LinearLayout) view.findViewById(R.id.ll4);

					iv1 = (ImageView) view.findViewById(R.id.iv1);
					iv2 = (ImageView) view.findViewById(R.id.iv2);
					iv3 = (ImageView) view.findViewById(R.id.iv3);
					iv4 = (ImageView) view.findViewById(R.id.iv4);

					ll1.setOnClickListener(OrderLessonFragment.this);
					ll2.setOnClickListener(OrderLessonFragment.this);
					ll3.setOnClickListener(OrderLessonFragment.this);
					ll4.setOnClickListener(OrderLessonFragment.this);

					cancelDialog.setContentView(view);
					cancelDialog.show();
					tv_sure.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!iv1.isShown() && !iv2.isShown()
									&& !iv3.isShown() && !iv4.isShown()) {
								Toast.makeText(getActivity(), "请选择一个原因", 0)
										.show();
								return;
							}
							cancleOrderLesson(position);

						}
					});
					tv_cancle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							cancelDialog.dismiss();
						}
					});

					vg = (ViewGroup) v.getParent();
				}
			});

			return view;
		}

	}

	TextView tv_sure, tv2, tv3, tv_cancle, tv_title;
	LinearLayout ll_button;
	AlertDialog dialog;
	ViewGroup vg;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tousu:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:"+"4006968080"));
			startActivity(intent);
			endDialog.dismiss();
			break;
		case R.id.tv_finish:
			break;

		case R.id.ll1:
			if (isll1) {
				isll1 = false;
				iv1.setVisibility(View.VISIBLE);
				setVisiable(iv2, iv3, iv4);
				isll2 = false;
				isll3 = false;
				isll4 = false;
			} else {
				isll1 = true;
				iv1.setVisibility(View.GONE);
			}
			break;
		case R.id.ll2:
			if (isll2) {
				isll2 = false;
				iv2.setVisibility(View.GONE);
			} else {
				isll2 = true;
				iv2.setVisibility(View.VISIBLE);
				setVisiable(iv1, iv3, iv4);
				isll1 = true;
				isll3 = false;
				isll4 = false;
			}

			break;
		case R.id.ll3:
			if (isll3) {
				isll3 = false;
				iv3.setVisibility(View.GONE);
			} else {
				isll3 = true;
				iv3.setVisibility(View.VISIBLE);
				setVisiable(iv2, iv1, iv4);
				isll1 = true;
				isll2 = false;
				isll4 = false;
			}
			break;
		case R.id.ll4:
			if (isll4) {
				isll4 = false;
				iv4.setVisibility(View.GONE);
			} else {
				isll4 = true;
				iv4.setVisibility(View.VISIBLE);
				setVisiable(iv2, iv3, iv1);
				isll1 = true;
				isll3 = false;
				isll2 = false;
			}
			break;
		default:
			break;
		}
	}

	private void finishLesson(final int position) {
		String str = "http://112.126.72.250/ut_app/index.php?m=User&a=finish_class";
		RequestParams params = new RequestParams();
		params.put("agree_id", list.get(position).agree_id);
		HttpUtils.post(str, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson = new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if ("1".equals(fromJson.code)) {
					endDialog.dismiss();
					list.remove(position);
					
					if (ma == null) {
						ma = new MyAdapter();
						listView.setAdapter(ma);
					} else {
						ma.notifyDataSetChanged();
					}
					Utils.showToast(getActivity(), "课程结束成功");
				} else if ("0".equals(fromJson.code)) {
					Utils.showToast(getActivity(), "课程结束失败");
				} else if ("401".equals(fromJson.code)) {
					Utils.showToast(getActivity(), "课程结束时间小于当前时间不能结束课程");
				} else {
					Utils.showToast(getActivity(), "约课id为空");
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}

	private void cancleOrderLesson(int position) {
		String str = "http://112.126.72.250/ut_app/index.php?m=User&a=cancel_class";
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("order_id", list.get(position).order_id);
		params.put("agree_id", list.get(position).agree_id);
		HttpUtils.post(str, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson = new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if ("1".equals(infoBean.code)) {
					vg.removeAllViews();
					TextView tv = new TextView(getActivity());
					tv.setText("已取消");
					vg.addView(tv);

					cancelDialog.dismiss();
					if (ma == null) {
						ma = new MyAdapter();
						listView.setAdapter(ma);
					} else {
						ma.notifyDataSetChanged();
					}
					Utils.showToast(getActivity(), "取消约课成功");
				} else if ("0".equals(infoBean.code)) {
					Utils.showToast(getActivity(), "取消约课失败");
				} else if ("403".equals(infoBean.code)) {
					Utils.showToast(getActivity(), "开课三小时内不可以取消约课");
				} else if ("402".equals(infoBean.code)) {
					Utils.showToast(getActivity(), "约课id为空");
				} else if ("401".equals(infoBean.code)) {
					Utils.showToast(getActivity(), "订单id为空");

				} else {
					Utils.showToast(getActivity(), "用户id为空");
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}

	LinearLayout ll1, ll2, ll3, ll4;
	ImageView iv1, iv2, iv3, iv4;
	boolean isll1, isll2, isll3, isll4;
	private View view;
	private View v;

	public void setVisiable(ImageView iv11, ImageView iv22, ImageView iv33) {
		iv11.setVisibility(View.GONE);
		iv22.setVisibility(View.GONE);
		iv33.setVisibility(View.GONE);
	}
	
	public void delete(String agree_id){
		RequestParams params =new RequestParams();
		params.put("agree_id", agree_id);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=del_agree", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson = new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if("1".equals(fromJson.code)){
					Utils.showToast(getActivity(), "删除成功");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});
	}
}
