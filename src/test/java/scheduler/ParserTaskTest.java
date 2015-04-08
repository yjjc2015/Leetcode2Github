package scheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.TestCase;

public class ParserTaskTest extends TestCase {
	
	@Test
	public void testIsType() {
		ParserTask parserTask = new ParserTask("https://leetcode.com/problems/house-robber/");
		parserTask.isType();
		System.out.println(parserTask.getType());
	}
	
	@Test
	public void testCheckedPattern() {
		String url = "https://leetcode.com/problems/house-robber/";
		String patternString = ".*/problems/.*";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(url);
		boolean res = matcher.matches();
		System.out.println(res);
	}
	
	@Test
	public void testEnum() {
		System.out.println(ParserTask.TaskType.PROBLEM_URL.ordinal());
	}
}
