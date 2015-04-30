package container;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private static List<String> list = new ArrayList<String>();
	public static List<String> getResult() {
		return list;
	}
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			string.append(" ").append(list.get(i));
		}
		return string.toString();
	}
}
