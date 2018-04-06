package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Eduardo Carbajal Reyes
 *
 */

public class ProcesarJSON {

	 static String serieCFD = null;
	static String folioCFD = null;
	static String timestamp = null;
	static String TSExpedicionCFD = null;
	static String status = null;
	static String TSTimbrado = null;
	static String uuid = null;
	static String linea = null;
	 
	public static String procesaJson(String json) {
		
		JsonParser parser = new JsonParser();
		
		JsonElement datos = parser.parse(json);

		return procesoElementoJson(datos);
	}

	private static String procesoElementoJson(JsonElement elemento) {
		 if (elemento.isJsonObject()) {
		        System.out.println("Es objeto");
		        JsonObject obj = elemento.getAsJsonObject();
		        java.util.Set<java.util.Map.Entry<String,JsonElement>> entradas = obj.entrySet();
		        java.util.Iterator<java.util.Map.Entry<String,JsonElement>> iter = entradas.iterator();
		        
		       
		        
		        while (iter.hasNext()) {
		            java.util.Map.Entry<String,JsonElement> entrada = iter.next();
		            
		            serieCFD = ((entrada.getKey().equals("serieCFD") && serieCFD == null) ? entrada.getValue().toString() : serieCFD);
		            folioCFD = ((entrada.getKey().equals("folioCFD") && folioCFD == null) ? entrada.getValue().toString() : folioCFD);
		            timestamp = ((entrada.getKey().equals("timestamp") && timestamp == null) ? entrada.getValue().toString() : timestamp);
		            TSExpedicionCFD = ((entrada.getKey().equals("TSExpedicionCFD") && TSExpedicionCFD == null) ? entrada.getValue().toString() : TSExpedicionCFD);
		            status = ((entrada.getKey().equals("status") && status == null) ? entrada.getValue().toString() : status);
		            TSTimbrado = ((entrada.getKey().equals("TSTimbrado") && TSTimbrado == null) ? entrada.getValue().toString() : TSTimbrado);
		            uuid = ((entrada.getKey().equals("uuid") && uuid == null) ? entrada.getValue().toString() : uuid);

		            if(serieCFD != null && folioCFD != null && timestamp != null && TSExpedicionCFD != null && status != null
		            		&& TSTimbrado != null && uuid != null) {

					linea = serieCFD.subSequence(1, serieCFD.length() - 1) + "|"
							+ folioCFD.substring(1, folioCFD.length() - 1) + "|" 
							+ timestamp.substring(1, timestamp.length() - 1) + "|" 
							+ TSExpedicionCFD.substring(1, TSExpedicionCFD.length() - 1) + "|" 
							+ status.substring(1, status.length() - 1) + "|" 
							+ TSTimbrado.substring(1, TSTimbrado.length() - 1) + "|" 
							+ uuid.substring(1, uuid.length() - 1);
		            
		       		 System.out.println(linea);
		            	break;
		            }
		            
		            procesoElementoJson(entrada.getValue());
		            
		           
		            
		            
		        }
		 
		    } else if (elemento.isJsonArray()) {
		        JsonArray array = elemento.getAsJsonArray();
		        System.out.println("Es array. Numero de elementos: " + array.size());
		        java.util.Iterator<JsonElement> iter = array.iterator();
		        while (iter.hasNext()) {
		            JsonElement entrada = iter.next();
		            procesoElementoJson(entrada);
		        }
		    }

		return linea;
	}

}
