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
	static String TSExpedicionCFD = null;
	static String TSTimbrado = null;
	static String rfcReceptor = null;
	static String status = null;
	static String timestamp = null;
	static String uuid = null;
	static boolean continuar = true;
	static String linea = null;
	static String respuesta = null;
	static JsonParser parseador = new JsonParser();
	static JsonElement datos = null;
	
	 
	public static String procesaJson(String json) {
		continuar = true;

		try {
			if(!json.contains("404 Not Found")) {
				datos = parseador.parse(json);
				procesoElementoJson(datos);
			}else {
				System.out.println("[ERROR] No se encontró el registro");
			}
		} catch (com.google.gson.JsonSyntaxException e) {
			System.out.println("Error de lectura de Json");
		} catch (com.google.gson.stream.MalformedJsonException e) {
			e.printStackTrace();
		}

		return linea;
	}

	private static void procesoElementoJson(JsonElement elemento) throws com.google.gson.stream.MalformedJsonException {
		if (elemento.isJsonObject() && elemento != null && continuar) {
			// System.out.println("Es objeto");
			JsonObject obj = elemento.getAsJsonObject();
			java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
			java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();

			while (iter.hasNext()) {
				java.util.Map.Entry<String, JsonElement> entrada = iter.next();

				serieCFD = ((entrada.getKey().equals("serieCFD") && serieCFD == null) ? entrada.getValue().toString()
						: serieCFD);
				folioCFD = ((entrada.getKey().equals("folioCFD") && folioCFD == null) ? entrada.getValue().toString()
						: folioCFD);
				timestamp = ((entrada.getKey().equals("timestamp") && timestamp == null) ? entrada.getValue().toString()
						: timestamp);
				TSExpedicionCFD = ((entrada.getKey().equals("TSExpedicionCFD") && TSExpedicionCFD == null)
						? entrada.getValue().toString() : TSExpedicionCFD);
				status = ((entrada.getKey().equals("status") && status == null) ? entrada.getValue().toString()
						: status);
				TSTimbrado = ((entrada.getKey().equals("TSTimbrado") && TSTimbrado == null)
						? entrada.getValue().toString() : TSTimbrado);
				uuid = ((entrada.getKey().equals("uuid") && uuid == null) ? entrada.getValue().toString() : uuid);
				rfcReceptor = ((entrada.getKey().equals("rfcReceptor") && rfcReceptor == null)
						? entrada.getValue().toString() : rfcReceptor);

				if (serieCFD != null && folioCFD != null && timestamp != null && TSExpedicionCFD != null
						&& status != null && TSTimbrado != null && uuid != null && rfcReceptor != null && continuar) {

					linea = serieCFD.subSequence(1, serieCFD.length() - 1) + "|"
							+ folioCFD.substring(1, folioCFD.length() - 1) + "|"
							+ TSExpedicionCFD.substring(1, TSExpedicionCFD.length() - 1) + "|"
							+ TSTimbrado.substring(1, TSTimbrado.length() - 1) + "|"
							+ rfcReceptor.substring(1, rfcReceptor.length() - 1) + "|"
							+ status.substring(1, status.length() - 1) + "|"
							+ timestamp.substring(1, timestamp.length() - 1) + "|"
							+ uuid.substring(1, uuid.length() - 1);

					continuar = false;
					elemento = null;
					serieCFD = null;
					folioCFD = null;
					TSExpedicionCFD = null;
					TSTimbrado = null;
					rfcReceptor = null;
					status = null;
					timestamp = null;
					uuid = null;
					break;

				}

				if (continuar) {
					procesoElementoJson(entrada.getValue());
				}
				// System.out.println("Bandera primer ciclo "+bandera);
			}

		}
		if (elemento != null && elemento.isJsonArray() && continuar) {
			JsonArray array = elemento.getAsJsonArray();
			// System.out.println("Es array. Numero de elementos: " + array.size());
			java.util.Iterator<JsonElement> iter = array.iterator();
			while (iter.hasNext()) {
				JsonElement entrada = iter.next();
				procesoElementoJson(entrada);
			}
		}

		// System.out.println("-------------"+ linea+continuar);
	}

}
