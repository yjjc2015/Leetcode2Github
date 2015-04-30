package pipeline;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import container.Result;
/**
 * 
 * @author haodong
 *
 */
public class ResultPipeLineImpl extends Result implements ResultPipeLine {
	
	public void resultWriter(String filename) {
		OutputStream os = null;
		Writer writer = null;
		BufferedWriter buffer = null;
		try {
			os = new FileOutputStream(new File(filename));
			writer = new OutputStreamWriter(new BufferedOutputStream(os));
			buffer = new BufferedWriter(writer);
			List<String> list = super.getResult();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				buffer.write(list.get(i));
			}
			buffer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String listToString() {
		return super.toString();
	}
}
