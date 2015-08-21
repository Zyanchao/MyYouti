package com.youti.appConfig;

/**
 *  @Description 配置信息
 */
public class Constants {
	
	
	
	/**环信 客服 im 号**/
	public final static String  CHAT_KEFU_NAME1 = "yoti_kefu1";
	public final static String  CHAT_KEFU_NAME = "优体客服";
	/**环信 客服 im 号**/
	public final static String  CHAT_KEFU_NAME2 = "yoti_kefu2";
	
	/**环信 客服 im 号**/
	public final static String  CHAT_XITONG_TONGZHI = "xitongxiaoxi";
	/**环信 客服 im 号**/
	public final static String  CHAT_XITONG_NAME = "xitongxiaoxi";
	/** 应用默认值：课时数量增加或减少的幅度 */
	public final static int DEFAULT_NUMBER_ADD_OR_REDUCE = 1;
	/** 页面 跳转 参数 名称  id  */
	public final static String KEY_ID = "id";
	
	
	/** 页面 跳转 参数 名称  id  */
	public final static String KEY_TITLE = "title";
	/** 页面 跳转 参数 名称  用户 id  */
	public final static String KEY_USER_ID = "user_id";
	/** 页面 跳转 参数 教练介绍 对象   */
	public final static String KEY_COACH_DETAIL = "coachDetail";
	
	/** 页面 跳转 参数 私教课 对象   */
	public final static String KEY_ORDER_COACH = "orderCoach";
	
	
	/** 页面 跳转 参数 私教课 对象   */
	public final static String KEY_ORDER_COURSE = "orderCourse";
	/** 页面 跳转 参数 环信 用户名   */
	public final static String KEY_CHAT_USERNAME = "userName";
	
	/**环信 后缀 环信 用户名   规则  手机号+“3”*/
	public final static String CHAT_CODE = "3";
	public final static String CHAT_COACH_CODE = "2";
	/** 页面 跳转 参数 环信 tel   */
	public final static String KEY_CHAT_TEL = "userId";
	/** 页面 跳转 参数 环信 头像  */
	public final static String KEY_CHAT_AVATAR = "avatar";
	
	/** 页面 跳转 参数 名称  code  */
	public final static String KEY_CODE = "code";
	
	
	
	
	
	/** 首页  页面 跳转名称  code */
	public final static int REQUEST_CODE_HOME_ACTIVE = 1101;
	
	/** 支付相关  页面 跳转名称  code */
	public final static int REQUEST_CODE_PAY_COACH = 1102;
	public final static int REQUEST_CODE_PAY_ACTIVE = 1103;
	public final static int REQUEST_CODE_PAY_COURSE = 1104;
	public final static int REQUEST_CODE_PAY_TONGKA = 1105;
	
	
	/** 教练 页面 跳转名称  code */
	public final static int REQUEST_CODE_COACH = 1301;
	/** 课程 页面 跳转名称  code */
	public final static int REQUEST_CODE_COURSE = 1302;
	/** 视频 页面 跳转名称  code */
	public final static int REQUEST_CODE_VIDEO= 1303;
	
	
	
	
	/** 教练 页面 跳转名称  code */
	public final static int REQUEST_CODE_DETAIL_COACH = 1401;
	/** 课程 页面 跳转名称  code */
	public final static int REQUEST_CODE_DETAIL_COURSE = 1402;
	/** 视频 页面 跳转名称  code */
	public final static int REQUEST_CODE_DETAIL_VIDEO= 1403;
	
	
	/**优惠券 跳转 相关**/
	public final static int REQUEST_CODE_YOUHUI_HOME= 1501;
	
	public final static int REQUEST_CODE_YUE_HOME= 1502;
	/** 应用默认值：课时 最大 数量 */
	public final static int MAX_NUMBER_ADD_OR_REDUCE = 99;
	
	/** 请求 图片前缀   */
	public final static String PIC_CODE = "http://112.126.72.250/ut_app";
	
	
	
	/**支付宝支付 前缀代码*/
	public static final String CHANNEL_UPMP = "upmp";
	/**微信支付  前缀代码*/
	public static final String CHANNEL_WECHAT = "wx";
	/**银联支付 前缀代码*/
	public static final String CHANNEL_ALIPAY = "alipay";
	
