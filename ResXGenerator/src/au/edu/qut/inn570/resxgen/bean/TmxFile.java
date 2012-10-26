package au.edu.qut.inn570.resxgen.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TmxFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7232057920989915628L;
	private String sourceLanguage;
	private String targetLanguage;
	private int counter = 0;
	private StringBuffer bf =  new StringBuffer();
	private String datetime = "20121005T155451Z";
	private String header = "" +
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + 
		"\n\t<tmx version=\"1.1\">" +
	"\n\t\t<header " +
	"\n\t\tcreationtool=\"http://resxgen.appspot.com\" " +
	"\n\t\tcreationtoolversion=\"Beta\" " +
	"\n\t\tsegtype=\"sentence\" " +
	"\n\t\to-tmf=\"TW4Win 2.0 Format\" " +
	"\n\t\tadminlang=\"EN-US\" " +
	"\n\t\tsrclang=\"EN-GB\" " +
	"\n\t\tdatatype=\"rtf\" " +
	"\n\t\tcreationdate=\"20121005T155451Z\" " +
	"\n\t\tcreationid=\"Translated\">" +
	"\n\t\t</header>";
	
	public TmxFile (String sourceLanguage, String targetLanguage){
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
		header = header.replace("EN-GB", sourceLanguage);
		bf.append(header);
		bf.append("<body>");
		this.counter = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'hhmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+10:00"));//Brisbane TimeZone
		this.datetime = dateFormat.format(new Date());
	}
	
	public void addElement(String value, String translatedValue){
		this.counter++;
		String str = "" +
				"\n\t\t<tu tuid=\""+counter+"\" creationdate=\""+this.datetime+"\" creationid=\"resxgen\" usagecount=\"1\" lastusagedate=\""+this.datetime+"\">" +
		"\n\t\t\t<tuv lang=\"" + this.sourceLanguage+ "\"  creationdate=\""+this.datetime+"\" creationid=\"resxgen\"> " +
		"\n\t\t\t\t<seg>" + value + "</seg>" +
		"\n\t\t\t</tuv>" +
		"\n\t\t\t<tuv lang=\""+this.targetLanguage+"\" creationdate=\""+this.datetime+"\" creationid=\"resxgen\" changedate=\""+this.datetime+"\" changeid=\"resxgen\"> " +
		"\n\t\t\t\t<seg>" + translatedValue +"</seg>" +
		"\n\t\t\t</tuv>" +
		"\n\t\t</tu>" ;
		bf.append(str);
	}
	
	public void endFile(){
		bf.append("\n\t</body>\n</tmx>");
	}

	public StringBuffer getBf() {
		return bf;
	}
	
}
