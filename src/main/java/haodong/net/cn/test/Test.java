package haodong.net.cn.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 同步leetcode数据到github上去
 * @author haodong
 *
 */
public class Test {
	private static final String PASSWORD = "thd04180015";
	private static final String USERNAME = "tanghaodong25@163.com";
	private static final String LOGIN_STRING = "https://www.leetcode.com/accounts/login/";
	private static final String INDEX_STRING = "https://www.leetcode.com";
	private static final String ALGORITHMS = "https://www.leetcode.com/problemset/algorithms/";
	private static String cookie = null;

	public static void main(String[] args) throws IOException {
		Test test = new Test();
		test.getToken("tanghaodong25@163.com", "thd04180015", LOGIN_STRING);
		test.getAlgorithmsPage(ALGORITHMS);
	}
	/**
	 * 获得认证，得到登录token
	 * @param name
	 * @param password
	 * @param urlString
	 */
	public void getToken(String name, String password, String urlString) {
		HttpURLConnection connection = null;
		OutputStream out = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			// 设置通用请求
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 可以根据需要 提交GET、POST、DELETE、INPUT等http提供的功能
			connection.setRequestMethod("POST"); 
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);

			// 设置http头消息
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6)Gecko/20091201 Firefox/3.5.6");
			connection.setRequestProperty("Referer", LOGIN_STRING);
			connection.setRequestProperty("Origin", INDEX_STRING);
			connection.setRequestProperty("Content-Type", "application/json"); 
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Connection", "keep-alive");
			// 特定http服务器需要的信息，根据服务器所需要求添加
			// connection.setRequestProperty("X-Auth-Token","xx");
			connection.connect();
			cookie = this.getCookie(INDEX_STRING);
			
			JSONObject user = new JSONObject();
			user.put("login", USERNAME);
			user.put("password", PASSWORD);
			user.put("csrfmiddlewaretoken", cookie);
			user.put("remember", "on");

			out = connection.getOutputStream();
			out.write(user.toString().getBytes()); 
			out.flush();
		} catch (MalformedURLException e) {
			System.out.print(e.getMessage());
		} catch (IOException e) {
			System.out.print(e.getMessage());
		} catch (JSONException e) {
			System.out.print(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					System.out.print(e.getMessage());
				}
			}
		}
	}
	/**
	 * 获得主页面返回的cookie，并把返回的cookie保存
	 * @param urlString
	 * @return
	 */
	public String getCookie(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//获取cookie  
	        Map<String,List<String>> map=conn.getHeaderFields();
	        List<String> list = map.get("Set-Cookie");
	        String string = list.get(0).split(";")[0];
	        String res = string.split("=")[1];
	        return res;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 返回算法页面
	 * @param urlString
	 */
	public void getAlgorithmsPage(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Cookie", cookie);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
