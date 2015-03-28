package haodong.net.cn.leetcode2github;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Hello world!
 *
 */
public class App implements PageProcessor {
	protected Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(15000);
	//submission 列表url
	protected String submissionList;
	//problem 代码url
	protected String problemCode;
	//problem 描述url
	protected String problemDescription;
	//problem 列表url
	protected String problemList;
	//初始访问页面
	protected String url;
	//前往problem列表的path
	protected String problemLinkPath;
	//获得problem列表的path
	protected String getProblemLinkPath;
	//前往submission列表的path
	protected String submissionLinkPath;
	//前往code页面的path
	protected String codePagePath;
	//获得code的path
	protected String codePath;
	
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		if (url.contains(problemList)) {
			getLinks(page);
		} else if (url.contains(problemCode)) {
			getCode(page);
		} else {
			getSubmission(page);
		}
	}
	
	public void getLinks(Page page) {
		page.putField("name", page.getHtml().xpath(getProblemLinkPath).all());
		forwards(page, problemLinkPath);
	}
	
	public void getSubmission(Page page) {
		forwards(page, submissionLinkPath);
	}
	
	public void getCode(Page page) {
		
	}
	
	public void forwards(Page page, String xpath) {
		List<String> list = page.getHtml().links().xpath(xpath).all();
		page.addTargetRequests(list);
	} 

	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {

		Spider.create(new App())
		// 从"https://github.com/code4craft"开始抓
				.addUrl("https://leetcode.com/problemset/algorithms/")
				// 开启5个线程抓取
				.thread(5)
				// 启动爬虫
				.run();
	}
}