	public static final String GET_SCODE_MSG = "http://api.holylandsports.com.cn/sms/send"; //获取短信验证码
	public static final String HONG_BAO = "http://api.holylandsports.com.cn/redbag/limittime"; //红包
	public static final String SEND_SCODE_MSG = "http://api.holylandsports.com.cn/sms/valid";//提交短信获取的验证码
	public static final String GET_SCODE_MSG2 = "http://api.holylandsports.com.cn/sms/send"; //获取短信验证码2
	public static final String SEND_SCODE_MSG2 = "http://api.holylandsports.com.cn/sms/valid";//提交短信获取的验证码2
	public static final String CHECK_SAME_NAME = "http://api.holylandsports.com.cn/user/named";//检查用户名是否相同
	public static final String USER_REGISTER = "http://api.holylandsports.com.cn/user/reg";//用户提交注册信息
	public static final String GET_DATA = "http://112.126.72.250/ut_app/index.php?m=Coach&a=week_time";//教练详情时间表
	
	
	public static final String HOME_SERACH = "http://112.126.72.250/ut_app/index.php?m=Advert&a=show_search";//首页搜索
	public static final String HOME_CAROUSEL = "http://112.126.72.250/ut_app/index.php?m=Advert&a=show_top";//首页轮播
	public static final String HOME_PROJECTS_RECOMMENDED  = "http://112.126.72.250/ut_app/index.php?m=Advert&a=four_hot_type";//首页四个色块
	public static final String  HOME_HOT_ITEM= "http://112.126.72.250/ut_app/index.php?m=Advert&a=more_type";//首页热门项目
	public static final String  HOME_MORE_ITEM= "http://112.126.72.250/ut_app/index.php?m=Advert&a=more_type";//首页更多 项目
	public static final String  HOME_POPULAR_COACH = "http://112.126.72.250/ut_app/index.php?m=Advert&a=hot_coach_course";//首页热门教练
	public static final String  HOME_POPULAR_COURSE = "http://112.126.72.250/ut_app/index.php?m=Advert&a=hot_coach_course";//首页热门课程
	public static final String  HOME_ACTIVE_GJC = "http://112.126.72.250/ut_app/index.php?m=Fourevent&a=tuanticao";// 工间操 
	
	
	public static final String  HOME_POPULAR_EVENT = "http://112.126.72.250/ut_app/index.php?m=Fourevent&a=eventList";//首页四项详情
	public static final String  HOME_POPULAR_BOOK = "http://112.126.72.250/ut_app/index.php?m=Fourevent&a=signup";//首页四项 报名列表
	public static final String  HOME_FLITER_ITEM = "http://wtapp.yoti.cn/index.php/Coach/ser_list";//筛选 条件 教练 
	public static final String  HOME_FLITER_ITEM_COURSE = "http://wtapp.yoti.cn/index.php/Course/ser_list";//筛选 条件  课程
	
	
	public static final String  COACH_COMMENT="http://112.126.72.250/ut_app/index.php?m=Coach&a=add_coach_comment";//教练订单评论
	public static final String  COACH_TOP_LIST = "http://112.126.72.250/ut_app/index.php?m=Coach&a=coach_top";//教练头条列表
	public static final String  COACH_TOP_RULE = "http://112.126.72.250/ut_app/index.php?m=Coach&a=top_rule";//教练头条 规则
	public static final String 	COACH_LIST_PRAISE="http://112.126.72.250/ut_app/index.php?m=User&a=praise_coach";//教练点赞
	public static final String  COACH_LIST = "http://112.126.72.250/ut_app/index.php?m=Coach&a=coach_list";//教练列表
	public static final String  COACH_DETAIL = "http://112.126.72.250/ut_app/index.php?m=Coach&a=coach_info";//教练详情
	public static final String  COACH_DETAIL_COMMENT = "http://112.126.72.250/ut_app/index.php?m=Coach&a=coach_comment_list";//教练详情- 评论
	public static final String  ISSUE_DETAIL_COACH_JS = "http://112.126.72.250/ut_app/index.php?m=Course&a=user_desc";//发布 评论
	public static final String  ISSUE_COMMENT = "http://112.126.72.250/ut_app/index.php?m=Coach&a=add_coach_comment";//发布 评论
	
	public static final String   CLUB_BGPIC = "http://112.126.72.250/ut_app/index.php?m=Course&a=backg_img";//获取俱乐部背景图片
	public static final String   CLUB_COURSE = "http://112.126.72.250/ut_app/index.php?m=Course&a=other_course";//俱乐部 其他课程
	public static final String   CLUB_DETAIL = "http://112.126.72.250/ut_app/index.php?m=Course&a=club_about";//俱乐部 介绍
	
