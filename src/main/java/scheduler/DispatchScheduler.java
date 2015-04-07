package scheduler;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import container.Container;
import downloader.HttpClientDownloader;

public class DispatchScheduler implements Scheduler {
	private HttpClientDownloader downloader = HttpClientDownloader.getInstance();;
	private static BlockingQueue<ParserTask> queue = Container.getQueue();
	private static final String problemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/@href";				//进入问题描述 Xpath路径
	private static final String getProblemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/text()";				//得到题目名称Xpath路径
	private static final String submissionLinkPath = "//div[@class='row']/div/div/a/@href";																		//进入题目提交页面Xpath路径
	private static final String codePagePath = "//table[@id='result_testcases']/tbody/tr/td/a/@href";															//进入题目代码页面Xpath路径
	private static final String codePageStatusPath = "//table[@id='result_testcases]/t"
																							+ "body/tr/td/a[@class='status-accepted text-success']/strong/text()";                     //题目提交状态Xpath路径
	private Logger myLog;
	/**
	 * 
	 * startProcess(开始任务)
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public DispatchScheduler() {
		PropertyConfigurator.configure("test.log");
		myLog = Logger.getLogger(DispatchScheduler.class);
	}
	public void startProcess() {
		if (queue.isEmpty()) {
			try {
				myLog.debug("进入startProcess，试图初始化task queue");
				List<String> list = downloader.problemListDownloader(problemLinkPath);
				setTask(list);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ParserTask task = queue.take();
				getDownloadResult(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 爬取过程
	 */
	public void taskProcceed() {
		myLog.debug("进入task任务处理");
		while (!queue.isEmpty()) {
			try {
				myLog.debug("处理了一个任务");
				ParserTask task = queue.take();
				myLog.debug("取得的task的type类型为： " + task.getType());
				getDownloadResult(task);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * getDownloadResult(根据已有的task获得新的task)
	 * @param task
	 * @return
	 * @throws InterruptedException
	 *ParserTask
	 * @exception
	 * @since  1.0.0
	 */
	public void getDownloadResult(ParserTask task) throws InterruptedException {
		if (task.getType() == ParserTask.TaskType.GET_SUBMISION_URL) {
			myLog.debug("正在获取问题提交列表url");
			List<String> list = downloader.problemDescriptionDownloader(task.getUrl(), submissionLinkPath);
			setTask(list);
		} else if (task.getType() == ParserTask.TaskType.GET_CODE_URL) {
			myLog.debug("正在获取代码页面url");
			List<String> list = downloader.submissionListDownloader(task.getUrl(), codePagePath);
			setTask(list);
		} else if (task.getType() == ParserTask.TaskType.GET_CODE) {
			myLog.debug("正在获取代码");
			//do something to get code
		} else {
			System.out.println("无法识别该url，请填写正确的url。");
		}
	}
	
	/**
	 * 
	 * setTask(辅助方法，根据从队列中取得的task，分析，爬取获得新的task)
	 * @param list
	 * @throws InterruptedException
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public void setTask(List<String> list) throws InterruptedException {
		for (String value: list) {
			ParserTask task = new ParserTask(new StringBuilder("https://leetcode.com").append(value).toString());
			myLog.debug("task的url为： " + task.getUrl());
			task.isType();
			queue.put(task);
		}
	}
}
