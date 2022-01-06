/**
 * 
 */
package org.yooreeka.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is a utility class for comparing two text files.
 * It should be able to manage arbitrarily large files without running out of memory.
 * It should also be fast and produce a sensible output.
 * 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class Diff {

	int globalCounter = 0;
	int numOfDiffs = 0;
	
	String fileName1, fileName2, outFileName;
	BufferedReader fileReader1, fileReader2;
	BufferedWriter diffWriter;
	
	public Diff(String fileName1, String fileName2, String outFileName) {
		
		this.fileName1 = fileName1;
		this.fileName2 = fileName2;
		this.outFileName = outFileName;
		
		//Initialize readers
		try {
			fileReader1 = new BufferedReader(new FileReader(fileName1));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			fileReader2 = new BufferedReader(new FileReader(fileName2));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Initialize writer for the output
		try {
			diffWriter = new BufferedWriter(new FileWriter(outFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method generates the comparison between {@code fileName1} and {@code fileName2}.
	 * {@code lines} defines the number of lines that must be read for the comparison.
	 *  
	 * @param lines
	 */
	public void execute(int lines) {

		ArrayList<String> f1;
		ArrayList<String> f2;

		do {
			f1 = readLines(fileReader1, lines);
			f2 = readLines(fileReader2, lines);

			if (f1.size() < f2.size()) {
				process(f1, "F1", f2, "F2");
			} else {
				process(f2, "F2", f1, "F1");
			}
		} while (f1.size() > 0 || f2.size() >0);		
	}
	
	private void process(ArrayList<String> a1, String a1Name, ArrayList<String> a2, String a2Name) {
		
		for (int i=0; i < a2.size(); i++) {
			
			if (i < a1.size() && a1.get(i) !=null) {
				if (a1.get(i).compareTo(a2.get(i)) == 0) {
					// The two strings are equal
				} else {
					// The two strings differ, write to the file
					numOfDiffs++;
					try {
						diffWriter.append(i+",\n    "+a1Name+": "+a1.get(i)+"\n    "+a2Name+": "+a2.get(i));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
			} else {
				// The two strings differ, write to the file
				numOfDiffs++;
				try {
					diffWriter.append(i+",\n   "+a1Name+": [Empty Line]\n   "+a2Name+":"+a2.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	

	private ArrayList<String> readLines(BufferedReader reader, int lines) {
		
		int counter = 0;
		
		ArrayList<String> strings = new ArrayList<String>();
        
        String s;
        try {
			while ((s = reader.readLine()) != null && counter < lines) {
				strings.add(s);
			    counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        return strings;
	}
	
	private void cleanUp() throws IOException {
		fileReader1.close();
		fileReader2.close();
		diffWriter.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String f1 = "D:\\Data\\PRIMES\\primes.100k.txt";
		String f2 = "D:\\Data\\PRIMES\\seq.M21.100k.txt";
		String out = "D:\\Data\\PRIMES\\diff_TEST_100k.out";
		
		Diff diff = new Diff(f1,f2,out);
		
		int lines=8000;
		
		diff.execute(lines);
		
		try {
			diff.cleanUp();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
