package downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import selector.XpathSelector;



public class DownloaderTest {
	private List<String> ProblemDescriptionPath = new ArrayList<String>();
	private List<String> ProblemSubmissionPath = new ArrayList<String>();
	private Map<String, List<String>> problemCodePath = new HashMap<String, List<String>>();
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://leetcode.com");
		CloseableHttpResponse response1 = httpClient.execute(httpGet);
		printResponse(response1);
		String cookieString = getCookie(response1);
		HttpEntity entity1 = response1.getEntity();
		EntityUtils.consume(entity1);

		HttpPost httpPost = new HttpPost("https://leetcode.com/accounts/login/");
		httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6)Gecko/20091201 Firefox/3.5.6");
		httpPost.addHeader("Referer", "https://leetcode.com/accounts/login/");
		httpPost.addHeader("Origin", "https://leetcode.com");
		Map<String, String> map = new HashMap<String, String>();
		map.put("login", "tanghaodong25@163.com");
		map.put("password", "thd04180015");
		map.put("csrfmiddlewaretoken", cookieString);
		map.put("remember", "on");
		httpPost.setEntity(new UrlEncodedFormEntity(getParam(map), "UTF-8"));
		response1 = httpClient.execute(httpPost);
		HeaderIterator iterator = response1.headerIterator();
		while (iterator.hasNext()) {
			iterator.next();
		}
		printResponse(response1);
		entity1 = response1.getEntity();
		EntityUtils.consume(entity1);
		
		HttpGet httpGet1 = new HttpGet("https://leetcode.com/problems/rotate-array/submissions/");
		response1 = httpClient.execute(httpGet1);
		printResponse(response1);
		XpathSelector xpathSelector = new XpathSelector("//div[@class='row']/div/div/a/@href");
		List<String> listTmp = xpathSelector.selectList(getHtml(response1));
		System.out.println(listTmp.size());
		entity1 = response1.getEntity();
		EntityUtils.consume(entity1);
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
		for (Header value: headers) {
			if (value.getName().equals("Set-Cookie")) {
				return value.getValue().split(";")[0].split("=")[1];
			}
		}
		return null;
	}
	
	public static String getHtml(HttpResponse response) {
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
}
