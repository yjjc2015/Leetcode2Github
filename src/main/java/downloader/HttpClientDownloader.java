package downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import container.URLContainer;
import us.codecraft.webmagic.selector.XpathSelector;

public class HttpClientDownloader extends URLContainer  implements Downloader {
	private static final String INDEX_URL = "https://leetcode.com";                                    														//首页的url地址
	private static final String LOGIN_URL = "https://leetcode.com/accounts/login/";																//登录页面url地址			
	private static final String PASSWORD = "thd04180015";																										//登录密码
	private static final String USER_NAME = "tanghaodong25@163.com";																				//登录邮箱
	private static final String submissionListURL = ".*/submissions/$";																					//检验是否是题目提交列表页面
	private static final String problemCodeURL = ".*/submissions/detail/.*";																		//检验是否是题目代码页面
	private static final String problemListURL = ".*/problemset/algorithms/$";																		//检验是否是题目列表页面
	
	private static final String problemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/@href";				//进入问题描述 Xpath路径
	private static final String getProblemLinkPath = "//table[@class='table table-striped table-centered']/tbody/tr/td/a/text()";				//得到题目名称Xpath路径
	private static final String submissionLinkPath = "//div[@class='row']/div/div/a/@href";																		//进入题目提交页面Xpath路径
	private static final String codePagePath = "//table[@id='result_testcases']/tbody/tr/td/a/@href";															//进入题目代码页面Xpath路径
	private static final String codePageStatusPath = "//table[@id='result_testcases]/t"
																							+ "body/tr/td/a[@class='status-accepted text-success']/strong/text()";                     //题目提交状态Xpath路径
	
	private static final String problemNamePath = "//div[@class='col-md-12']/h4/a/text()";                                                  //题目名称Xpath路径
	private static final String codePath = "//div[@class='ace_content']/text()";																			//得到代码Xpath路径
	private static CloseableHttpClient httpClient;
	/**
	 * 
	 * 创建一个新的实例 HttpClientDownloader.
	 *
	 */
	public HttpClientDownloader() {
		init();
	}

	public void init() {
		httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(INDEX_URL);
		CloseableHttpResponse response1;
		String cookieString = null;
		HttpEntity entity1;
		try {
			response1 = httpClient.execute(httpGet);
			printResponse(response1);
			cookieString = getCookie(response1);
			entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpPost httpPost = new HttpPost(LOGIN_URL);
		httpPost.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6)Gecko/20091201 Firefox/3.5.6");
		httpPost.addHeader("Referer", LOGIN_URL);
		httpPost.addHeader("Origin", INDEX_URL);
		Map<String, String> map = new HashMap<String, String>();
		map.put("login", USER_NAME);
		map.put("password", PASSWORD);
		map.put("csrfmiddlewaretoken", cookieString);
		map.put("remember", "on");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(getParam(map), "UTF-8"));
			response1 = httpClient.execute(httpPost);
			printResponse(response1);
			entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void setTread(Thread thread) {
		// TODO Auto-generated method stub

	}

	public void download(String url) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://leetcode.com");
		CloseableHttpResponse response1;
		String cookieString = null;
		HttpEntity entity1;
		try {
			response1 = httpClient.execute(httpGet);
			printResponse(response1);
			cookieString = getCookie(response1);
			entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (ClientProtocolException e1) {
			System.out.println(e1.getMessage());
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}

		HttpPost httpPost = new HttpPost("https://leetcode.com/accounts/login/");
		httpPost.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6)Gecko/20091201 Firefox/3.5.6");
		httpPost.addHeader("Referer", "https://leetcode.com/accounts/login/");
		httpPost.addHeader("Origin", "https://leetcode.com");
		Map<String, String> map = new HashMap<String, String>();
		map.put("login", "tanghaodong25@163.com");
		map.put("password", "thd04180015");
		map.put("csrfmiddlewaretoken", cookieString);
		map.put("remember", "on");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(getParam(map), "UTF-8"));
			response1 = httpClient.execute(httpPost);
			printResponse(response1);
			entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static List<NameValuePair> getParam(Map parameterMap) {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		Iterator it = parameterMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry parmEntry = (Entry) it.next();
			param.add(new BasicNameValuePair((String) parmEntry.getKey(),
					(String) parmEntry.getValue()));
		}
		return param;
	}

	public static void printResponse(HttpResponse httpResponse)
			throws ParseException, IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			System.out.println("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			System.out.println("response length:" + responseString.length());
			System.out.println("response content:"
					+ responseString.replace("\r\n", ""));
		}
	}

	public static String getCookie(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getAllHeaders();
		for (Header value : headers) {
			if (value.getName().equals("Set-Cookie")) {
				return value.getValue().split(";")[0].split("=")[1];
			}
		}
		return null;
	}
	
	public void problemListDownloader(String url) {
		doDispatcher(url, problemQueue);
	}

	public void problemDescriptionDownloader(String url) {
		doDispatcher(url, problemSubmission);
	}

	public void submissionListDownloader(String url, String name) {
		doDispatcher(url, problemCodePage, name);
	}

	public void codePageDownloader(String url) {
		//爬取代码
	}

	public String getHtml(HttpResponse response) {
		BufferedReader br = null;
		try {
			InputStream responseBody = response.getEntity().getContent();
			StringBuilder stringBuilder = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(
					responseBody));
			String line = null;
			while ((line = br.readLine()) != null) {
				stringBuilder.append("\n" + line);
			}
			return stringBuilder.toString();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	/**
	 * 
	 * doDispatcher()
	 * (适用于获取问题详情url和获取问题提交列表url)
	 * @param url
	 * @param blockingQueue
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public void doDispatcher(String url, BlockingQueue<String> blockingQueue) {
		HttpGet httpGet1 = new HttpGet(url);
		try {
			HttpResponse response1 = httpClient.execute(httpGet1);
			printResponse(response1);
			
			XpathSelector xpathSelector = new XpathSelector(problemLinkPath);
			List<String> listTmp = xpathSelector.selectList(getHtml(response1));
			for (String string: listTmp) {
				blockingQueue.put(string);
			}
			HttpEntity entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * doDispatcher()
	 * (用于获取accept代码url)
	 * @param url
	 * @param blockingQueue
	 * @param name
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public void doDispatcher(String url, BlockingQueue<String> blockingQueue, String name) {
		HttpGet httpGet1 = new HttpGet(url);
		try {
			HttpResponse response1 = httpClient.execute(httpGet1);
			printResponse(response1);
			
			XpathSelector xpathSelector = new XpathSelector(problemLinkPath);
			List<String> listTmp = xpathSelector.selectList(getHtml(response1));
			for (String string: listTmp) {
				blockingQueue.put(string);
			}
			HttpEntity entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
