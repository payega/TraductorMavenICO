/*******************************************************************************
 * Copyright (c) 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/ 
package application.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import application.model.LanguageDetection;
import application.model.SynthesizeCredentials;
import application.model.TranslateCredentials;

@ApplicationPath("traductor")
@Path("/")
public class LibertyRestEndpoint extends Application {
	
    @POST
    @Path("/text")
    @Produces("text/plain")   // sends JSON
    public String hello(@FormParam( "phrase" ) String phrase, @FormParam( "language" ) String targetLang) {

    	String decodePhrase = new String(Base64.getDecoder().decode(phrase.getBytes()));
    	System.out.println(decodePhrase);
    	
    	LanguageDetection ld = new LanguageDetection();
    	com.ibm.watson.developer_cloud.alchemy.v1.model.Language sourceLang = ld.detect(decodePhrase);
    	
    	TranslateCredentials tc = new TranslateCredentials();
        LanguageTranslator service = new LanguageTranslator();
        
    	service.setEndPoint(tc.getUrl());
    	service.setUsernameAndPassword(tc.getUser(), tc.getPass());
    	
    	TranslationResult translationResult = null;
    	
    	Language lans = getLanguaje(sourceLang.getLanguage().toLowerCase());
    	Language lant = getLanguaje(targetLang.toLowerCase());
    	
    	try{
    		if (lans != null && lant != null)
    			translationResult = service.translate(decodePhrase, lans, lant).execute();
    		else
    			throw new Exception();
    	}catch (Exception  e){
    		//return Base64.getEncoder().encode("Error detecting languaje or source/target translation is not implemented".getBytes());
    		return "Error detecting languaje or source/target translation is not implemented";
    	}
    	JsonObject result = new JsonParser().parse(translationResult.toString()).getAsJsonObject();
    	System.out.println(result);
    	JsonObject translations = result.getAsJsonArray("translations").get(0).getAsJsonObject();
    	System.out.println(translations);
    	String translation = translations.get("translation").getAsString();
    	//return Base64.getEncoder().encode(translation.getBytes());
    	return translation;
    }
    
    @GET
    @Path("/voice/{phrase}")
    @Produces("audio/x-wav")
    public byte[]   synthesite(@PathParam( "phrase" ) String phrase) {

    	String decodePhrase = new String(Base64.getDecoder().decode(phrase.getBytes()));
       	LanguageDetection ld = new LanguageDetection();
    	com.ibm.watson.developer_cloud.alchemy.v1.model.Language languaje = ld.detect(decodePhrase);
    	
    	SynthesizeCredentials sc = new SynthesizeCredentials();
    	
    	  try {

    		  TextToSpeech service = new TextToSpeech();
    		  service.setUsernameAndPassword(sc.getUser(), sc.getPass());
    		  InputStream stream = null;
    	    	if( languaje.getLanguage().equals("english")){
    	    		stream = service.synthesize(decodePhrase, Voice.EN_LISA, AudioFormat.WAV).execute();
    	    	}else{
    	    		if( languaje.getLanguage().equals("spanish")){
    	    			stream = service.synthesize(decodePhrase, Voice.ES_LAURA, AudioFormat.WAV).execute();
    	    		}else{
    	   	    		if( languaje.getLanguage().equals("french")){
        	    			stream = service.synthesize(decodePhrase, Voice.FR_RENEE, AudioFormat.WAV).execute();
        	    		}else{
        	   	    		if( languaje.getLanguage().equals("italian")){
            	    			stream = service.synthesize(decodePhrase, Voice.IT_FRANCESCA, AudioFormat.WAV).execute();
            	    		}else{
            	   	    		if( languaje.getLanguage().equals("partuguese")){
                	    			stream = service.synthesize(decodePhrase, Voice.PT_ISABELA, AudioFormat.WAV).execute();
                	    		}else{
                	    			stream = service.synthesize("Error getting stream. Sorry" , Voice.EN_LISA, AudioFormat.WAV).execute();
                	    		}
            	    		}
        	    		}    	    			
    	    		}
    	    	}

    		    InputStream in = WaveUtils.reWriteWaveHeader(stream);
    		    
    	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	        int len;
    	        byte[] buffer = new byte[4096];
    	        while ((len = in.read(buffer, 0, buffer.length)) != -1) {
    	            baos.write(buffer, 0, len);
    	        }
 
    	        baos.flush();
    	        baos.close();
    		    in.close();
    		    stream.close();
       	        System.out.println("Server size: " + baos.size());
    	        return Base64.getEncoder().encode(baos.toByteArray());    		    

    		  }
    		  catch (Exception e) {
    		    e.printStackTrace();
    		    return null;
    		  }
    	  

    }
    
    private Language getLanguaje(String lang){
    	
    	switch(lang) {
    	   case "english" :
    		   return Language.ENGLISH;
    	   
    	   case "french" :
    		   return Language.FRENCH;
    	   
    	   case "spanish" :
    		   return Language.SPANISH;
    	   
    	   case "italian" :
    		   return Language.ITALIAN;
    	   
    	   case "portuguese" :
    		   return Language.PORTUGUESE;
    	   
    	   default : 
    	      return null;
    	}
    	
    	
    }

}