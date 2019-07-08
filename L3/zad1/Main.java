import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

	private static final String encodeFileSource = "C:\\Users\\janku\\OneDrive\\Pulpit\\EclipseWorkspace2\\SieciL3z1\\src\\encodingSource.txt";
	private static final String encodeFileResult = "C:\\\\Users\\\\janku\\\\OneDrive\\\\Pulpit\\\\EclipseWorkspace2\\\\SieciL3z1\\\\src\\\\encodingResult.txt";
	private static final String decodeFileResult = "C:\\\\Users\\\\janku\\\\OneDrive\\\\Pulpit\\\\EclipseWorkspace2\\\\SieciL3z1\\\\src\\\\decodingResult.txt";
	private static final int frameSize = 8;

	public static void main(String[] args) throws UnsupportedEncodingException {

		//Encode encode = new Encode(encodeFileSource,encodeFileResult,frameSize);
		//encode.doEncoding();

		Decode decode = new Decode(encodeFileResult,decodeFileResult,frameSize);

		try {
			decode.doDecoding();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			byte[] f1 = Files.readAllBytes(Paths.get(encodeFileSource));
			byte[] f2 = Files.readAllBytes(Paths.get(decodeFileResult));
			if(Arrays.equals(f1,f2)) {
				System.out.println("Encoding and then decoding have worked");
			} else {
				System.out.println("Encoding and then decoding haven't worked");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
