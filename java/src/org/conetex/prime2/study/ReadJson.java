package org.conetex.prime2.study;

import java.io.StringReader;
import java.util.Map.Entry;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class ReadJson {

	public static void main(String[] args)  {

		String json = 
		"{                                                                                                  " +
		//"  \"data\" : [                                                                                     " +
		"    \"blogURL\": \"http://crunchify.com\",                                                         " + 
		"    \"twitter\": \"http://twitter.com/Crunchify\",                                                 " +
		"    \"social\": {                                                                                  " +
		"        \"facebook\": \"http://facebook.com/Crunchify\",                                           " +
		"        \"pinterest\": \"https://www.pinterest.com/Crunchify/crunchify-articles\",                 " +
		"        \"rss\": \"http://feeds.feedburner.com/Crunchify\"                                         " +
		"    }                                                                                              " +
		//"  ]                                                                                                " +
		"}                                                                                                  "
		;
		JsonReader rdr = Json.createReader( new StringReader(json) );
		JsonObject obj = rdr.readObject();
		
		//System.out.println(obj.getJsonObject("social"));
		for (JsonValue v : obj.values()) {
			System.out.println( v.toString() );
		}
		
		for (Entry<String, JsonValue> e : obj.entrySet()) {
			System.out.println( e.getKey() + " === " + e.getValue().getValueType() );
		}

		/*
			JsonReader rdr = Json.createReader( new StringReader(json) );
			JsonObject obj = rdr.readObject();
			JsonArray results = obj.getJsonArray("data");
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				System.out.print(result.getJsonObject("social").getString("facebook"));
				System.out.print(": ");
				System.out.println(result.getString("rss", ""));
				System.out.println("-----------");
			}
		*/		

	}

}
