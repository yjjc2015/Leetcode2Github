package scheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ParserTask implements Comparable<ParserTask>{
	private static final String submissionListURL = ".*/submissions/$"; // 检验是否是题目提交列表页面
	private static final String problemCodeURL = ".*/submissions/detail/.*"; // 检验是否是题目代码页面
	private static final String problemListURL = ".*/problemset/algorithms/$"; // 检验是否是题目列表页面
	private static final String problemDescription = ".*/problems/.*";
	private Logger myLog;
	
	/**
	 * 
	 * TaskType
	 * 2015年4月7日 上午10:35:07
	 * 
	 * @version 1.0.0
	 *
	 */
	static enum TaskType {
		PROBLEM_URL, SUBMISION_URL, CODE_PAGE_URL
	}

	private String url;
	private TaskType type;

	/**
	 * 
	 * 创建一个新的实例 ParserTask.
	 *
	 * @param url
	 */
	public ParserTask(String url) {
		PropertyConfigurator.configure("test.log");
		myLog = Logger.getLogger(ParserTask.class);
		this.url = url;
	}
	
	/**
	 * 
	 * getUrl(获取url属性)
	 * @return
	 *String
	 * @exception
	 * @since  1.0.0
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * 
	 * getType(获取type属性)
	 * @return
	 *TaskType
	 * @exception
	 * @since  1.0.0
	 */
	public TaskType getType() {
		return this.type;
	}
	
	/**
	 * 
	 * isType(设置task的type属性)
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public void isType() {
		if (checkPattern(url, submissionListURL)) {
			myLog.debug("设定task类型为SUBMISION_URL");
			type = TaskType.SUBMISION_URL;
		} else if (checkPattern(url, problemCodeURL)) {
			myLog.debug("设定task类型为CODE_PAGE_URL");
			type = TaskType.CODE_PAGE_URL;
		} else {
			myLog.debug("设定task类型为PROBLEM_URL");
			type = TaskType.PROBLEM_URL;
		}
	}

	/**
	 * 
	 * checkPattern(判断url是否是patternString类型)
	 * @param url
	 * @param patternString
	 * @return
	 *boolean
	 * @exception
	 * @since  1.0.0
	 */
	public boolean checkPattern(String url, String patternString) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(url);
		boolean res = matcher.matches();
		return res;
	}

	/**
	 * 复写Comparable接口方法
	 */
	public int compareTo(ParserTask o) {
		return this.type.ordinal()-o.type.ordinal();
	}
}
