package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import java.io.InputStream;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager.ContentType;

public class BillingRequestHandler extends ConnectionHandler {
	private int id = 0;
	private String algSuffix = "";
	public BillingRequestHandler(Observer listener, Session session, int id,String algSuffix, int mode) {
		super(listener, session);
	this.id = id;

	this.algSuffix = algSuffix;
	}
	
	public String getAlgSuffix(){
		return algSuffix;
	}

	@Override
	protected String getSuffix() {
		return "/getresponse?id="+id;
	}

	@Override
	protected boolean isPost() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object handleInput(ContentType type, InputStream inputStream)
			throws Exception {
		Result result = Result.parse(type, inputStream);
		return result;
	}

}
