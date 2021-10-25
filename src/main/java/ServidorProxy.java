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
    private final Map<String, Host> virtuales;

    /**
     * Hace referencia al puerto por el cual se establece la comunicación con el navegador o cliente.
     */
    private final int puerto;

    /**
     * Este objeto es un auxiliar de la API RawHTTP que contiene varios métodos útiles para el manejo de HTTP.
     * Entre ellos está el método parseHttp() que permite convertir un String en un mensaje HTTP encapsulado.
     */
    private final RawHttp http;

    public ServidorProxy(int puerto) {
        this.puerto = puerto;
        ManejoArchivos manager = new ManejoArchivos();
        virtuales = manager.leerTablaHV();
        http = new RawHttp();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("El servidor está iniciando");
        ServidorProxy proxy = new ServidorProxy(8095);

        proxy.escuchar();
    }

    /**
     * Escucha en el puerto establecido para recibir solicitudes. Al recibir una solicitud, la redirecciona al host
     * requerido.
     */
    public void escuchar() throws IOException {
        Logger logger = new Logger("./");
        ServerSocket servidor = new ServerSocket(this.puerto);
        Socket cliente;
        while(true) {
            cliente = servidor.accept();
            try {
                RawHttpRequest request = http.parseRequest(cliente.getInputStream()).eagerly();

                String linea1 = request.getStartLine().toString();
                String host = request.getUri().getHost();
                if(linea1.startsWith("GET")) {
                    logger.log(request.toString(), Logger.Tipo.SR);
                    if(virtuales.containsKey(host)) {
                        request = modificarSolicitud(host, request);
                    }
                }
                else if(linea1.startsWith("POST")) {
                    logger.log(request.toString(), Logger.Tipo.SR);
                    if(virtuales.containsKey(host)) {
                        modificarSolicitud(host, request);
                    }
                }
                else if(linea1.startsWith("CONNECT")){
                    logger.log(request.toString(), Logger.Tipo.SR);
                    continue;
                }
                else {
                    logger.log("Solicitud no soportada: "+request, Logger.Tipo.WARNING);
                    continue;
                }
                TcpRawHttpClient clienteRaw = new TcpRawHttpClient();
                EagerHttpResponse<?> respuesta = clienteRaw.send(request).eagerly();
                respuesta.writeTo(cliente.getOutputStream());
            } catch (Exception e) {
                logger.log(e.getMessage(), Logger.Tipo.ERROR);
                e.printStackTrace();
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


}
