import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import scheduler.DispatchScheduler;
import downloader.HttpClientDownloader;

public class MultiSpider {

	private int threadNum = 10;
	private ExecutorService service;
	private String username;
	private String password;
	private static DispatchScheduler scheduler;
	private static Logger myLog;

	public MultiSpider create(String username, String password) {
		PropertyConfigurator.configure("test.log");
		myLog = Logger.getLogger(MultiSpider.class);
		myLog.debug("开始创建MultiSpider");
		this.username = username;
		this.password = password;
		HttpClientDownloader.init(username, password);
		scheduler = new DispatchScheduler();
		scheduler.startProcess();
		myLog.debug("MultiSpider创建完毕");
		return new MultiSpider();
	}

	public void run() {
		myLog.debug("正式开始爬虫");
		service = Executors.newFixedThreadPool(this.threadNum);
		for (int i = 0; i < this.threadNum; i++) {
			Future future = service.submit(new Callable() {

				public Object call() throws Exception {
					myLog.debug("进入Executors模块，准备进入taskProcceed方法");
					scheduler.taskProcceed();
					return null;
				}
	
			});
		}
	}

	public MultiSpider thread(int num) {
		this.threadNum = num;
		return this;
	}

	public static void main(String[] args) {
		MultiSpider spider = new MultiSpider().thread(10);
		spider.create("tanghaodong25@163.com", "***").run();
	}
}
