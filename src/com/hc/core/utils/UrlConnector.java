package com.hc.core.utils;

public class UrlConnector {

	public static final String BASE_URL1 = "http://api.handone.com:8088/v1/";
	public static final String BASE_URL2 = "http://api.handone.com:8088/";

	public static final String BASE_URL = "http://123.56.136.12:8088/v1/";

	/** 版本更新 */
	public final static String BANBEN_UPDATE = "http://42.121.108.116:8833/api/index/banben_update";
	/** 导航栏的broadcastreceiver */
	public static final String NAVIGATION_RECEIVER = "com.aim.mallapp.navigation";
	public final static String SIGN = "&sign=";
	// public final static String LOGIN = BASE_URL1 + "user/user/login";

	/** 登陆 */
	public static final String LOGIN_URL = BASE_URL + "user/user/login";
	/** qq登陆 */
	// public static final String QQ_LOGIN =
	// "http://api.handone.com/v1/user/user/qq-login";
	public static final String QQ_LOGIN = BASE_URL1 + "user/user/qq-login";
	/** wx登陆 */
	public static final String XEIXIN_LOGIN = BASE_URL1 + "user/user/wx-login";
	/** 请求手机验证码 */
	public static final String PHONE_VERCODE = BASE_URL + "user/sms/send";
	/** 手机验证 */
	public static final String PHONE_VER = BASE_URL + "user/sms/valid";
	public final static String ONE = BASE_URL1 + "document/document/tasklist?access-token=";
	/** 我的还款 */
	public final static String MYPAYMENT = BASE_URL1 + "payment_info/payment-infos?access-token=";
	/** 我的消息 */
	public final static String MYMESSAGE = BASE_URL1 + "message/messages?access-token=";
	public final static String NEW_STATUS = "&news_status=";
	public final static String NEW_PAGE = "&page=";
	/** 消息详情 */
	public final static String MYMESSAGE_DETAIL = BASE_URL1 + "message/messages/";
	/** 删除消息 */
	public final static String MYMESSAGE_DELECT = BASE_URL1 + "message/messages/";
	/** 查看个人信息接口 */
	public final static String PERSONALINFO = BASE_URL1 + "house/house/user-view?access-token=";
	/** 个人认证检测 */
	public final static String CERTIFY_TEST = BASE_URL1 + "user/userstat/home?access-token=";
	/** 编辑个人信息接口 */
	public final static String PERSONALINFO_EDIT = BASE_URL1 + "house/house/edit?access-token=";
	/** 编辑个人头像接口 */
	public final static String PERSONALAVATOR_EDIT = BASE_URL1 + "user/userstat/avatar-upload?access-token=";
	/** 我的编号 */
	public static final String MYNUM = BASE_URL + "user/user/agent?access-token=";
	/** 委托找房获取面积 */
	public final static String SIZELIST = BASE_URL1 + "house/house/area?access-token=";
	/** 委托找房获取租金 */
	public final static String PRICELIST = BASE_URL1 + "house/house/price?access-token=";
	/** 委托找房提交 */
	public final static String ROOMSUBMIT = BASE_URL1 + "house/house/commit?access-token=";
	/** 续租房源或停租申请接口 */
	public final static String MYRENTS_OPERATION = BASE_URL1 + "house/myrents/";
	/** (普通用户)租房列表接口 */
	public final static String MYRENTS_ITEMLIST = BASE_URL1 + "house/myrents?access-token=";
	/** 我的房源收藏列表接口 */
	public final static String MYCOLLECTION = BASE_URL1 + "house/favorites?access-token=";
	/** 删除我的收藏接口 */
	public final static String MYCOLLECT_DELETE = BASE_URL1 + "house/favorites/";
	/** 新增我的收藏接口 */
	public final static String MYCOLLECTION_ADD = BASE_URL1 + "house/favorites?access-token=";
	/** 我的委托列表接口 */
	public final static String MYENTRUST = BASE_URL1 + "house/house/commit-list?access-token=";
	/** 删除我的委托接口 */
	public final static String MYENTRUST_DELETE = BASE_URL1 + "house/house/commit-delete?access-token=";
	/** 查看我的委托详情 */
	public final static String MYENTRUST_DETAIL = BASE_URL1 + "house/house/commit-view?access-token=";
	/** 委托看房接口 */
	public final static String DEPUTELOOKROOM = BASE_URL1 + "house/house/mycommit?access-token=";
	/** 我的房源列表接口 */
	public final static String MYROOMLIST = BASE_URL1 + "house/house/listing?access-token=";
	/** 我的房源详情接口 */
	public final static String MYROOMDETAIL = BASE_URL1 + "house/house/house-update-view?access-token=";
	/** 我的房源修改接口 */
	public final static String MYROOMCHANGE = BASE_URL1 + "house/houses/";
	/** 我的房源上,下架接口 */
	public final static String MYROOM_UP_DOWN = BASE_URL1 + "house/house/update-sale?access-token=";
	/** 查看我的经纪人 */
	public final static String MYAGENT = BASE_URL1 + "house/agent/agentinfo?access-token=";
	/** 更换经纪人 */
	public final static String MYAGENT_CHANGE = BASE_URL1 + "house/agent/change?access-token=";
	/** 功能与介绍接口 */
	public final static String MYFUNCTION = BASE_URL1 + "house/house/about?access-token=";
	/** 经纪人与房产证审核接口 */
	public final static String VERIFY = BASE_URL1 + "user/user/verify?access-token=";
	/** 请求用户类型 */
	public final static String GETUSERTYPE = BASE_URL1 + "user/user/type?access-token=";
	/** 设置用户类型 */
	public final static String SETGETUSERTYPE = BASE_URL1 + "user/user/settype?access-token=";
	/** 关于我们 */
	public static final String ABOUT_US = BASE_URL1 + "house/house/about?access-token=";
	/** 意见反馈 */
	public static final String FEEDBACK = BASE_URL + "feedback/feedback/newfeedback?access-token=";
	/** 联系我们 */
	public static final String CONTACT_US = BASE_URL1 + "house/house/contact?access-token=";
	/** 用户注册 */
	public static final String REGISTER_USER = BASE_URL + "user/user/regadv";
	/** 忘记密码 */
	public static final String FROGETPSD = BASE_URL + "user/uservalidate/editpassphone";

