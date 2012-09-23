package au.edu.qut.inn570.resxgen.controller;

import java.io.IOException;

import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

import au.edu.qut.inn570.resxgen.bean.FileEntry;

@ViewScoped

@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {

	private String uploadedFile;
	private List<FileEntry> entries = new ArrayList<FileEntry>();
	private String language;

	public String getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	
	public List<FileEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<FileEntry> entries) {
		this.entries = entries;
	}


	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -2970866286751151250L;

	public void handleFileUpload(FileUploadEvent event) throws IOException,
			XMLStreamException {
		//FacesMessage msg = new FacesMessage("Succesful", event.getFile()
		//		.getFileName() + " is uploaded.");
		//FacesContext.getCurrentInstance().addMessage(null, msg);
		StringTokenizer st = new StringTokenizer(event.getFile().getFileName(),".");
		st.nextToken();
		this.language = "File Language = "+st.nextToken();
		
		InputStream is = event.getFile().getInputstream();
		parseFile(is);
		
		/* show file in the screen
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, "UTF-8");
		String theString = writer.toString();
		this.uploadedFile = theString;
		System.out.println("file: " + theString);
		*/
	}

	@SuppressWarnings("unchecked")
	private void parseFile(InputStream is) {
		// First create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader
		try {
			XMLEventReader eventReader = inputFactory.createXMLEventReader(is);

			FileEntry entry = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					// If we have a item element we create a new item
					if (startElement.getName().getLocalPart() == ("data")) {

						entry = new FileEntry();

						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals("name")) {
								entry.setId(attribute.getValue());
							}

						}
					}

					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart()
								.equals("value")) {
							if (entry != null) {
								event = eventReader.nextEvent();
								entry.setValue(event.asCharacters().getData());
							}

						}
					}
				}
				
		        // If we reach the end of an item element we add it to the list
		        if (event.isEndElement()) {
		          EndElement endElement = event.asEndElement();
		          if (endElement.getName().getLocalPart() == ("data")) {
						if (entry.getId().contains(".Text")) {
							entries.add(entry);
						}
		          }
		        }
			}
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
	}
}