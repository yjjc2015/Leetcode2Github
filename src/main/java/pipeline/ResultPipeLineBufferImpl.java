package pipeline;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import container.Result;

public class ResultPipeLineBufferImpl extends Result implements ResultPipeLine {

	public String listToString() {
		return null;
	}

	public void resultWriter(String filename) {
		try (FileChannel fileChannel = FileChannel.open(Paths.get(filename), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			List<String> tmp = super.getResult();
			buffer.clear();
			int len = tmp.size();
			for (int i = 0; i < len; i++) {
				buffer.put(tmp.get(i).getBytes());
			}
			while (buffer.hasRemaining()) {
				fileChannel.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
