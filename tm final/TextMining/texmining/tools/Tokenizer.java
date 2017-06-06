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
				// System.out.println( "loading Next Chunk" );
				numCharsRead = br.read(chars = new char[SIZE], 0, SIZE);
				// System.out.println( "\n read bytes: " + numCharsRead );
				hasNextToken = true;
			} else {
				// System.out.println( " no more data avail to load " );
				hasNextToken = false;
			}
			int i = 0;
			while (i < numCharsRead) {
				// System.out.print(" in load chunk "+chars[i]+ "\n");
				i++;
			}
		} catch (IOException e) {
			// System.out.println(
			// "The file couldn't read at present, please try again" );
			e.printStackTrace();
		}
		return hasNextToken;
	}

	public String nextToken() {
		if (counter < SIZE - 1) {
			// System.out.println( "status 4: " + (counter < SIZE ));
			while (chars[counter] != SPACE && chars[counter] != NL
					&& chars.equals(null) == false && chars[counter] != '\0') {
				if (counter < SIZE - 1) {
					// System.out.println("Status 3: "+counter +
					// "   "+chars[counter]);
					if (Character.isLetter(chars[counter])) {
						sb.append(chars[counter]);
						counter++;
					} else
						counter++;
					if (counter == SIZE - 1) {
						if (loadNextChunk() && numCharsRead != 0) {
							// System.out.println( "Status 3-1: " + counter
							// + "   " + chars[counter] );
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
			// System.out.println("Returning token: " + retString);
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