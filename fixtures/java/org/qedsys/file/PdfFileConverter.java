package org.qedsys.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class PdfFileConverter {
	public static void main(String[] args) throws IOException{
		/*	PdfFileConverter pdfConverter = new PdfFileConverter();
		pdfConverter.setPDF("./FitNesseRoot/files/testFiles/AgileTestingOverview.pdf");
		pdfConverter.getNumberOfPages();
		pdfConverter.getTitle();
		pdfConverter.getAuthor();
		pdfConverter.getSubject();
		pdfConverter.getKeywords();
		pdfConverter.getCreator();
		pdfConverter.getProducer();
		pdfConverter.getCreationDate();
		pdfConverter.getModificationDate();
		pdfConverter.isEncrypted();
		pdfConverter.setStartPage(0);
		pdfConverter.setEndPage(15);
		pdfConverter.writeToTextFile("./FitNesseRoot/files/testFiles/output.txt");		 */
	}

	File file;
	PDDocument document;
	PDDocumentInformation docInfo;
	PDFTextStripper textStripper;
	int startPageValue=0;
	int endPageValue=0;
	private static final Logger LOG = LoggerFactory.getLogger(PdfFileConverter.class);

	public PdfFileConverter(){

	}

	public void setPdf(String pdfFile){
		try {
			file = new File(pdfFile);
			LOG.info("PDF File is set to: " + pdfFile);
			document = PDDocument.load(file);
			docInfo = document.getDocumentInformation();
			LOG.info("PDF File has been loaded.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle(){
		LOG.info("Title: " + docInfo.getTitle());
		return docInfo.getTitle();
	}

	public String getAuthor(){
		LOG.info("Author: " + docInfo.getAuthor());
		return docInfo.getAuthor();
	}

	public String getSubject(){
		LOG.info("Subject: " + docInfo.getSubject());
		return docInfo.getSubject();
	}

	public String getKeywords(){
		LOG.info("Keywords: " + docInfo.getKeywords());
		return docInfo.getKeywords();
	}

	public String getCreator(){
		LOG.info("Creator: " + docInfo.getCreator());
		return docInfo.getCreator();
	}

	public String getProducer(){
		LOG.info("Producer: " + docInfo.getProducer());
		return docInfo.getProducer();
	}

	public String getCreationDate() throws IOException{
		LOG.info("Created: " + docInfo.getCreationDate().toString());
		return docInfo.getCreationDate().toString();
	}

	public String getModificationDate() throws IOException{
		LOG.info("Last modified: " + docInfo.getModificationDate().toString());
		return docInfo.getModificationDate().toString();
	}

	public int getNumberOfPages(){
		LOG.info("Number of Pages? " + document.getNumberOfPages());
		return document.getNumberOfPages();
	}

	public boolean isEncrypted(){
		LOG.info("Document Encrypted? " + document.isEncrypted());
		return document.isEncrypted();
	}

	public void setStartPage(int startPageValue){
		this.startPageValue=startPageValue;
	}

	public void setEndPage(int endPageValue){
		this.endPageValue=endPageValue;
	}

	public String getText(){
		String text = "";
		try {
			textStripper = new PDFTextStripper();
			if(endPageValue!=0 && (endPageValue > startPageValue)){
				textStripper.setStartPage(startPageValue);
				textStripper.setEndPage(endPageValue);
				LOG.info("File will be read from: " + startPageValue + " to " + endPageValue);
				text = textStripper.getText(document);
			}
			else if(startPageValue > endPageValue){
				throw new IndexOutOfBoundsException("The start value cannot be greater than the end value.");
			}
			else
				text = textStripper.getText(document);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			document.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	public void writeToTextFile(String fileName){
		File output = new File(fileName);
		String outputText = getText();
		LOG.info("Output text set to: " + outputText);
		try (FileOutputStream fop = new FileOutputStream(output)) {
			if (!file.exists()){
				LOG.info("File does not exist.  Creating new file: " + fileName);
				file.createNewFile();
				LOG.info("Done writing to file: " + fileName);
			}
			byte[] contentInBytes = outputText.getBytes();
			fop.write(contentInBytes);
			fop.flush();fop.close();
			LOG.info("Done writing to file: " + fileName);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
