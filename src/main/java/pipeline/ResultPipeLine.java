package pipeline;

/**
 * 输出接口
 * @author haodong
 *
 */
public interface ResultPipeLine {
	public String listToString();
	public void resultWriter(String filename);
}
