import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Decode extends Code {

	Decode(String sourceFilePath, String resultFilePath, int frameSize) {	
		super(sourceFilePath, resultFilePath, frameSize);
	}

	protected void loadFile() {

		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(sourceFilePath)), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		frames = content.split("01111110");
	}

	public void doDecoding() throws Exception {

		PrintWriter out;

		try {
			out = new PrintWriter(resultFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		for(String frame : frames) {

			if(frame.length() > 0) {

				frame = frame.replace("01111110","");
				frame = frame.replace("111110","11111");

				String realFrame = frame.substring(0, frameSize);
				String crcCode = frame.substring(frameSize);

				crc.update(realFrame.getBytes());

				if(! crcCode.equals(Integer.toBinaryString((int)crc.getValue())))
					throw new Exception("Invalid crc for frame: " + realFrame + " expected crc : " + crcCode
							+"and was : "+ Integer.toBinaryString((int)crc.getValue()));

				crc.reset();
				out.write(realFrame);
			}
		}
		out.close();
	}


}
