

import org.apache.http.impl.client.CloseableHttpClient;

public class MultiSpider implements Runnable {
	private  CloseableHttpClient httpClient;
	public MultiSpider(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	public void run() {
			
	}

}
