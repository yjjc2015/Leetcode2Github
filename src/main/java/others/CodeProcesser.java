
package others;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

public class CodeProcesser extends App {
	
	public CodeProcesser(String url, String cookie) {
		submissionList = ".*/submissions/$";
		problemCode = ".*/submissions/detail/.*";
		problemList = ".*/problemset/algorithms/$";
		
		problemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/@href";
		getProblemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/text()";
		submissionLinkPath = "//div[@class='row']/div/div/a/@href";
		codePagePath = "//table[@id='result_testcases']/tbody/tr/td/a/@href";
		codePageStatusPath = "//table[@class=''table table-hover table-bordered table-striped ng-scope']/tbody/tr/td/a[@class='status-accepted text-success']/strong/text()";
		
		problemNamePath = "//div[@class='col-md-12']/h4/a/text()";
		codePath = "//div[@class='ace_content']/text()";
		this.url = url;
		setSite(cookie);
	}
	@Override
	public void process(Page page) {
		super.process(page);
	}
	@Override
	public Site getSite() {
		return super.getSite();
	}
	
	
	public static void main(String[] args) {

		Spider.create(new App())
		// 从"https://github.com/code4craft"开始抓
				.addUrl("https://leetcode.com/problemset/algorithms/")
				// 开启5个线程抓取
				.thread(1)
				// 启动爬虫
				.run();
	}
	
	public static void portal(String url, String cookie) {
		
		Spider.create(new CodeProcesser(url, cookie))
		// 从"https://github.com/code4craft"开始抓
				.addUrl(url)
				// 开启5个线程抓取
				.thread(40)
				// 启动爬虫
				.run();
	}
}
