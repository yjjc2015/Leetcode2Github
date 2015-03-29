
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import scheduler.DispatchScheduler;
import downloader.HttpClientDownloader;

public class MultiSpider {

	private int threadNum = 1;
	private ExecutorService service;
	private String username;
	private String password;
	private DispatchScheduler scheduler;
	
	public MultiSpider create(String username, String password) {
		this.username = username;
		this.password = password;
		HttpClientDownloader.init(username, password);
		scheduler = new DispatchScheduler();
		scheduler.startProcess();
		return new MultiSpider();
	}

	public void run() {
		service = Executors.newFixedThreadPool(this.threadNum);
		Future future = service.submit(new Callable() {

			public Object call() throws Exception {
				scheduler.taskProcceed();
				return null;
			}
			
		});
	}

	public MultiSpider thread(int num) {
		this.threadNum = num;
		return this;
	}
}
