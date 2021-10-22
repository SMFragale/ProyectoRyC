import rawhttp.core.*;
import rawhttp.core.client.TcpRawHttpClient;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * El servidor proxy se encarga de recibir las solicitudes HTTP de los clientes del proxy (AKA el navegador).
 * Una vez recibida la solicitud, el servidor la revisa y hace los cambios necesarios para ser enviados al ClienteProxy.
 * El ClienteProxy simula ser el cliente de la página que se quiere visitar y hace las operaciones en vez del cliente
 * real. El servidor recibe la respuesta del ClienteProxy y la envía al navegador.
 */
public class ServidorProxy {

    /**
     * La tabla virtuales contiene en su llave el nombre de un host virtual y en su valor el host
     * real. Esta tabla se carga desde un archivo de configuración llamado virtuales.txt
     */
    private Map<String, Host> virtuales;

    private final int puerto;

    private final RawHttp http;

    public ServidorProxy(int puerto) throws IOException {
        this.puerto = puerto;
        ManejoArchivos manager = new ManejoArchivos();
        virtuales = manager.leerTablaHV();
        http = new RawHttp();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("El servidor está iniciando");
        //TODO El servidor debe mostrar en tiempo real las solicitudes
        ServidorProxy proxy = new ServidorProxy(8095);

        proxy.escuchar();
    }

    /*
    * TODO El servidor debe mantener un log con las solicitudes en un archivo de texto.
    *  Este log debe tener los campos más importantes del mensaje y el tratamiento que se le da.
    *   por ejemplo si la solicitud corresponde a un “sitio web virtual” se debe indicar el URL al que se reenviará.
    */

    /**
     * Escucha en el puerto establecido para recibir solicitudes. Al recibir una solicitud, la redirecciona al host
     * requerido.
     */
    public void escuchar() throws IOException {
        ServerSocket servidor = new ServerSocket(this.puerto);
        ServerSocket servidorProxy = new ServerSocket(this.puerto+1);
        Socket cliente;
        while(true) {
            cliente = servidor.accept();
            try {
                RawHttpRequest request = http.parseRequest(cliente.getInputStream()).eagerly();

                String linea1 = request.getStartLine().toString();
                String host = request.getUri().getHost();
                if(linea1.startsWith("GET")) {
                    System.out.println("Se recibió una solicitud GET: " + linea1);
                    if(virtuales.containsKey(host)) {
                        request = modificarSolicitud(host, request);
                    }
                }
                else if(linea1.startsWith("POST")) {
                    System.out.println("Se recibió una solicitud POST");
                    if(virtuales.containsKey(host)) {
                        modificarSolicitud(host, request);
                    }
                }
                else if(linea1.startsWith("CONNECT")){
                    System.out.println("Se recibió una solicitud CONNECT: " + linea1);
                    continue;
                }
                else {
                    System.out.println("Se recibió una solicitud no soportada: " + linea1);
                    continue;
                }
                TcpRawHttpClient clienteRaw = new TcpRawHttpClient();
                EagerHttpResponse<?> respuesta = clienteRaw.send(request).eagerly();
                respuesta.writeTo(cliente.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            } finally {
                cliente.close();
            }
        }
    }

    /**
     * En el caso que el host de una solicitud se encuentre en la tabla de sitios web virtuales, la solicitud es
     * modificada para apuntar hacia el host real que debe recibir la solicitud.
     */
    public RawHttpRequest modificarSolicitud(String host, RawHttpRequest request) {
        Host nuevoHost = virtuales.get(host); //TODO Modificar el host
        String hostModificado = nuevoHost.getHostReal() + "/" + nuevoHost.getDirectorioRaiz();
        String req = request.toString();
        String[] headers = req.split("\r");
        for(int i = 0; i < headers.length; i++) {
            String linea = headers[i];
            if(linea.startsWith("\nHost") || linea.startsWith("Host")) {
                headers[i] = linea.replace(host, nuevoHost.getHostReal());
                String[] sentenciaGet = headers[0].split(" ");
                break;
            }
        }
        req = String.join("\r", headers);
        request = http.parseRequest(req);
        return request;
    }

    /*
    * TODO Manejo de sitios web virtuales. Se necesita un archivo de configuración
    *  en el que se asocie un nombre de host con el host real donde se encuentra y el
    *  directorio debase de ese host.
    *  Cuando se recibe una solicitud (GET o POST) se debe verificar el host solicitado (especificado en
    *  el encabezado Host) y compararlo con los nombres de host virtuales configurados.
    *  Si el host especificado es un host virtual se deben hacer las siguientes modificaciones a la
    *  solicitud antes de enviarla:
    *    1. Modificar el URL solicitado: Se debe cambiar la primera línea de la solicitud cambiando
    *    el host del URL por el host real y el directorio raíz que se configuraron.
    *    2. Modificar el encabezado Host: Se debe cambiar la línea correspondiente al encabezado
    *    Host por una que contenga el nombre del host real.
    */

}
