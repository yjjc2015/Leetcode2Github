package haodong.net.cn.leetcode2github;

import us.codecraft.webmagic.Page;

public class CodeProcesser extends App {
	
	public CodeProcesser(String url) {
		submissionList = "/submissions/";
		problemCode = "/submissions/detail/";
		problemList = "/problemset/algorithms/";
		problemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/@href";
		submissionLinkPath = "//div[@class='question-title']/a/@href";
		this.url = url;
	}
	@Override
	public void process(Page page) {
		super.process(page);
	}
}
