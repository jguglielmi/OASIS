	package org.qedsys.file;

	
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.BufferedReader;
	import java.io.IOException;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import java.util.Arrays;
	
		public class TextFileReader {
	private static final Logger LOG = LoggerFactory.getLogger(TextFileReader.class);
	FileReader fileReader;
	BufferedReader bufferedReader;
	String fileName="";
	
			public TextFileReader(){
			}
			
			public BufferedReader setFile(String fileName) throws FileNotFoundException{
			this.fileName = fileName;
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			LOG.info("Reading file: " + fileName);
			return bufferedReader;
			}
	
			public String readNextLineOf(BufferedReader bufferedReader) throws IOException{
			String line = bufferedReader.readLine();
			LOG.info("Line contains: " + line);
			return line;
			}
			
			public String readToEndOfFile(BufferedReader bufferedReader) throws IOException{
			String line="";
			while(((line = bufferedReader.readLine()) != null)){
				line +=line;
			}
				LOG.info("End of file reached.");
				return line;			
			}
			
			public String[] getArray(BufferedReader bufferedReader) throws IOException{
			String[] array = new String[]{};
			String line="";
			int counter=0;
			while(((line = bufferedReader.readLine()) != null)){
				array[counter]=line;
				counter++;
			}
				LOG.info("Array returned.");
				return array;			
			}
			
			public void closeFile(BufferedReader bufferedReader) throws IOException{
			bufferedReader.close();
			LOG.info("Reader closed.");
			}
	}