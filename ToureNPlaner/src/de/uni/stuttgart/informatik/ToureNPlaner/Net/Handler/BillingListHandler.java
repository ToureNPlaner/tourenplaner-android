package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager.ContentType;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;

public class BillingListHandler extends ConnectionHandler {
	private int limit;
	private int offset;
	private int mode = 0;
	private int id = 0;
	private String algSuffix = "";

	public BillingListHandler(Observer listener, Session session, int limit, int offset,int mode) {
		super(listener, session);
		this.limit = limit;
		this.offset = offset;
	}
	public BillingListHandler(Observer listener, Session session, int id,String algSuffix, int mode) {
		super(listener, session);
	this.id = id;
	this.mode = mode;
	this.algSuffix = algSuffix;
	}
	public int getMode(){
		return this.mode;
	}
	public String getAlgSuffix(){
		return this.algSuffix;
	}
	@Override
	protected String getSuffix() {
		if(mode == 0){
		return "/listrequests?details=nojson&limit=" + limit + "&offset=" + offset;
		}if(mode== 1){
			return "/getresponse?id="+id;
		}
		return "";
	}

	@Override
	protected boolean isPost() {
		return false;
	}

	@Override
	protected Object handleInput(ContentType type, InputStream inputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getMapper(type);
		if(mode == 0){
		return BillingItem.parse(mapper.readValue(inputStream, JsonNode.class));
		}if(mode== 1){
			Result result = Result.parse(type, inputStream);
			return result;
		}
		return true;
	}
}
