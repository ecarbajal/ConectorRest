package model;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import org.apache.commons.net.ftp.*;



public class TestClient {
	private String archivoSalida="\\Informacion-Fiscal\\Autos\\FacmanUUID\\Cifras_UUID_C.txt";
	//	private String archivoSalida="\\Informacion-Fiscal\\Auto\\FacmanUUID\\Cifras_UUID pruebas.txt";

	private String archivoEntrada = "C:\\ConectorREST\\data_cad_corta\\entrada.txt";
	//private String archivoPaso = "C:\\ConectorREST\\data\\Cifras_UUID pruebas.txt";
	private String archivoPaso = "C:\\ConectorREST\\data_cad_corta\\Cifras_UUID.txt";
	private String server="150.23.1.10";
	private String userServ="ftprseguro";
	private String passServ="frseguro";
	private String host="150.23.1.13";
	private String userHost="tv6jcg";
	private String passHost="mexico15";
	//	private String userHost="TV6RMQ";
	//	private String passHost="KMC03KMC";
    private String remoteFile1 = "\'DNO.EOCGWN01'"; // DESARROLLO 
//	private String remoteFile1 = "\'PGA.EARCX801'"; // PRODUCCION
	private static FTPClient cliente = new FTPClient();


	public static void main(String[] args) throws IOException {
		TestClient tc = new TestClient();
		tc.Consulta();		
	}

	private void Consulta() throws IOException{
		String serie="", folio="",cadena="", respuesta="";
		int cont=0;
		boolean validaCon = descarga(host, userHost, passHost, archivoEntrada, remoteFile1);
//				boolean validaCon = true;
		if(validaCon) {
			FileReader f = new FileReader(archivoEntrada);
			BufferedReader b = new BufferedReader(f);
			//descarga(server, userServ, passServ, archivoPaso,archivoSalida);
			while ((cadena = b.readLine()) != null) {
				// System.out.println(cadena);
				serie = cadena.substring(39, 43).trim();
				folio = cadena.substring(49, cadena.length()).trim();
				AtributoModel am = new AtributoModel(serie, folio);
				ServiceClient sc = new ServiceClient();
				respuesta = sc.invokeService(am, serie, folio);

				CargarDatos(respuesta);

				cont++;
				System.out.println("Registro: " + cont);
			}
			System.out.println("Archivo generado.");
			boolean SubeArch = subirFichero(server,userServ, passServ,archivoPaso,archivoSalida);
//						boolean SubeArch = true;
			if (SubeArch) {
				//if (true) {
				System.out.println("Archivo depositado en Servidor.");
				/*File f1 = new File(salida);
					f1.delete();*/
				/*} else {
					System.out.println("No se pudo subir Archivo al Servidor");
				}*/

				b.close();
			} else {
				System.out.println("No se pudo subir Archivo al Servidor");
				System.out.println("No se pudo descargar el Archivo de Entrada.");
			}
		}
	}

	private boolean descarga(String server, String user, String pass, String archivo,String remoteFile1){
		boolean ind=false;
		FTPClient cliente = new FTPClient();		
		try {
			cliente.connect(server);
			int reply = cliente.getReplyCode();
			if (reply == 220) {
				if(cliente.login(user,pass)){
					System.out.println("Inicio de sesión correcto");
					File downloadFile1 = new File(archivo);
					BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));				
					ind = cliente.retrieveFile(remoteFile1, outputStream1);
					//					cliente.deleteFile(remoteFile1);
					outputStream1.close();
					//ind=true;

					System.out.println(ind ? "Descarga completa" : "No descargo");

				}else {
					System.out.println("Usuario o contraseña incorrectos.");
				}
			}else {
				System.out.println("Error conexión");
			}

		} catch (SocketException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return ind;
	}

	public static boolean subirFichero(String server, String userServ,String passServ, String pathFich, String fich) throws SocketException, IOException{
		InputStream is;
		boolean fichSubido = false;
		boolean statusLogin = false;
		try {
			cliente.connect(server);
			statusLogin = cliente.login(userServ, passServ);
		}catch(NoRouteToHostException nrthe) {
			System.out.println("Error al conectar al servidor.");
		}catch(NullPointerException npe) {
			System.out.println("Error al iniciar sesion.");
		}

		if(statusLogin){
			cliente.enterLocalPassiveMode();
			cliente.setFileType(FTP.BINARY_FILE_TYPE);

			int respuesta = cliente.getReplyCode();
			if (FTPReply.isPositiveCompletion(respuesta) == true) {
				is = new BufferedInputStream(new FileInputStream(pathFich));
				fichSubido = cliente.storeFile(fich, is); 
				//				cliente.deleteFile(fich);
				fichSubido = cliente.appendFile(fich,is);
				is.close();
				return fichSubido;
			}else{
				return fichSubido;
			}
		}else{
			System.out.println("Usuario o contraseña incorrectos.");
			return false;
		}
	}


	private void CargarDatos(String lineaXML) {
		try {
			InputStream initialStream = new FileInputStream(new File(archivoPaso));
			BufferedReader reader = new BufferedReader(new InputStreamReader(initialStream));

			FileWriter fstream = new FileWriter(archivoPaso, true);
			BufferedWriter out = new BufferedWriter(fstream);

			String respuesta = ProcesarJSON.procesaJson(lineaXML);

			if(respuesta != null) {
				out.write(respuesta+"\r\n");
			}

			reader.close();
			initialStream.close();
			out.close();

		} catch (IOException ex) {
			System.out.println("Error: "+ex.getMessage());
		}
	}


}
