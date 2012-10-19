package au.edu.qut.inn570.resxgen.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.input.XmlStreamReader;

import au.edu.qut.inn570.resxgen.controller.FileUploadController;
@ViewScoped
@ManagedBean
@SessionScoped
public class FileEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8790858315914267823L;
	private String id;
	private String value;
	private String sourceLanguage = "";
	private String targetLanguage = "";
	private List<TmxEntry> tmxEntries;
	private String selectedEntry;

	private String translatedValue;
	private int tuID;

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

	public String getSourceLanguage() {
		return sourceLanguage;
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

	public List<TmxEntry> getTmxEntries() {
		return tmxEntries;
	}

	public void setTmxEntries(List<TmxEntry> tmxEntries) {
		this.tmxEntries = tmxEntries;
	}

	public String getSelectedEntry() {
		return selectedEntry;
	}

	public void setSelectedEntry(String selectedEntry) {
		this.selectedEntry = selectedEntry;
	}

	public void loadTmxEntries(String tmxResponse) {
		try {
			tmxEntries = new ArrayList<TmxEntry>();
			tmxResponse = tmxResponse.replace("<!DOCTYPE tmx SYSTEM \"tmx11.dtd\">", "");
			//FileUploadController.uploadedFile += "\n\n FILEEEEEEEE" + new String(response);
			InputStream is = new ByteArrayInputStream(tmxResponse.getBytes(Charset.forName("UTF-8")));
			//FileUploadController.uploadedFile += "\n\ncreated input stream:" + is.available();
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(is,
					"UTF-8");
			
			TmxEntry tmxEntry = null;
			StartElement startElement;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					startElement = event.asStartElement();
					// If we have a item element we create a new item
					if (startElement.getName().getLocalPart() == ("tu")) {

						tmxEntry = new TmxEntry();
						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals("tuid")) {
								tmxEntry.setTuID(new Integer(attribute
										.getValue()));
							} else if (attribute.getName().toString()
									.equals("creationdate")) {
								tmxEntry.setCreationDate(attribute.getValue());
							} else if (attribute.getName().toString()
									.equals("creationid")) {
								tmxEntry.setCreationID(attribute.getValue());
							} else if (attribute.getName().toString()
									.equals("usagecount")) {
								tmxEntry.setUsageCount(new Integer(attribute
										.getValue()));
							} else if (attribute.getName().toString()
									.equals("lastusagedate")) {
								tmxEntry.setLastUsageDate(attribute.getValue());
							}
						}
					} else if (startElement.getName().getLocalPart() == ("tuv")) {
						Iterator<Attribute> attributes = startElement
								.getAttributes();

						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals("lang")) {
								if (attribute.getValue().equalsIgnoreCase(
										this.targetLanguage))
									tmxEntry.setTargetLanguage(attribute
											.getValue());
								else
									tmxEntry.setSourceLanguage(attribute
											.getValue());
							}
						}
					}

					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart()
								.equals("seg")) {
							if (tmxEntry != null) {
								event = eventReader.nextEvent();
								if (tmxEntry.getTargetLanguage() == null)
									tmxEntry.setSource(event.asCharacters().getData());
								else
									tmxEntry.setTarget(event.asCharacters().getData());
								

							}
						}
					}

				}

				// If we reach the end of an item element we add it to the list

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == ("tu")) {
						if (tmxEntry.getSource().equalsIgnoreCase(this.value)) {
							tmxEntries.add(tmxEntry);
						}
					}
				}

			}// close while

			// add default value
			tmxEntry = new TmxEntry();
			tmxEntry.setCreationDate("");
			tmxEntry.setCreationID("");
			tmxEntry.setLastUsageDate("");
			tmxEntry.setSource("");
			tmxEntry.setTarget("NO MATCH");
			if (tmxEntries.size() <= 0)
				tmxEntries.add(tmxEntry);
			//selectedEntry = tmxEntries.get(0).getTarget();
		} catch (Exception e) {
			System.out.println("error: " + e);
			// TODO: handle exception
			// add default value
			TmxEntry tmxEntry = new TmxEntry();
			tmxEntry.setCreationDate("");
			tmxEntry.setCreationID("");
			tmxEntry.setLastUsageDate("");
			tmxEntry.setSource("");
			tmxEntry.setTarget("NO_MATCH");
			if(tmxEntries.size() <= 0)
				tmxEntries.add(tmxEntry);
			//selectedEntry = tmxEntries.get(0).getTarget();
		}
	}


}
