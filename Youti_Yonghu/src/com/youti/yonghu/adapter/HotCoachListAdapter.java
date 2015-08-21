package com.youti.yonghu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.UserEntity;
import com.youti.yonghu.bean.VedioItem;

public class HotCoachListAdapter extends BaseAdapter<VedioItem, HotCoachListAdapter.VedioItemHolder> {
	
	public HotCoachListAdapter(Context context, List list) {
		super(context, list);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.video_list_item;
	}

	@Override
	protected VedioItemHolder createHolder(View convertView,int postion) {
		// TODO Auto-generated method stub
		VedioItemHolder holder = new VedioItemHolder();
		holder.des = (TextView) convertView.findViewById(R.id.sp_describe);
		holder.img = (ImageView) convertView.findViewById(R.id.bigImg);
		holder.num = (TextView) convertView.findViewById(R.id.sp_studyNum);
		holder.isTj = (ImageView) convertView.findViewById(R.id.sp_xiaobiantuijian);
		holder.zan_img=(ImageView) convertView.findViewById(R.id.zan_img);
		holder.zan_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(YoutiApplication.getInstance(), "点赞", 0);
			}
		});
		return holder;
	}

	public class VedioItemHolder extends BaseHolder {

		/** 是否是推荐 **/
		public ImageView isTj;
		/** 多少人学过 **/
		public TextView num;
		/** 简单介绍 **/
		public TextView des;
		public ImageView img,zan_img;
		
	}

	@Override
	protected void initHolderByBean(VedioItem bean, VedioItemHolder holder) {
		holder.des.setText(bean.des);
		if("".equals(bean.isTj)) {
			holder.isTj.setVisibility(View.VISIBLE);
		} else {			
			holder.isTj.setVisibility(View.GONE);
		}
		holder.num.setText(bean.num+"人学过");
		//bean.img 是图片的imgUrl，holder.img是ImageView。相当于xutils中的display将url图片设置到imageView中。
		loadImage(holder.img, bean.img,R.drawable.spxq_pic,R.drawable.spxq_pic);
	}
	
	List<UserEntity> likeUser= new ArrayList<UserEntity>();
	
	 public List<UserEntity> getLikeUser() {
	        return likeUser;
	    }

	    public void setLikeUser(List<UserEntity> likeUser) {
	        this.likeUser = likeUser;
	    }
	
	  int likeCount; 
	    public int getLikeCount() {
	        return likeCount;
	    }

	    public void setLikeCount(int likeCount) {
	        this.likeCount = likeCount;
	    }
	    
	   int isLike;
	   
	   public int getIsLike() {
	        return isLike;
	    }

	    public void setIsLike(int isLike) {
	        this.isLike = isLike;
	    }
	  /**
     * 设置点赞过的用户的用户名到用户名列表中
     * @param contet 
     * @param likeUser 用户名列表
     * @param limit
     */
    public void setLikeUsers(Context contet, TextView likeUser, boolean limit) {
        // 构造多个超链接的html, 通过选中的位置来获取用户名
        if (getLikeCount() > 0 && getLikeUser() != null
                && !getLikeUser().isEmpty()) {
            likeUser.setVisibility(View.VISIBLE);
            //设置超链接
            likeUser.setMovementMethod(LinkMovementMethod.getInstance());
            likeUser.setFocusable(false);
            likeUser.setLongClickable(false);
            likeUser.setText(addClickablePart(contet, limit),
                    BufferType.SPANNABLE);
        } else {
            likeUser.setVisibility(View.GONE);
            likeUser.setText("");
        }
    }

    /**
     * @param str
     * @return
     */
    private SpannableStringBuilder addClickablePart(final Context context,
            boolean limit) {

        StringBuilder sbBuilder = new StringBuilder();
        int showCunt = getLikeUser().size();
        //最多显示四位用户名
        if (limit && showCunt > 4) {
            showCunt = 4;
        }

        // 如果已经点赞，始终让该用户在首位。通过循环点赞的用户列表，判断是否有该用户，如果有该用户，那么就删除该用户，再将该用户添加到首位。
        if (getIsLike() == 1) {
        	
        	//用户id假设为3
            for (int i = 0; i < getLikeUser().size(); i++) {
                if (getLikeUser().get(i).getUser_id() == getLoginUser().getUser_id()) {
                    getLikeUser().remove(i);
                }
            }
            getLikeUser().add(0, getLoginUser());
        }
        
        for (int i = 0; i < showCunt; i++) {
            sbBuilder.append(getLikeUser().get(i).getUser_name() + "、");
        }
        
        //去掉最后一个“、”
        String likeUsersStr = sbBuilder
                .substring(0, sbBuilder.lastIndexOf("、")).toString();

        // 第一个赞图标
        // ImageSpan span = new ImageSpan(AppContext.getInstance(),
        // R.drawable.ic_unlike_small);
        SpannableString spanStr = new SpannableString("");
        // spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(likeUsersStr);

        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = likeUsersStr.indexOf(name) + spanStr.length();
                final int index = i;
                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        UserEntity user = getLikeUser().get(index);
                        Toast.makeText(YoutiApplication.getInstance(), "进入该用户主页", 0).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        // ds.setColor(R.color.link_color); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
                
            }
        }      
            return ssb.append("觉得很赞");
        
    }
	private UserEntity getLoginUser() {
		UserEntity user= new UserEntity("3","http://192.168.1.37:8080/protrait.png","张三");
		return user;
	}
	
	private UserEntity getOtherUser(){
		UserEntity user= new UserEntity("1","http://192.168.1.37:8080/other.png","李四");
		return user;
	}
}
