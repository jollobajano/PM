package jollobajano.pm.esb;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.helpers.KeyValuePair;

import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

public class PrintBody extends BaseAction
{
    
    protected ConfigTree config;

    public PrintBody(ConfigTree config) throws Exception { 
    } 

  
    public Message process(Message message) throws Exception{

	log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	log.debug("Body.class: " + message.getBody().get().getClass());
	log.debug("Body: " + message.getBody().get());
	log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	
	return message;
	
    }
}

