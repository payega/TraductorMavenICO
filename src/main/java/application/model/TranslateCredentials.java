package application.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TranslateCredentials {
	
	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
	
	public TranslateCredentials() {
		super();
		JsonObject vcap = null;
    	System.out.println("VCAP_SERVICES " + System.getenv("VCAP_SERVICES") + "*************");
       	if (System.getenv("VCAP_SERVICES") != null && !System.getenv("VCAP_SERVICES").equals("{}")){
	    	vcap = new JsonParser().parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
	    	System.out.println("vcap " + vcap.toString()); 
       	}
       	try{
	    	JsonObject language = vcap.getAsJsonArray("language_translator").get(0).getAsJsonObject();
	    	System.out.println("language " + vcap.toString());
	    	JsonObject credentials = language.getAsJsonObject("credentials");
	    	System.out.println("credentials " + vcap.toString());
	    	             
	    	user = credentials.get("username").getAsString();
	    	pass = credentials.get("password").getAsString();
	    	url = credentials.get("url").getAsString();
    	}catch(Exception e){
        	user = "a7e3f18d-4c1b-41f2-b3ee-15d97f4800ef";
        	pass = "nOSLjlfwrexb";
        	url = "https://gateway.watsonplatform.net/language-translator/api";
    	}
	}

	private String url = "";
	private String user = "";
	private String pass = "";

}
