package au.edu.qut.inn570.resxgen.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;

import javax.faces.application.FacesMessage;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;  
  
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;  
@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable{  
	
	private String uploadedFile;
  
    public String getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2970866286751151250L;
    public void handleFileUpload(FileUploadEvent event) throws IOException {  
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        StringWriter writer = new StringWriter();
        IOUtils.copy(event.getFile().getInputstream(), writer, "UTF-8");
        String theString = writer.toString();
        this.uploadedFile = theString;
        //System.out.println("file: " + theString);
    }  
} 