package au.edu.qut.inn570.resxgen.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
@ViewScoped
@ManagedBean
@SessionScoped
public class TmxEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7187612323820093098L;
	private int tuID;
	private String creationDate;
	private String creationID;
	private int usageCount;
	private String lastUsageDate;
	private String source;
	private String target;
	private String sourceLanguage;
	private String targetLanguage;

	
	public int getTuID() {
		return tuID;
	}
	public void setTuID(int tuID) {
		this.tuID = tuID;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreationID() {
		return creationID;
	}
	public void setCreationID(String creationID) {
		this.creationID = creationID;
	}
	public int getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}
	public String getLastUsageDate() {
		return lastUsageDate;
	}
	public void setLastUsageDate(String lastUsageDate) {
		this.lastUsageDate = lastUsageDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}
	public String getTargetLanguage() {
		return targetLanguage;
	}
	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}
	
	
	
}
