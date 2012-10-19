package au.edu.qut.inn570.resxgen.poc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
 
@Provider
@Consumes(MediaType.TEXT_XML)
@Produces(MediaType.TEXT_XML)
public class SoapProvider implements MessageBodyWriter<SOAPMessage>, MessageBodyReader<SOAPMessage> {
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return SOAPMessage.class.isAssignableFrom(aClass);
    }
 
    public SOAPMessage readFrom(Class<SOAPMessage> soapEnvelopeClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> stringStringMultivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            StreamSource messageSource = new StreamSource(inputStream);
            SOAPMessage message = messageFactory.createMessage();
            SOAPPart soapPart = message.getSOAPPart();
            soapPart.setContent(messageSource);
            return message;
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public long getSize(SOAPMessage soapMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
 
    public void writeTo(SOAPMessage soapMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            soapMessage.writeTo(outputStream);
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }
 
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass.isAssignableFrom(SOAPMessage.class);
    }
}
