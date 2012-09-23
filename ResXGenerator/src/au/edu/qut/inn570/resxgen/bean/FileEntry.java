package au.edu.qut.inn570.resxgen.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FileEntry  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8790858315914267823L;
	private String id;
	private String value;
	private String translatedValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTranslatedValue() {
		return translatedValue;
	}
	public void setTranslatedValue(String translatedValue) {
		this.translatedValue = translatedValue;
	}
	
}
