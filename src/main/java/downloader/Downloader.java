package downloader;

import java.util.List;

public interface Downloader {
	/**
	 * set thread for downloader
	 * @param thread
	 */
	public void setTread(Thread thread);
	
	/**
	 * 爬取题目列表
	 * @param url
	 * @return 
	 */
	public List<String> problemListDownloader(String pattern);
	
	/**
	 * 进入题目描述页面，爬取题目提交url列表
	 * @param url
	 */
	public List<String> problemDescriptionDownloader(String url, String pattern);
	
	/**
	 *	进入题目提交列表页面，爬取code所在页面url 
	 * @param url
	 */
	public List<String> submissionListDownloader(String url, String pattern);
	
	/**
	 * 进入code所在页面，爬取code
	 * @param url
	 */
	public List<String> codePageDownloader(String url, String pattern);
}
