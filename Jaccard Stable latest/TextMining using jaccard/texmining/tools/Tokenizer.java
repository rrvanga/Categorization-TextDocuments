package texmining.tools;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class Tokenizer {
	private static final int SIZE = 16 * 1024; // 8KBytes
	private static final int SPACE = 32;
	private static final int NL = 10;
	private FileReader reader;
	private BufferedReader br;
	private int numCharsRead;
	private int counter = 0;
	public boolean hasNextToken = true;
	private char chars[] = new char[SIZE];
	private StringBuffer sb = new StringBuffer();
	private String retString = new String();
	private String filePath;
	Tokenizer(String filePath) {
		try {
			this.filePath = filePath;
			System.out.println(" Reading from " + this.filePath);
			reader = new FileReader(this.filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(reader);
		try {
			numCharsRead = br.read(chars, 0, SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean loadNextChunk() {
		try {
			if ((numCharsRead = br.read(chars = new char[SIZE], 0, SIZE)) != -1) {
				counter = 0;
				numCharsRead = br.read(chars = new char[SIZE], 0, SIZE);
				hasNextToken = true;
			} else {
				hasNextToken = false;
			}
			int i = 0;
			while (i < numCharsRead) {
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hasNextToken;
	}
	public String nextToken() {
		if (counter < SIZE - 1) {
			while (chars[counter] != SPACE && chars[counter] != NL
					&& chars.equals(null) == false && chars[counter] != '\0') {
				if (counter < SIZE - 1) {
					if (Character.isLetter(chars[counter])) {
						sb.append(chars[counter]);
						counter++;
					} else
						counter++;
					if (counter == SIZE - 1) {
						if (loadNextChunk() && numCharsRead != 0) {
							sb.append(chars[counter]);
							counter++;
						} else {
							counter = 0;
							break;
						}
					}
				}
			}
			while ((chars[counter] == NL || chars[counter] == SPACE)) {
				if (counter == SIZE - 1)
					loadNextChunk();
				else
					counter++;
			}
			retString = new String(sb);
			sb.setLength(0);
			if ((chars[counter] == '\0' || chars[counter] == -1 || counter > numCharsRead)) {
				hasNextToken = false;
			}
			return retString;
		} else {
			if (counter == SIZE - 1) {
				loadNextChunk();
			} else {
				hasNextToken = false;
			}
			return null;
		}
	}
}