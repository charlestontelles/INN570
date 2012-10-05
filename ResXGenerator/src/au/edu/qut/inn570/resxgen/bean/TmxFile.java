package au.edu.qut.inn570.resxgen.bean;

import java.io.Serializable;

public class TmxFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7232057920989915628L;
	private String sourceLanguage;
	private String targetLanguage;
	private int counter = 0;
	private StringBuffer bf =  new StringBuffer();
	private String header = "" +
			"<?xml version=\"1.0\" ?>" + 
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
	}
	
	public void addElement(String value, String translatedValue){
		String str = "" +
				"\n\t\t<tu tuid=\""+counter+"\" creationdate=\"20121005T155451Z\" creationid=\"resxgen\" usagecount=\"1\" lastusagedate=\"20121005T155451Z\">" +
		"\n\t\t\t<tuv lang=\"" + this.sourceLanguage+ "\"  creationdate=\"20121005T155451Z\" creationid=\"resxgen\"> " +
		"\n\t\t\t\t<seg>" + value + "</seg>" +
		"\n\t\t\t</tuv>" +
		"\n\t\t\t<tuv lang=\""+this.targetLanguage+"\" creationdate=\"20121005T155451Z\" creationid=\"resxgen\" changedate=\"20121005T000000Z\" changeid=\"resxgen\"> " +
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
