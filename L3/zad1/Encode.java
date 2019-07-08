import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.CRC32;

public class Encode extends Code {

	Encode(String sourceFilePath, String resultFilePath, int frameSize) {	
		super(sourceFilePath, resultFilePath, frameSize);
	}
	
	public void doEncoding() {
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(resultFilePath, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		} 
		
		String result = "";
		StringBuilder sb = new StringBuilder();
		
		for(String frame : frames) {
			
			if(frame.length() == frameSize) {
				sb.append(frame);
			
				crc.update(frame.getBytes());
				sb.append(Integer.toBinaryString((int)crc.getValue())); 
				
				result = sb.toString();
				result = result.replace("11111","111110");
				result = endsFlag + result;
						
				writer.print(result);
				crc.reset();
				sb.setLength(0);
			}
		}
		writer.print(endsFlag);
		writer.close();
	}
}