	/*课程列表头部的轮播*/
	public static final String COURSE_COMMENT="http://112.126.72.250/ut_app/index.php?m=Course&a=add_course_comment";//课程评论
	public static final String COURSE_TONGKA = "http://112.126.72.250/ut_app/index.php?m=Course&a=tonka_list";//99 元 活动 通卡 接口
	public static final String COURSE_CAROUSEL = "http://112.126.72.250/ut_app/index.php?m=Course&a=courde_deimg";//课程轮播图
	public static final String  COURSE_LIST = "http://112.126.72.250/ut_app/index.php?m=Course&a=course_list";//课程 列表
	public static final String  COURSE_DETAIL = "http://112.126.72.250/ut_app/index.php?m=Course&a=course_info";//课程详情
	public static final String  COURSE_DETAIL_COACH_JS = "http://112.126.72.250/ut_app/index.php?m=Course&a=coach_desc";//课程详情 -教练介绍
	public static final String  COURSE_DETAIL_COMMENT = "http://112.126.72.250/ut_app/index.php?m=Course&a=course_comment_list";//课程详情- 评论
	public static final String  COURSE_LIST_PRAISE="http://112.126.72.250/ut_app/index.php?m=User&a=praise_course";//课程列表点赞
	public static final String  NEW_XM = "http://112.126.72.250/ut_app/index.php?m=Advert&a=more_type";//全部项目
	public static final String  NEW_FAST_RESERVATION = "http://api.holylandsports.com.cn/index.php/newpapge/reservation";//提交快速预定
	
	public static final String  BOOK_COURSE_FLAG = "http://112.126.72.250/ut_app/index.php?m=User&a=appoint_class";//预约 课程 判断
	
	/*订单相关 **/
	
	public static final String ORDER_COACH  = "http://112.126.72.250/ut_app/index.php?m=User&a=sert_order";//提交 教练订单
	public static final String ORDER_COURSE  = "http://112.126.72.250/ut_app/index.php?m=User&a=course_order";//提交 教练订单
	public static final String ORDER_MESS  = "http://112.126.72.250/ut_app/index.php?m=User&a=send_mess";//付款成功 短信提醒接口
	
	/**账户 信息 ，充值列表*/
	
	public static final String RECHARGE_LIST  = "http://112.126.72.250/ut_app/index.php?m=User&a=recharge_list";//充值列表
	public static final String PAY_RECHARGE = "http://112.126.72.250/ut_app/index.php?m=User&a=recharge";// 充值支付接口
	public static final String PAY_YUE = "http://112.126.72.250/ut_app/index.php?m=User&a=yupay";// 账户支付 接口
	public static final String PAY_TONGKA_YUE = "http://112.126.72.250/ut_app/index.php?m=User&a=yu_ton";//通卡 余额 账户支付
	public static final String PAY_ACTIVE_YUE = "http://112.126.72.250/ut_app/index.php?m=User&a=pay_four";//活动页 余额 账户支付
	
	
	/**支付相关接口*/
	
	public static final String PAY_ORDER_COACH = "http://112.126.72.250/ut_app/index.php?m=User&a=pay";//教练 私教课 支付接口
	public static final String PAY_ORDER_COURSE = "http://112.126.72.250/ut_app/index.php?m=User&a=course_charge";//课程 支付接口
	public static final String PAY_ORDER_TONGKA = "http://112.126.72.250/ut_app/index.php?m=User&a=tonka_charge";//通卡 支付接口
	public static final String PAY_ORDER_ACTIVE = "http://112.126.72.250/ut_app/index.php?m=User&a=tonka_charge";//活动页 支付接口
	/**
	 * 视频
	 */
	public static final String VIDEO_PRAISE="http://112.126.72.250/ut_app/index.php?m=Video&a=praise_video";//视频点赞
	public static final String COURSE_PACKEG = "http://112.126.72.250/ut_app/index.php?m=User&a=click_buy_coach";//課程 套餐
	
	
	
