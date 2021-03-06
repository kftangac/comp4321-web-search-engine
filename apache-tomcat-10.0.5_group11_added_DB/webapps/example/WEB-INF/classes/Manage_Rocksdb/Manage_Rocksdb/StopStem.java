package Manage_Rocksdb.Manage_Rocksdb;


import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StopStem {
	private Porter porter;
	private java.util.HashSet<String> stopWords;
	public boolean isStopWord(String str) {
		return stopWords.contains(str);	
	}
	public StopStem(String str) {
		super();
		porter = new Porter();
		stopWords = new java.util.HashSet<String>();
			
		// CHANGE THIS PART TO FILE CONTENT 
		//		stopWords.add("is");
		//		stopWords.add("am");
		//		stopWords.add("are");
		//		stopWords.add("was");
		//		stopWords.add("were");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(str));
			String line;
			while ((line = reader.readLine()) != null) {
				stopWords.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String stem(String str) {
		return porter.stripAffixes(str);
	}
	public static void main(String[] arg) {
		StopStem stopStem = new StopStem("stopwords-en.txt");
		String input="";
		try{
			do {
				System.out.print("Please enter a single English word: ");
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				input = in.readLine();
				if(input.length() > 0)
				{	
					if (stopStem.isStopWord(input))
						System.out.println("It should be stopped");
					else
			   			System.out.println("The stem of it is \"" + stopStem.stem(input)+"\"");
				}
			}
			while(input.length() > 0);
		}
		catch(IOException ioe) {
			System.err.println(ioe.toString());
		}
	}
}
