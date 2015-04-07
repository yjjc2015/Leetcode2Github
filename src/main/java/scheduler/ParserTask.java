package scheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ParserTask implements Comparable<ParserTask>{
	private static final String submissionListURL = ".*/submissions/$"; // 检验是否是题目提交列表页面
	private static final String problemCodeURL = ".*/submissions/detail/.*"; // 检验是否是题目代码页面
	private static final String problemListURL = ".*/problemset/algorithms/$"; // 检验是否是题目列表页面
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
		GET_PROBLEM_LIST_URL, GET_SUBMISION_URL, GET_CODE_URL, GET_CODE
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
		if (checkPattern(url, problemListURL)) {
			type = TaskType.GET_PROBLEM_LIST_URL;
		} else if (checkPattern(url, problemCodeURL)) {
			type = TaskType.GET_CODE_URL;
		} else if (checkPattern(url, submissionListURL)) {
			type = TaskType.GET_SUBMISION_URL;
		} else {
			type = TaskType.GET_CODE;
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
		return o.type.ordinal()-this.type.ordinal();
	}
}