	public static final String UPLOAD_PICTURE ="http://api.holylandsports.com.cn/user/uppic";//上传头像
	public static final String GET_DATA_LIST = "http://api.holylandsports.com.cn/timetable/timelist";//预定时间完成列表
	public static final String GET_SURPLUS_DATA = "http://api.holylandsports.com.cn/timetable/rest";//获取订单剩余时间
	public static final String GET_BESPEAK_DATA = "http://api.holylandsports.com.cn/timetable/order";//预约时间
	public static final String REFER_BESPEAK_DATA = "http://api.holylandsports.com.cn/timetable/confirm_order";//提交时间课程选择
	public static final String COURSE_START_HINT = "http://api.holylandsports.com.cn/timetable/tips";//课程开始提醒 
	public static final String COURSE_STAR = "http://api.holylandsports.com.cn/timetable/isallow";//判断是否允许结束课程
	public static final String COURSE_START_COMPLAIN = "http://api.holylandsports.com.cn/timetable/complain";//投诉
	public static final String AROUND_THE_QUERY  ="http://api.holylandsports.com.cn/nearby/neighbour";//查询周围
	public static final String UPDATE_THE_LATITUDE_AND_LONGITUDE   ="http://api.holylandsports.com.cn/nearby/upnearuser";//更新经纬度
	public static final String FORGET_GET_SCODE_MSG = "http://api.holylandsports.com.cn/user/findpwd";//找回密码时，获取短信验证码
	public static final String CHANGE_PWD = "http://api.holylandsports.com.cn/user/changepwd";//修改密码
	public static final String CHANGE_PWD_2 = "http://api.holylandsports.com.cn/user/upwd";//修改密码
	public static final String CHANGE_HEADIMG = "http://api.holylandsports.com.cn/user/uptx";//修改头像
	public static final String ITEM_CATEGORY = "http://api.holylandsports.com.cn/ping/gettype";//获取项目类别列表
	public static final String SPELL_LIST = "http://api.holylandsports.com.cn/ping/pinglist";//拼单列表
	public static final String SPELL_DETAILS = "http://api.holylandsports.com.cn/ping/getdetail";//获取拼单详情
	public static final String UPLOAD_IMG = "http://api.holylandsports.com.cn/photo/upload";//上传图片
	public static final String PAY_UNIONPAY = "http://api.holylandsports.com.cn/unionpay/pay";//获取银联流水号
	public static final String TZ_UNIONPAY = "http://api.holylandsports.com.cn/unionpay/pay_back";//通知后台成功
	public static final String GET_USER_INFO = "http://api.holylandsports.com.cn/user/info";//查看个人信息
	public static final String UPDATE_USER_INFO = "http://api.holylandsports.com.cn/user/upself";//跟新个人资料
	public static final String CHECK_COACH_COUNT = "http://api.holylandsports.com.cn/user/coachnum";//获取教练总数
	public static final String SEArCH_COACH_TYPE = "http://api.holylandsports.com.cn/course/type";//查询某类型的教练
	public static final String CHECK_COACH_FAV = "http://api.holylandsports.com.cn/user/isfav";//查询是否收藏了教练
	public static final String COLLECTION_COACH = "http://api.holylandsports.com.cn/user/fav";//收藏教练
	public static final String UN_COLLECTION_COACH = "http://api.holylandsports.com.cn/user/unfav";//取消收藏教练
	public static final String CHECK_COACH_PRAISE = "http://api.holylandsports.com.cn/user/ispraise";//查询是否收藏了教练
	public static final String PRAISE_COACH = "http://api.holylandsports.com.cn/user/praise";//赞教练
	public static final String UN_PRAISE_COACH = "http://api.holylandsports.com.cn/user/unpraise";//查询是否收藏了教练
	public static final String COACH_INFO = "http://api.holylandsports.com.cn/user/cinfo";//获取教练详细信息
	public static final String COACH_CMTS = "http://api.holylandsports.com.cn/order/cmts";//教练评论信息
	public static final String MAKE_ORDER = "http://api.holylandsports.com.cn/order/generate";//提交订单
	//public static final String PAY_ORDER = "http://api.holylandsports.com.cn/order/pay";//支付
	public static final String PAY_SHORT = "http://api.holylandsports.com.cn/order/alipay_short";//支付宝快捷支付
	public static final String PAY_SHORT_BACK = "http://api.holylandsports.com.cn/order/alipay_short_back";//支付宝回复成功
	public static final String START_ORDER = "http://api.holylandsports.com.cn/order/start";//申请开始
	public static final String CONFIRM_ORDER = "http://api.holylandsports.com.cn/order/confirm";//确认开始
	public static final String CANCEL_ORDER = "http://api.holylandsports.com.cn/order/cancel";//取消订单
	public static final String OVER_ORDER = "http://api.holylandsports.com.cn/order/finish";//结束订单
	public static final String COMMENT_ORDER = "http://api.holylandsports.com.cn/order/comment";//评论订单
	public static final String DELET_ORDER = "http://api.holylandsports.com.cn/order/del";//删除历史订单
	public static final String CURRENT_ORDER = "http://api.holylandsports.com.cn/order/cur";//当前订单
	public static final String HISTORY_ORDER = "http://api.holylandsports.com.cn/order/history";//历史订单
	public static final String ORDER_INFO = "http://api.holylandsports.com.cn/order/info";//订单详情
	public static final String HAIL_FELLOW = "http://112.126.72.250/ut_app/index.php?m=User&a=build_relation";//建立关系
	public static final String GET_FELLOW = "http://api.holylandsports.com.cn/huanxing/friend";//获取好友列表
	public static final String DELETE_FELLOW = "http://api.holylandsports.com.cn//huanxing/delcoach";//删除好友
	public static final String BUILD_TEACHERSANDSTUDENTS = "http://api.holylandsports.com.cn//huanxing/teacher";//删除好友
	public static final String UNUSE_COUPONS = "http://api.holylandsports.com.cn/coupon/unuse";//未使用优惠券
	public static final String USED_COUPONS = "http://api.holylandsports.com.cn/coupon/used";//使用过优惠券
	public static final String EXCHANGE_COUPONS = "http://api.holylandsports.com.cn/coupon/getbykey";//兑换优惠券
	public static final String USER_YE = "http://api.holylandsports.com.cn/user/ye";//账户余额
	public static final String ALI_PAY = "http://api.holylandsports.com.cn/order/alipay";//账户充值
	public static final String MY_COLLECTION_COACHS = "http://api.holylandsports.com.cn/user/myfav";//我收藏的教练
	public static final String COACH_TYPE_LIST = "http://api.holylandsports.com.cn/user/typelst";//教练类型列表
	public static final String CITY_ADR_LIST = "http://api.holylandsports.com.cn/site/county";//城市内地址列表
	public static final String COACH_RANK = "http://api.holylandsports.com.cn/user/rank_demo";//教练排行榜
	public static final String COACH_SEARCH = "http://api.holylandsports.com.cn/user/so_demo";//搜索教练
	public static final String COACH_SEARCH_NAME = "http://api.holylandsports.com.cn/user/sobyname";//搜索教练姓名
	public static final String CHECK_ONLINE = "http://api.holylandsports.com.cn/user/isonline";//搜索教练
	public static final String LOGIN_SERVER = "http://112.126.72.250:9915";//登陆服务0
	public static final String MAIN_SERVER = "http//112.126.72.250:9901";//其他MSG服务等
	public static final String MSG_SERVER_IP = "112.126.72.250";
	public static final int MSG_SERVER_PORT = 9901;
	public static final String SCHEDULE_SERVER_IP = "112.126.72.250";
	public static final int SCHEDULE_SERVER_PORT = 9901;
	public static final String LOACTION_SERVER = "http://112.126.72.6:9916";//位置服务
	public static final int LOACTION_SERVER_LOGIN_ACTION = 0;//登陆
	public static final int LOACTION_SERVER_ADD_ACTION = 1;//add
	public static final int LOACTION_SERVER_DELET_ACTION = 2;//del
	public static final int LOACTION_SERVER_QUERY_ACTION = 3;//query
	public static final String COACH_LIST_INFO = "http://api.holylandsports.com.cn/user/coachinfobyids";//批量查询教练信息
	public static final String USER_PROTOCOL = "http://api.holylandsports.com.cn/html/user/sjxy.html";//用户协议
	public static final String PAY_PASS_ALREADY = "http://api.holylandsports.com.cn/user/ispwd";//是否有支付密码
	public static final String PAY_PASS_SET = "http://api.holylandsports.com.cn/user/setpaypwd";//设置支付密码
	public static final String PAY_PASS_CHANGE = "http://api.holylandsports.com.cn/user/uppaypwd";//修改支付密码
	public static final String PAY_PASS_GET_SCODE = "http://api.holylandsports.com.cn/sms/sendsms";//发送短信验证码
	public static final String PAY_PASS_SEND_SCODE = "http://api.holylandsports.com.cn/sms/valid";//发送短信验证码
	public static final String PAY_PASS_CHANGE_BY_MSG = "http://api.holylandsports.com.cn/user/findpaypwd";//提交新的支付密码
	public static final String SEND_FEEDBACK = "http://api.holylandsports.com.cn/app/feedback";//发送反馈信息
	public static final String CHECK_UPDATE = "http://api.holylandsports.com.cn/app/update";//检查版本更新
	public static final String COMMUNITY_LIST = "http://api.holylandsports.com.cn/bbs/posts";//社区获取文章列表
	public static final String COMMUNITY_LIST_OWN = "http://api.holylandsports.com.cn/bbs/myposts";//社区获取文章列表
	public static final String COMMUNITY_DETAIL_INFO = "http://api.holylandsports.com.cn/bbs/info";//获取文章详细信息(包括评论)
	public static final String COMMUNITY_REPLY = "http://api.holylandsports.com.cn/bbs/cmt";//评论
	public static final String COMMUNITY_SEND_CARD = "http://api.holylandsports.com.cn/bbs/post";//发帖	
	public static final String COMMUNITY_DELET_CARD = "http://api.holylandsports.com.cn/bbs/del";//删除	
}