	/** 首页 */
	public final static String INDEX = BASE_URL1 + "house/house/home";
	/** 重置密码 */
	public final static String UPDATE_PAS = BASE_URL1 + "user/uservalidate/editpass";
	/** 设置-修改密码 */
	public final static String CHANGE_PAS = BASE_URL1 + "user/user/updatepass?access-token=";
	/** 设置-个人信息 */
	public final static String PERSONAL_INFORMATION = BASE_URL1 + "user/userstat/detail?access-token=";
	/** 设置-个人信息-头像提交 */
	public final static String COMMIT_HEAD_PIC = BASE_URL1 + "user/userstat/avatar-upload?access-token=";
	/** 设置-个人信息-姓名提交 */
	public final static String COMMIT_NAME = BASE_URL1 + "user/userstat/editname?access-token=";
	/** 文件类型 */
	public final static String FILE_CATE = BASE_URL1 + "document/dfile/type_list?access-token=";
	/** 手机绑定 */
	public static final String PHONEBIND = BASE_URL1 + "user/phonebinds?access-token=";
	/** 手机验证码 */
	public static final String PHONE_VERIFICATION = BASE_URL1 + "user/uservalidate/phonecode";
	/** 我的足迹 */
	public static final String FOOT_PRINTS = BASE_URL1 + "house/footprints?access-token=";
	/** 添加足迹 */
	public static final String ADD_FOOT_PRINTS = BASE_URL1 + "house/footprints?access-token=";
	/** 园区列表 */
	public static final String HOUSE_LIST = BASE_URL1 + "house/houses";
	/** 新上房源 */
	public static final String NEW_HOUSE = BASE_URL1 + "house/house/new";
	/** 园区详情 */
	public static final String HOUSE_DETAIL = BASE_URL1 + "house/houses/";
	public static final String HOUSE_DETAIL_UNLOGIN = BASE_URL1 + "house/house/view2";
	/** 佣金收入列表 */
	public static final String INCOME_DETAIL = BASE_URL1 + "house/agent/charge?access-token=";
	/** 佣金总收入 */
	public static final String INCOME_ALL = BASE_URL1 + "house/agent/chargeall?access-token=";
	/** 佣金折线图 */
	public static final String INCOME_GRAPH = BASE_URL2 + "agent/chargeall?access-token=";
	/** 分享图标（这个图标用于测试） */
	public final static String SHARE_ICON = "http://qd.tongshuai.com:5657/site/static/images/mubiao.png";
	/** 获取面积筛选项 */
	public static final String AREA = BASE_URL1 + "house/house/area";
	/** 获取地区筛选项 */
	public static final String DECRATION = BASE_URL1 + "house/house/redecorate";
	/** 获取价格筛选项 */
	public static final String PRICE = BASE_URL1 + "house/house/price";
	/** 租房管理列表 */
	public static final String RENT_MANAGE_LIST = BASE_URL1 + "house/agents?access-token=";
	/** 租房管理详情 */
	public static final String RENT_MANAGE_DETAIL = BASE_URL1 + "house/agents/";
	/** 我要租 */
	public static final String RENT = BASE_URL1 + "house/house/rent?access-token=";
	/** 获取分期付款 */
	public static final String RECKON = BASE_URL1 + "house/house/reckon?access-token=";
	/** 实名认证获取信息 */
	public static final String CERTIFICATION_GET = BASE_URL1 + "house/house/certification-info?access-token=";
	/** 实名认证 */
	public static final String CERTIFICATION = BASE_URL1 + "user/user/certification?access-token=";
	/** 发布房源 */
	public static final String POST_HOUSE = BASE_URL1 + "house/houses?access-token=";
	/** 修改发布房源 */
	public static final String UPDATE_HOUSE = BASE_URL1 + "house/houses/";
	/** 选择户型 */
	public static final String CHOOSE_HOUSE_TYPE = BASE_URL1 + "house/house/type";
	/** 选择商圈 */
	public static final String CHOOSE_CIRCLE = BASE_URL1 + "house/house/park";
	/** 租客信息 */
	public static final String TENANT_INFORMATION = BASE_URL1 + "house/agent/zuke?access-token=";
}
