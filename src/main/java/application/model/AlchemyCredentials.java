package application.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlchemyCredentials {
	
	public String getAPIKey() {
		return apiKey;
	}

	
	public AlchemyCredentials() {
		super();
		JsonObject vcap = null;
    	System.out.println("VCAP_SERVICES " + System.getenv("VCAP_SERVICES") + "*************");
       	if (System.getenv("VCAP_SERVICES") != null && !System.getenv("VCAP_SERVICES").equals("{}")){
	    	vcap = new JsonParser().parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
	    	System.out.println("vcap " + vcap.toString()); 
       	}
       	try{
	    	JsonObject tts = vcap.getAsJsonArray("alchemy_api").get(0).getAsJsonObject();
	    	System.out.println("alchemy_api " + tts.toString());
	    	JsonObject credentials = tts.getAsJsonObject("credentials");
	    	System.out.println("credentials " + vcap.toString());
	    	             
	    	apiKey = credentials.get("apikey").getAsString();	    	
	    	url = credentials.get("url").getAsString();
    	}catch(Exception e){
    		apiKey = "dd6456aa5764b68285e2034fb0dc5b524621053c";
        	url = "https://gateway-a.watsonplatform.net/calls";
    	}		

	}

	private String apiKey = "";
	private String url = "";


}
