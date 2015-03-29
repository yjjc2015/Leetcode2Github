package scheduler;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import container.Container;
import downloader.HttpClientDownloader;

public class DispatchScheduler implements Scheduler {
	private volatile boolean stoped;
	private ParserTask task;
	private HttpClientDownloader downloader = HttpClientDownloader.getInstance();;
	
	private static BlockingQueue<ParserTask> queue = Container.getQueue();
	public void startProcess() {
		if (queue.isEmpty()) {
			try {
				List<String> list = downloader.problemListDownloader();
				for (String value: list) {
					task = new ParserTask(value);
					task.isType();
					queue.put(task);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				task = queue.take();
				String url = task.getUrl();
				task = new ParserTask(url);
				task.isType();
				queue.put(task);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void taskProcceed() {
		while (!queue.isEmpty()) {
			try {
				task = queue.take();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String url = task.getUrl();
			task = new ParserTask(url);
			task.isType();
			try {
				queue.put(task);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
