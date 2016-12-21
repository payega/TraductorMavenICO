package application.model;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Language;

public class LanguageDetection {
	
	public LanguageDetection() {
		
	}
	public Language detect (String phrase) {
		
    	AlchemyCredentials ac = new AlchemyCredentials();

	    AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey(ac.getAPIKey());

	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(AlchemyLanguage.TEXT, phrase);
	    Language languaje = new Language();
	    
	    try{
	    	languaje = service.getLanguage(params).execute();
	    	System.out.println(languaje.toString());
	    }catch (RuntimeException re){
	    	languaje.setLanguage("unknow");
	    	return languaje;
	    }
	    return languaje;
	}

}
