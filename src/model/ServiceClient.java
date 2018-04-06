package model;

import java.io.IOException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.util.concurrent.TimeUnit;

public class ServiceClient {

	public String invokeService(AtributoModel am, String serie, String folio){
		ResponseBody rb = null; 
		String  rt = "";
	try{
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{ \"serieCFD\": \""+am.getSerieCFD()+"\", \"folioCFD\": \""+am.getFolioCFD()+"\"}");
//		RequestBody body = RequestBody.create(mediaType, "{ \"serie\": \""+am.getSerieCFD()+"\", \"folio\": \""+am.getFolioCFD()+"\"}");

		Request request = new Request.Builder()
				//Datos de Consulta para Desarrollo
		.url("https://iwc.gnp.com.mx/consulta/serie/"+serie+"/folio/"+folio)
//		.url("https://iwc.gnp.com.mx/consulta")
		.post(body)
		.addHeader("x-api-key", "coteal-a05c976f-11ea-4b17-be51-ab7a2d8bba23")
		//.addHeader("content-type", "application/json")
		.addHeader("cache-control", "no-cache")
		.build();	
		
		client.setConnectTimeout(30000, TimeUnit.MILLISECONDS);
		client.setReadTimeout(30000, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(30000, TimeUnit.MILLISECONDS);
		
		Response response = client.newCall(request).execute();
		System.out.println(request.toString());
		rb = response.body();
		rt  = rb.string();
		//System.out.println(rt); //se imprime el valor del json
		rb.close();
		}catch (Exception e) {			
			System.out.println(e.getMessage());			
				if(rb!=null) {
					try {
						rb.close();
					} catch (IOException e1) {						
						System.out.println(e1.getMessage());
						return "";
					}
				return "";
				}
		}	
	
	return rt;	
	}
}

