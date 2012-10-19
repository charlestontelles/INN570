package au.edu.qut.inn570.resxgen.poc;

import java.net.URL;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.xml.soap.*;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Copy_2_of_BingAccess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Client client = new Client();
			
			WebResource webResource = client.resource("https://datamarket.accesscontrol.windows.net/v2/OAuth2-13/");
			client.setConnectTimeout(30000);
			client.setReadTimeout(30000);

			com.sun.jersey.api.representation.Form input = new Form();
			//input.add("client_id", "c3a826ba-743f-44f2-94fc-1ffc83a0bddf");worked
			//input.add("client_secret", "95vrnQVnBAyL7khBUlRQMGeJLQBQhfQbNqn+r5DJfGU="); worked
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
					//accessToken = URLDecoder.decode(accessToken);
				}
				//System.out.println("\ntoken: " + element.nextToken() + "\tvalue: " + element.nextToken());
			}
			 
			System.out.println("\nAccessToken: " + accessToken);
			// *************** GET LANGUAGES ******************
			/*
			webResource = client.resource("http://api.microsofttranslator.com/V2/Ajax.svc/GetLanguagesForTranslate");
			webResource.header("Authorization", accessToken);
			
			
			
			response = webResource.type("application/x-www-form-urlencoded")
			        .post(ClientResponse.class, input);
			
			output = response.getEntity(String.class);
			
			System.out.println("\n\nlanguages: " + output);
			*/
			
			// ****************** TRANSLATION *******************
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
	        SOAPElement bodyElement = body.addChildElement(envelope.createName("Translate", "", "http://api.microsofttranslator.com/V2"));
	        bodyElement.addChildElement("appId").addTextNode("");
	        bodyElement.addChildElement("text").addTextNode("Close");
	        bodyElement.addChildElement("from").addTextNode("en");
	        bodyElement.addChildElement("to").addTextNode("pt");
	        bodyElement.addChildElement("contentType").addTextNode("text/plain");
	        bodyElement.addChildElement("category").addTextNode("");
	        message.saveChanges();
	        
			
	     // POST the request
	        ClientResponse cr = service.header("SOAPAction", "\"http://api.microsofttranslator.com/V2/LanguageService/Translate\"").header("Authorization",  "Bearer " + accessToken).post(ClientResponse.class, message);
	        message = cr.getEntity(SOAPMessage.class);
				
					
			output = message.getSOAPPart().getEnvelope().getBody().getFirstChild().getFirstChild().getFirstChild().getTextContent();
			
			System.out.println("\n\ntranslation: " + output);
					
			service.delete();
			c.destroy();
			
			webResource.delete();
			client.destroy();
			

			System.out.println("testing bing access");
		} catch (Exception e) {
			System.out.println("error: "  + e);
		}

	}
	
	/*
	WebResource webResource = client.resource("https://datamarket.accesscontrol.windows.net/v2/OAuth2-13/");
	client.setConnectTimeout(30000);
	client.setReadTimeout(30000);
	
	webResource.type("application/x-www-form-urlencoded");
	

	MultivaluedMap queryParams = new MultivaluedMapImpl();
	queryParams.add("client_id", "c3a826ba-743f-44f2-94fc-1ffc83a0bddf");
	queryParams.add("client_secret", "95vrnQVnBAyL7khBUlRQMGeJLQBQhfQbNqn+r5DJfGU=");
	queryParams.add("scope", "http://api.microsofttranslator.com");
	queryParams.add("grant_type", "client_credentials");
	
	String s = webResource.queryParams(queryParams).post(
			String.class);
	
	System.out.println("testing bing access");
*/
}
