package au.edu.qut.inn570.resxgen.controller;

import java.io.IOException;

import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.FileUploadEvent;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import au.edu.qut.inn570.resxgen.bean.FileEntry;
import au.edu.qut.inn570.resxgen.bean.TmxFile;
import au.edu.qut.inn570.resxgen.bing.BingTranslator;
/**
 * The Controller is the intermediate layer responsible for the communication
 * between UI and Beans (POJOS)
 * 
 * 
 * @author Charleston
 *
 */
@ViewScoped
@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {

	public static String uploadedFile;
	public static String tmxFile;
	public String originalUploadedFile;
	public static String staticTargetLanguage;
	private List<FileEntry> entries = new ArrayList<FileEntry>();
	private String sourceLanguage;

	private Map<String, String> languages;
	private String targetLanguage;

	private int tabIndex = 0;
	
	private Integer progress; 
	
	private String myMemoryUserName = "";
	private String myMemoryKey = "";
	
	private String tmOption = "";
	
	private BingTranslator bingTranslator;

	public FileUploadController() {
		progress = 0;
		uploadedFile = "";
		myMemoryUserName = "charlestontelles";
		myMemoryKey = "1tP9yey56yAoU";
		tmOption = "My Memory";
		languages = new HashMap<String, String>();
		languages.put("en-US", "en-US");
		languages.put("de-DE", "de-DE");
		languages.put("pt-BR", "pt-BR");
		languages.put("fr-FR", "fr-FR");
		languages.put("zh-CN", "zh-CN");
		languages.put("es-ES", "es-ES");
		languages.put("ko-KR", "ko-KR");
		languages.put("ja-JA", "ja-JA");
		languages.put("nl-NL", "nl-NL");
		languages.put("tr-TR", "tr-TR");
		bingTranslator = new BingTranslator();
	}

	
	
	public String getTmOption() {
		return tmOption;
	}



	public void setTmOption(String tmOption) {
		this.tmOption = tmOption;
	}



	public List<FileEntry> getEntries() {
		return entries;
	}



	public String getMyMemoryUserName() {
		return myMemoryUserName;
	}



	public void setMyMemoryUserName(String myMemoryUserName) {
		this.myMemoryUserName = myMemoryUserName;
	}



	public String getMyMemoryKey() {
		return myMemoryKey;
	}



	public void setMyMemoryKey(String myMemoryKey) {
		this.myMemoryKey = myMemoryKey;
	}



	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public void setEntries(List<FileEntry> entries) {
		this.entries = entries;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public Map<String, String> getLanguages() {
		return languages;
	}

	public void setLanguages(Map<String, String> languages) {
		this.languages = languages;
	}

	public String getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2970866286751151250L;
	
	public void handleSourceLanguage(ValueChangeEvent event){
		//System.out.println("event= " + event.getNewValue());
		this.sourceLanguage = ""+event.getNewValue();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException,
			XMLStreamException {
		// FacesMessage msg = new FacesMessage("Succesful", event.getFile()
		// .getFileName() + " is uploaded.");
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		StringTokenizer st = new StringTokenizer(event.getFile().getFileName(),
				".");
		st.nextToken();
		this.sourceLanguage = st.nextToken();

		InputStream is = event.getFile().getInputstream();
		// loadedFile = event.getFile().getInputstream();
		parseFile(is);

		// show file in the screen
		StringWriter writer = new StringWriter();
		is.reset();
		IOUtils.copy(is, writer, "UTF-8");
		String theString = writer.toString();
		this.uploadedFile = theString;
		this.originalUploadedFile = theString;
		// System.out.println("file: " + theString);
	}

	@SuppressWarnings("unchecked")
	private void parseFile(InputStream is) {
		// First create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader
		try {
			XMLEventReader eventReader = inputFactory.createXMLEventReader(is,
					"UTF-8");
			FileEntry entry = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					// If we have a item element we create a new item
					if (startElement.getName().getLocalPart() == ("data")) {

						entry = new FileEntry();

						Iterator<Attribute> attributes = startElement
								.getAttributes();
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
								entry.setValue(eventReader.getElementText());
								event = eventReader.nextEvent();
							}

						}
					}
				}

				// If we reach the end of an item element we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == ("data")) {
						//if (entry.getId().contains(".Text")) {
						if (!entry.getId().contains(">>") &&
							!entry.getValue().contains("System.Windows") &&
							!entry.getValue().contains("1") &&
							!entry.getValue().contains("2") &&
							!entry.getValue().contains("3") &&
							!entry.getValue().contains("4") &&
							!entry.getValue().contains("5") &&
							!entry.getValue().contains("5") &&
							!entry.getValue().contains("7") &&
							!entry.getValue().contains("8") &&
							!entry.getValue().contains("9") &&
							!entry.getValue().contains("0")
							)
							entries.add(entry);
						//}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
	}

	public void handleTranslate() {
		try {
						
			if(tmOption.equalsIgnoreCase("My Memory") ||
				tmOption.equalsIgnoreCase("Both")){
				Client client = new Client();
				WebResource webResource = client
						.resource("http://mymemory.translated.net/api/get");
				
				client.setConnectTimeout(30000);
				client.setReadTimeout(30000);
	
				for (FileEntry entry : entries) {
					MultivaluedMap queryParams = new MultivaluedMapImpl();
					queryParams.add("q", entry.getValue());
					queryParams.add("langpair", this.sourceLanguage + "|"
							+ this.targetLanguage);
					queryParams.add("of", "tmx");
					
					if (!this.myMemoryUserName.equalsIgnoreCase("none") && 
							!this.myMemoryKey.equalsIgnoreCase("none")){
						queryParams.add("user",this.myMemoryUserName);
						queryParams.add("key",this.myMemoryKey);
					}
					
					
					String s = webResource.queryParams(queryParams).get(
							String.class);
					entry.setSourceLanguage(this.sourceLanguage);
					entry.setTargetLanguage(this.targetLanguage);
					entry.loadTmxEntries(s);
					
	
				}
				webResource.delete();
				client.destroy();
			
			}
			
			// CALL BING TRANSLATOR
			
			if (tmOption.equalsIgnoreCase("Microsoft Translator (Bing)")){				
				bingTranslator.translate(entries, this.sourceLanguage, this.targetLanguage,true);
			} else if (tmOption.equalsIgnoreCase("Both")){
				bingTranslator.translate(entries, this.sourceLanguage, this.targetLanguage,false);
			}
			
			
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Successful",
					"Memory Translation Completed"));
			

		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Error", "" + e));

		}
	}
	
	public void handleUpdate(){
		try {
			if (this.myMemoryKey.equalsIgnoreCase("none") ||
					this.myMemoryUserName.equalsIgnoreCase("none")){
				FacesContext context = FacesContext.getCurrentInstance();

				context.addMessage(null, new FacesMessage("Validation Message",
						"Cannot Save without MyMemory key (see Config Tab)"));
				
				return;
				
			}
				
			
			Client client = new Client();
			WebResource webResource = client
					.resource("http://mymemory.translated.net/api/set");
			
			client.setConnectTimeout(30000);
			client.setReadTimeout(30000);

			for (FileEntry entry : entries) {
				MultivaluedMap queryParams = new MultivaluedMapImpl();
				queryParams.add("seg", entry.getValue());
				queryParams.add("tra", entry.getTmxEntries().get(0).getTarget());
				queryParams.add("langpair", this.sourceLanguage + "|"
						+ this.targetLanguage);
				
				if (!this.myMemoryUserName.equalsIgnoreCase("none") && 
						!this.myMemoryKey.equalsIgnoreCase("none")){
					queryParams.add("user",this.myMemoryUserName);
					queryParams.add("key",this.myMemoryKey);
				}
				
				
				String s = webResource.queryParams(queryParams).get(
						String.class);
				
				System.out.println("\nupdate response: " + s);

			}
			
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Sucessful",
					"Translation Memory Updated"));
			
			webResource.delete();
			client.destroy();
			
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Error",
					""+e));
		}
		

	}

	public void handleExport() {
		try {
			//new ByteArrayInputStream(tmxResponse.getBytes(Charset.forName("UTF-8")));
			//System.out.println("\n\n");
			this.uploadedFile = this.originalUploadedFile;
			TmxFile exportFile = new TmxFile(this.sourceLanguage, this.targetLanguage);
			
			for (FileEntry entry : entries) {
				System.out.println(entry.getValue() + " - "	+ entry.getTmxEntries().get(0).getTarget());
				this.uploadedFile = this.uploadedFile.replace("<value>"+entry.getValue()+"</value>",	"<value>"+entry.getTmxEntries().get(0).getTarget()+"</value>");
				exportFile.addElement(entry.getValue(), entry.getTmxEntries().get(0).getTarget());
			}

			exportFile.endFile();
			this.tmxFile = exportFile.getBf().toString();
			this.tabIndex = 2;
			this.staticTargetLanguage = this.targetLanguage;
			
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Successful",
					"File Exported"));
		} catch (Exception e) {
			System.out.println("error: " + e);
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Error", "" + e));
		}
	}
	
	public void handleUserNameSelection(ValueChangeEvent event){
		System.out.println("triggered");
		if(((SelectOneMenu)event.getSource()).getClientId().contains("username")){
			this.myMemoryUserName = ""+event.getNewValue();
		} else {
			this.myMemoryKey = ""+event.getNewValue();
		}
	}
	
	public void handleTmOptionSelection(ValueChangeEvent event){
		if(((SelectOneMenu)event.getSource()).getClientId().contains("tmOption")){
			this.tmOption = ""+event.getNewValue();
		}
	}
	
	public void handleTranslateChange(ValueChangeEvent event){
		System.out.println("triggered");
		String newValue = ""+event.getNewValue();
		//tabView:pageForm:dataTable:3:strTranslate
		String newEvent = ((SelectOneMenu)event.getSource()).getClientId();		
		//String itemNumber = newEvent.substring(27,newEvent.indexOf(":", 27));
		//System.out.println("triggered: " + newValue + " " + newEvent);
		if (newValue.contains("0")){
			newValue = newValue.substring(0,newValue.indexOf("0"));
		}else if (newValue.contains("1")){
			newValue = newValue.substring(0,newValue.indexOf("1"));
		}else if (newValue.contains("2")){
			newValue = newValue.substring(0,newValue.indexOf("2"));
		}if (newValue.contains("3")){
			newValue = newValue.substring(0,newValue.indexOf("3"));
		}else if (newValue.contains("4")){
			newValue = newValue.substring(0,newValue.indexOf("4"));
		}else if (newValue.contains("5")){
			newValue = newValue.substring(0,newValue.indexOf("5"));
		}else if (newValue.contains("6")){
			newValue = newValue.substring(0,newValue.indexOf("6"));
		}else if (newValue.contains("7")){
			newValue = newValue.substring(0,newValue.indexOf("7"));
		}else if (newValue.contains("8")){
			newValue = newValue.substring(0,newValue.indexOf("8"));
		}else if (newValue.contains("9")){
			newValue = newValue.substring(0,newValue.indexOf("9"));
		}
		//entry.tmxEntries[0].target
		((SelectOneMenu)event.getSource()).setValue(newValue);
		//System.out.println("trigger");
		
	}
		

}