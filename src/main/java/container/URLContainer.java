package container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * 容器类
 *
 * URLContainer
 * 
 * kin
 * kin
 * 2015年3月29日 上午6:50:58
 * 
 * @version 1.0.0
 *
 */
public class URLContainer {
	protected BlockingQueue<String> problemQueue = new ArrayBlockingQueue<String>(200);
	protected BlockingQueue<String> problemSubmission = new ArrayBlockingQueue<String>(50);
	protected BlockingQueue<String> problemCodePage = new ArrayBlockingQueue<String>(100);
	protected Map<String, String> problemCode = new HashMap<String, String>();
}
