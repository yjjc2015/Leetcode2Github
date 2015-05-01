import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pipeline.ResultPipeLine;
import pipeline.ResultPipeLineImpl;
import container.Result;
import scheduler.DispatchScheduler;
import downloader.HttpClientDownloader;

public class MultiSpider {

	private static Spider spider;
	private ExecutorService service;
	private String username;
	private String password;
	private static DispatchScheduler scheduler;
	private static Logger myLog;	

	static class Spider {
		private String outputFilename;
		private int threadNum;
		public String getOutputFilename() {
			return outputFilename;
		}
		public void setOutputFilename(String outputFilename) {
			this.outputFilename = outputFilename;
		}
		public int getThreadNum() {
			return threadNum;
		}
		public void setThreadNum(int threadNum) {
			this.threadNum = threadNum;
		}
		
	}
	
	public MultiSpider build() {
		spider = new Spider();
		return this;
	}
	
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
		return this;
	}
	
	static class CallableProccess implements Callable {
		int threadNum = spider.getThreadNum();
		private final CountDownLatch threadCount;
		public CallableProccess(CountDownLatch threadCount) {
			this.threadCount = threadCount;
		}
		public Object call() throws Exception {
			myLog.debug("进入Executors模块，准备进入taskProcceed方法");
			scheduler.taskProcceed();
			myLog.debug("剩余任务数量： "+threadCount.getCount());
			threadCount.countDown();
			return null;
		}
		
	}
	public void run() throws InterruptedException {
		myLog.debug("正式开始爬虫");
		int threadNum = spider.getThreadNum();
		String filename = spider.getOutputFilename();
		service = Executors.newFixedThreadPool(threadNum);
		final CountDownLatch threadCount = new CountDownLatch(threadNum*3);
		
		CallableProccess proccess = new CallableProccess(threadCount);
		for (int i = 0; i < threadNum*3; i++) {
			Future future = service.submit(proccess);
		}
		threadCount.await();
		myLog.debug("正式收集结果！！存入文件："+filename);
		ResultPipeLine pipeline = new ResultPipeLineImpl();
		pipeline.resultWriter(filename);
		service.shutdown();
	}

	public MultiSpider thread(int num) {
		this.spider.setThreadNum(num);
		return this;
	}

	public MultiSpider setFile(String filename) {
		this.spider.setOutputFilename(filename);
		return this;
	}

	public static void main(String[] args) throws InterruptedException {
		MultiSpider spider = new MultiSpider().build().thread(10).setFile("output.txt");
		spider.create("tanghaodong25@163.com", "***").run();
	}
}
