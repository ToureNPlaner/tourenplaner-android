package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.User;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager.ContentType;

public class BillingListHandler extends ConnectionHandler{

	public BillingListHandler(Observer listener, Session session) {
		super(listener, session);
	}

	@Override
	protected String getSuffix() {
		return "/authuser";
		//return "/listrequests";
	}

	@Override
	protected boolean isPost() {
		return false;
	}

	@Override
	protected Object handleInput(ContentType type, InputStream inputStream)throws Exception {
		ObjectMapper mapper = JacksonManager.getMapper(type);
		session.setBillingItemlist(BillingItem.parse(mapper.readValue(inputStream, JsonNode.class)));
	
		return true;
	}


}
