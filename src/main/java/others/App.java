package others;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Hello world!
 *
 */
public class App implements PageProcessor {
	protected volatile Site site;
	//submission 列表url
	protected String submissionList;
	//problem 代码url
	protected String problemCode;
	//problem 描述url
	protected String problemDescription;
	//problem 列表url
	protected String problemList;
	//初始访问页面
	protected static String url;
	//前往problem列表的path
	protected String problemLinkPath;
	//获得problem列表的path
	protected String getProblemLinkPath;
	//前往submission列表的path
	protected String submissionLinkPath;
	//前往code页面的path
	protected String codePagePath;
	//获得code对应的状态
	protected String codePageStatusPath;
	//获得code的path
	protected String codePath;
	//获得code页面problem标题的path
	protected String problemNamePath;
	
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		url = page.getUrl().toString();
		if (checkPattern(url, problemList)) {
			System.out.println("查询问题列表");
			getLinks(page);
		} else if (checkPattern(url, problemCode)) {
			System.out.println("查询问题代码");
			getCode(page);
		} else if (checkPattern(url, submissionList)){
			System.out.println("查看提交列表");
			getSubmissionList(page);
		} else {
			System.out.println("查看问题描述");
			getSubmission(page);
		}
	}
	
	public void getLinks(Page page) {
		page.putField("name", page.getHtml().xpath(getProblemLinkPath).all());
		forwards(page, problemLinkPath);
	}
	
	public void getSubmissionList(Page page) {
		List<String> list = page.getHtml().xpath(codePagePath).all();
		System.out.println("list is: " + list.toString());
		if (list.size() != 0)  {
			for (String value: list) {
				page.addTargetRequest(value);
			}
		}
	}
	
	public void getSubmission(Page page) {
		forwards(page, submissionLinkPath);
	}
	public void getCode(Page page) {
		page.putField("code", codePath);
		page.putField("problem_name", problemNamePath);
	}
	
	public void forwards(Page page, String xpath) {
		List<String> list = page.getHtml().xpath(xpath).all();
		System.out.println("list is: " + list.toString());
		if (list.size() != 0)  {
			for (String value: list) {
				page.addTargetRequest(value);
			}
		}
	} 

	public Site getSite() {
		return site;
	}
	
	public void setSite(String cookie) {
		site = Site.me().addCookie("csrftoken", cookie).setRetryTimes(3).setSleepTime(1000);
	}
	public boolean checkPattern(String url, String patternString) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(url);
		boolean res = matcher.matches();
		return res;
	}
}
