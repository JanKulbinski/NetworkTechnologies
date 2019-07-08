import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.CRC32;

public abstract class Code {

	protected String[] frames;
	protected String resultFilePath;
	protected String sourceFilePath;
	protected String endsFlag = "01111110";
	protected int frameSize;
	protected CRC32 crc;


	Code(String sourceFilePath, String resultFilePath, int frameSize) {	

		this.sourceFilePath = sourceFilePath;
		this.resultFilePath = resultFilePath;
		this.frameSize = frameSize;
		crc = new CRC32();
		loadFile();
	}

	protected void loadFile() {

		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(sourceFilePath)), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		content = content.replaceAll("\\s","");
		frames = splitEqually(content,frameSize);
	}

	protected String[] splitEqually(String text, int size) {

		int length = (text.length() + size - 1) / size;
		String[] result = new String[length];

		for (int start = 0; start < length; start++) {
			result[start] = text.substring(start*size, Math.min(text.length(), (start*size)+size));
		}
		return result;
	}

}
