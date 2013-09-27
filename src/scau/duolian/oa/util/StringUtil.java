package scau.duolian.oa.util;

public class StringUtil {
	/**
	 * 判断是否为空
	 * @param cids
	 * @return
	 */
	public static boolean isBlank(String cids) {
		if(cids==null)
			return true;
		if(cids.trim().equals(""))
			return true;
		return false;
	}
	
	/* 首字母大写*/
	static public String ucfirst (String str) {
		if (str != null && str != "") {
			str  = str.substring(0,1).toUpperCase()+str.substring(1);
		}
		return str;
	}
	
	/**
	 * 是否符合username格式
	 * @param username
	 * @return
	 */
	public static boolean isUsernameFormat(String username){
		return true;
	}
	/**
	 * 是否符合password格式
	 * @param password
	 * @return
	 */
	public static boolean isPasswordFormat(String password){
		return true;
	}
	/**
	 * 过长截断
	 * @param s
	 * @param maxLength
	 * @param append
	 * @return
	 */
	public static String subString(String s,int maxLength,String append){
		if( maxLength<=0 || s.length() < maxLength)
			return s;
		return s.substring(0, maxLength)+append;
	}
	/**
	 * 过长截断
	 * @param s
	 * @param maxLength
	 * @return
	 */
	public static String subString(String s,int maxLength){
		return subString(s,maxLength,"...");
	}
}
