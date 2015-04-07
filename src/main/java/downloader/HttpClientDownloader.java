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
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import us.codecraft.webmagic.selector.XpathSelector;

public class HttpClientDownloader implements Downloader {
	private static final String ALGORITHMS = "https://www.leetcode.com/problemset/algorithms/";
	private static final String INDEX_URL = "https://leetcode.com";                                    														//首页的url地址
	private static final String LOGIN_URL = "https://leetcode.com/accounts/login/";																//登录页面url地址			
	private static final String PASSWORD = "thd04180015";																										//登录密码
	private static final String USER_NAME = "tanghaodong25@163.com";																				//登录邮箱
	private static final String submissionListURL = ".*/submissions/$";																					//检验是否是题目提交列表页面
	private static final String problemCodeURL = ".*/submissions/detail/.*";																		//检验是否是题目代码页面
	private static final String problemListURL = ".*/problemset/algorithms/$";																		//检验是否是题目列表页面
	
	private static final String problemNamePath = "//div[@class='col-md-12']/h4/a/text()";                                                  //题目名称Xpath路径
	private static final String codePath = "//div[@class='ace_content']/text()";																			//得到代码Xpath路径
	private static CloseableHttpClient httpClient;
	private static final HttpClientDownloader downloader = new HttpClientDownloader();
	private static Logger myLog;

	public static HttpClientDownloader getInstance() {
		return downloader;
	}
	
	/**
	 * 
	 * init(初始化httpclient，以单例模式保存httpclient)
	 * @param username
	 * @param password
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public static void init(String username, String password) {
		PropertyConfigurator.configure("test.log");
		myLog = Logger.getLogger(HttpClientDownloader.class);
		httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(INDEX_URL);
		CloseableHttpResponse response1;
		String cookieString = null;
		HttpEntity entity1;
		try {
			response1 = httpClient.execute(httpGet);
			//printResponse(response1);
			myLog.debug("第一次登录，试图获取cookie值，response状态为：" + response1.getStatusLine());
			cookieString = getCookie(response1);
			entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		HttpPost httpPost = new HttpPost(LOGIN_URL);
		httpPost.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6)Gecko/20091201 Firefox/3.5.6");
		httpPost.addHeader("Referer", LOGIN_URL);
		httpPost.addHeader("Origin", INDEX_URL);
		Map<String, String> map = new HashMap<String, String>();
		map.put("login", username);
		map.put("password", password);
		map.put("csrfmiddlewaretoken", cookieString);
		map.put("remember", "on");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(getParam(map), "UTF-8"));
			response1 = httpClient.execute(httpPost);
			//printResponse(response1);
			myLog.debug("试图登录leetcode网站， response状态为： " + response1.getStatusLine());
			entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		myLog.debug("HttpClientDownloader初始化完成");
	}

	public void setTread(Thread thread) {
		
	}
	
	/**
	 * 获取问题列表
	 */
	public List<String> problemListDownloader(String pattern) {
		myLog.debug("尝试获取问题列表");
		return doDispatcher(ALGORITHMS, pattern);
	}

	/**
	 * 根据问题问题描述信息所在的url，爬取问题提交列表对应的url
	 */
	public List<String> problemDescriptionDownloader(String url, String pattern) {
		myLog.debug("尝试获取问题提交记录列表");
		return doDispatcher(url, pattern);
	}
	
	public List<String> submissionListDownloader(String url, String pattern) {
		myLog.debug("尝试获取代码页面url");
		return doDispatcher(url, pattern);
	}

	public List<String> codePageDownloader(String url, String pattern) {
		myLog.debug("尝试获取代码");
		//爬取代码
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
	public List<String> doDispatcher(String url, String pattern) {
		HttpGet httpGet1 = new HttpGet(url);
		myLog.debug("进入doDispatcher函数，准备对"+url+"进行解析");
		try {
			HttpResponse response1 = httpClient.execute(httpGet1);
			myLog.debug("正在对"+url+"进行访问，response状态为：" + response1.getStatusLine());
			myLog.debug("试图对"+url+"进行解析，其中pattern为："+pattern);
			XpathSelector xpathSelector = new XpathSelector(pattern);
			List<String> listTmp = xpathSelector.selectList(getHtml(response1));
			HttpEntity entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
			return listTmp;
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * printResponse(打印response信息)
	 * @param httpResponse
	 * @throws ParseException
	 * @throws IOException
	 *void
	 * @exception
	 * @since  1.0.0
	 */
	public static void printResponse(HttpResponse httpResponse)
			throws ParseException, IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
//		HeaderIterator iterator = httpResponse.headerIterator();
//		while (iterator.hasNext()) {
//			System.out.println("\t" + iterator.next());
//		}
//		// 判断响应实体是否为空
//		if (entity != null) {
//			String responseString = EntityUtils.toString(entity);
//			System.out.println("response length:" + responseString.length());
//			System.out.println("response content:"
//					+ responseString.replace("\r\n", ""));
//		}
		PropertyConfigurator.configure("test.log");
		Logger m_log = Logger.getLogger(HttpClientDownloader.class);
		m_log.debug("hello world");
	}
	
	/**
	 * 
	 * getHtml(辅助方法，根据response信息返回String类型的html内容)
	 * @param response
	 * @return
	 *String
	 * @exception
	 * @since  1.0.0
	 */
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
	 * getCookie(获取leetcode网站的Set-Cookie值)
	 * @param httpResponse
	 * @return
	 *String
	 * @exception
	 * @since  1.0.0
	 */
	public static String getCookie(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getAllHeaders();
		for (Header value : headers) {
			if (value.getName().equals("Set-Cookie")) {
				return value.getValue().split(";")[0].split("=")[1];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * getParam(辅助方法)
	 * @param parameterMap
	 * @return
	 *List<NameValuePair>
	 * @exception
	 * @since  1.0.0
	 */
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
}
