package scheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserTask implements Comparable<ParserTask>{
	private static final String submissionListURL = ".*/submissions/$"; // 检验是否是题目提交列表页面
	private static final String problemCodeURL = ".*/submissions/detail/.*"; // 检验是否是题目代码页面
	private static final String problemListURL = ".*/problemset/algorithms/$"; // 检验是否是题目列表页面

	static enum TaskType {
		GET_PROBLEM_LIST_URL, GET_SUBMISION_URL, GET_CODE_URL, GET_CODE
	}

	private String url;
	private TaskType type;

	public ParserTask(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
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

	public boolean checkPattern(String url, String patternString) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(url);
		boolean res = matcher.matches();
		return res;
	}

	public int compareTo(ParserTask o) {
		return o.type.ordinal()-this.type.ordinal();
	}
}
