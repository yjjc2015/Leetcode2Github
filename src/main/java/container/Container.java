package container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import scheduler.ParserTask;

public class Container {
	private static BlockingQueue<ParserTask> priorityQueue = new PriorityBlockingQueue<ParserTask>(200);
	public static boolean isEmpty() {
		return priorityQueue.isEmpty();
	}
	public static BlockingQueue<ParserTask> getQueue() {
		return priorityQueue;
	}
}
