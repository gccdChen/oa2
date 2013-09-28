package scau.duolian.oa.base;

import android.os.Environment;

public class C {
	public static final String MODELPACKAGE = "scau.duolian.oa.model.";
	/////// 目录
	public static final class dir{
		public static final String base				= "/sdcard/oa";
//		public static final String base				= Environment.getExternalStorageDirectory().getPath()+"/oa";
//		public static final String base				= Environment.getDataDirectory().getPath()+"/oa";
		public static final String temp 				= base + "/temp";
	}
	/////// api
	public static final class api {

		public static final String BASE 				= "http://www.duolia.com/yun/pad";
		public static final String login					= "/ywyl.jsp";
		public static final String reg					= "/regist_do.jsp";
		public static final String data					= "/xtl.jsp";
		//修改个人信息
		public static final String upadatePersonInfo					= "/hblb.jsp";
		
		public static final String addParnerCom = "/tl.jsp";
		public static final String addTask = "/rwlb.jsp";
		public static final String addProject = "/xmlb.jsp";
		public static final String addFlow = "/lclb.jsp";
		//项目
		public static final String forbiddenPro = "/xmlb.jsp";
		//通知
		public static final String notice = "tzxq.jsp";
		
		//伙伴
		public static final String parnerCom = "/hblb.jsp";
		public static final String addPartner = "/hblb.jsp";
		public static final String forbiddenParner = "/hblb.jsp";
		public static final String partnerCalender = "/rclb.jsp";
		//任务
		public static final String rwComment = "/rwlb.jsp";
		public static final String sendRwComment = "/tl.jsp";
		public static final String forbiddenTask = "/rwlb.jsp";
		//日程
		public static final String calComment = "/rclb.jsp";
		public static final String createRw = "/rwlb.jsp";
		public static final String sendCalComment = "/tl.jsp";
		public static final String addCalender = "/rclb.jsp";
		
		//里程碑
		public static final String addMilestone = "/lcblb.jsp";
		public static final String editMilestone = "/lcblb.jsp";
		public static final String forbiddenLcb = "/lcblb.jsp";
		//流程
		public static final String handlerFlow = "/lclb.jsp";
		public static final String sendFlowComment = "/lclb.jsp";
		public static final String flowDetail = "/lclb.jsp";
		
		//通知
		public static final String noticeDetail = "/tzxq.jsp";
		//签到
		public static final String qiandao = "/ywykq.jsp";
	}
	////// taskid
	public static final class task {
		public static final  int sys_notice = 0;//系统通知，全系统公告
		public static final  int notice = 1;//通知，加入或退出团队/项目/订单/流程/任务
		public static final  int partner_discuss = 2;//团队讨论
		public static final  int order = 3;//订单
		public static final  int calender_reply = 4;//日程点评
		public static final  int flow = 5;//流程
		public static final  int pro_message = 6;//项目消息
		public static final  int pro_discuss = 7;//项目讨论
		public static final  int task_discuss = 8;//任务讨论
		public static final  int personal_message = 9;//个人消息
		public static final  int milestone = 10;//里程碑
		public static final  int questionnaire = 11;//问卷
		public static final  int exam = 12;//考试
	}
	////// 参数
	public static final class params{
		public static final String id 		="id";
		public static final String dlyid 		="dlyid";
		public static final String uid 		="uid";
		public static final String username 		="username";
		public static final String password 		="password";
		public static final String mac 				="mac";
		public static final String title = "title";
		public static final String mm = "mm";
		public static final String mm2 = "mm2";
		public static final String sj = "sj";
		public static final String qq = "qq";
		public static final String file = "file";
		public static final String phone = "phone";
		
		public static final String regName = "RegName";
		public static final String password1 = "Password";
		public static final String password2 = "Password2";
		public static final String validateCode = "validateCode";
		public static final String hbid = "hbid";
		
		public static final String bz = "bz";
		
		
	}
	////// 设置
	public static final class config{
		public static final String user 		="user";
		public static final String mac 		="mac";
		public static final String dlyid 		="dlyid";
		public static final String uid 		="uid";
		public static final String username 		="username";
		
	}
	
	////// 响应码
	public static final class response{
		public static final int success 		=999;
	}
	
	///// modelname
	public static final class model{
		public static final String user 		="user";
		public static final String message 		="message";
		public static final String wdhb 		="wdhb";
		public static final String wdxm 		="wdxm";
		public static final String wdrwlx 		="wdrwlx";
		public static final String wdrw 		="wdrw";
		public static final String wdlcb 		="wdlcb";
		public static final String wdlclx 		="wdlclx";
		public static final String wdlc 		="wdlc";
		public static final String wdwj 		="wdwj";
		public static final String wdks 		="wdks";
		public static final String wdbm 		="wdbm";
		public static final String wdrc 		="wdrc";
		public static final String packet 		= "scau.duolian.oa.model.";
	}
	
	public static final class array{
		// 项目 0一般；1重要；2紧急
		public static final String[] proType = {
			"一般","重要","紧急"
		};
		//项目状态 
		public static final String[] proStatus = {
			"未开始","进行中","完成"
		};
		//流程 当前步骤，0未开始；1开始；大于1则为第几步；-1结束；-2；完成
		// 流程等级 0普通；1重要；2紧急
		public static final String[] flowLevel = {
			"普通","重要","紧急"
		};
	}
}
