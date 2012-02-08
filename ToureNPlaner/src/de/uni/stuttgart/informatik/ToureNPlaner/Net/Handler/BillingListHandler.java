package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.User;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager.ContentType;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.BillingScreen;

public class BillingListHandler extends ConnectionHandler{
private ArrayList<BillingItem> billinglist;
	public BillingListHandler(Observer listener, Session session) {
		super(listener, session);
	
	}

	@Override
	protected String getSuffix() {
		//return "/authuser";
		return "/listrequests?limit=100&offset=0";
	}

	@Override
	protected boolean isPost() {
		return false;
	}

	@Override
	protected Object handleInput(ContentType type, InputStream inputStream)throws Exception {
		ObjectMapper mapper = JacksonManager.getMapper(type);
		BillingScreen.billinglist.clear();
		BillingScreen.billinglist = ((BillingItem.parse(mapper.readValue(inputStream, JsonNode.class))));
	    return true;
	}


}
