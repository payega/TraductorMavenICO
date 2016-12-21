package application.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SynthesizeCredentials {
	
	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
	
	public SynthesizeCredentials() {
		super();
		JsonObject vcap = null;
    	System.out.println("VCAP_SERVICES " + System.getenv("VCAP_SERVICES") + "*************");
       	if (System.getenv("VCAP_SERVICES") != null && !System.getenv("VCAP_SERVICES").equals("{}")){
	    	vcap = new JsonParser().parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
	    	System.out.println("vcap " + vcap.toString()); 
       	}
       	try{
	    	JsonObject tts = vcap.getAsJsonArray("text_to_speech").get(0).getAsJsonObject();
	    	System.out.println("text_to_speech " + tts.toString());
	    	JsonObject credentials = tts.getAsJsonObject("credentials");
	    	System.out.println("credentials " + vcap.toString());
	    	             
	    	user = credentials.get("username").getAsString();
	    	pass = credentials.get("password").getAsString();
	    	url = credentials.get("url").getAsString();
    	}catch(Exception e){
        	user = "9e5bcb2c-c25b-47cd-aa58-16d446bcc045";
        	pass = "eFqyIXauinY7";
        	url = "https://stream.watsonplatform.net/text-to-speech/api";
    	}		

	}

	private String url = "";
	private String user = "";
	private String pass = "";

}
