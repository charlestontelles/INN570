package au.edu.qut.inn570.resxgen.bing;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.NodeList;

import au.edu.qut.inn570.resxgen.bean.FileEntry;
import au.edu.qut.inn570.resxgen.bean.TmxEntry;
import au.edu.qut.inn570.resxgen.poc.SoapProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.representation.Form;

public class BingTranslator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4595526808298500540L;


	/**
	 * Gets an access token from bing server
	 * 
	 * @return access token
	 */
	public String getToken(){
		String token = "";
		try {
			Client client = new Client();
			
			WebResource webResource = client.resource("https://datamarket.accesscontrol.windows.net/v2/OAuth2-13/");
			client.setConnectTimeout(30000);
			client.setReadTimeout(30000);

			com.sun.jersey.api.representation.Form input = new Form();
			input.add("client_id", "c3a826ba-743f-44f2-94fc-1ffc83a0bddf");
			input.add("client_secret", "El329FrG3EoPo8EFb9ZqawIUrWu1uCMGGDO58Mk9kW8="); 
			input.add("scope", "http://api.microsofttranslator.com");
			input.add("grant_type", "client_credentials");

			// send HTTP POST
			ClientResponse response = webResource.type("application/x-www-form-urlencoded")
			        .post(ClientResponse.class, input);
			

			String output = response.getEntity(String.class);
			
			System.out.println("Firstoutput: " + output);
			StringTokenizer tk = new StringTokenizer(output, ",");
			StringTokenizer element;
			String accessToken = "";
			while(tk.hasMoreElements()){
				element = new StringTokenizer(tk.nextToken(),":");
				if (element.nextToken().contains("access_token")){
					accessToken = element.nextToken();
					accessToken = accessToken.replace("\"", "");
				}
			}
			 
			System.out.println("\nAccessToken: " + accessToken);
			
			client.destroy();
			
			token = accessToken;
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
		return token;
	}

	
	public void translate(List<FileEntry> entries, String sourceLanguage, String targetLanguage){
		try {
		
	        ClientConfig config = new DefaultClientConfig();
	        config.getClasses().add(SoapProvider.class);
	        Client c = Client.create(config);
	        c.addFilter(new LoggingFilter());
			
			
	        WebResource service = c.resource("http://api.microsofttranslator.com/V2/soap.svc");
	        //service.header("Authorization", "Bearer " + accessToken);
			//webResource.header("SOAPAction", "http://api.microsofttranslator.com/V2/LanguageService/GetLanguagesForTranslate");

	        MessageFactory messageFactory = MessageFactory.newInstance();
	        SOAPMessage message = messageFactory.createMessage();
	        SOAPPart soapPart = message.getSOAPPart();
	        SOAPEnvelope envelope = soapPart.getEnvelope();
	        SOAPBody body = envelope.getBody();
	        SOAPElement bodyElement = body.addChildElement(envelope.createName("TranslateArray", "", "http://api.microsofttranslator.com/V2"));
	        bodyElement.addChildElement("appId").addTextNode("");
	        SOAPElement textsElement = bodyElement.addChildElement("texts");
	        for (FileEntry entry : entries){
	        	textsElement.addChildElement("string","", "http://schemas.microsoft.com/2003/10/Serialization/Arrays").addTextNode(entry.getValue());
	        }    
	        bodyElement.addChildElement("from").addTextNode(sourceLanguage.substring(0,2));
	        bodyElement.addChildElement("to").addTextNode(targetLanguage.substring(0, 2));
	        message.saveChanges();
	        
			
	     // POST the request
	        ClientResponse cr = service.header("SOAPAction", "\"http://api.microsofttranslator.com/V2/LanguageService/TranslateArray\"").header("Authorization",  "Bearer " + getToken()).post(ClientResponse.class, message);
	        message = cr.getEntity(SOAPMessage.class);
	        
	        
	        
			NodeList translations = message.getSOAPPart().getEnvelope().getBody().getFirstChild().getFirstChild().getChildNodes();
			FileEntry currentEntry;
			TmxEntry newTmx;
			for (int i = 0; i < translations.getLength(); i++){
				currentEntry = entries.get(i);
				newTmx = new TmxEntry();
				newTmx.setTuID(0);
				newTmx.setUsageCount(1);
				newTmx.setCreationDate("20121010101010");
				newTmx.setLastUsageDate("20121010101010");
				newTmx.setCreationID("Bing Translator");
				newTmx.setSource(currentEntry.getValue());
				newTmx.setTarget(translations.item(i).getChildNodes().item(2).getTextContent());
				newTmx.setSourceLanguage(sourceLanguage);
				newTmx.setTargetLanguage(targetLanguage);
				currentEntry.getTmxEntries().add(newTmx);
				System.out.println("translation value: " + translations.item(i).getChildNodes().item(2).getTextContent());
			}
	        
	        c.destroy();
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
	}
}
