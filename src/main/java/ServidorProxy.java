import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * El servidor proxy se encarga de recibir las solicitudes HTTP de los clientes del proxy (AKA el navegador).
 * Una vez recibida la solicitud, el servidor la revisa y hace los cambios necesarios para ser enviados al ClienteProxy.
 * El ClienteProxy simula ser el cliente de la página que se quiere visitar y hace las operaciones en vez del cliente
 * real. El servidor recibe la respuesta del ClienteProxy y la envía al navegador.
 */
public class ServidorProxy {

    /**
     * El socketServidor recibe las solicitudes del navegador, las modifica si es necesario y las
     * envía al socketCliente.
     */
    private ServerSocket socketServidor;

    /**
     * El socketCliente recibe las solicitudes del servidor y las envía al servidor web al que
     * se le hace la solicitud.
     */
    private Socket socketCliente;

    /**
     * La tabla virtuales contiene en su llave el nombre de un host virtual y en su valor el host
     * real. Esta tabla se carga desde un archivo de configuración llamado virtuales.txt
     */
    private Map<String, Host> virtuales;

    private int puerto;

    public ServidorProxy(int puerto) throws IOException {
        this.puerto = puerto;
        socketServidor = new ServerSocket(puerto);
        socketCliente = socketServidor.accept();
        PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        String s;
        while((s = entrada.readLine()) != null) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        System.out.println("El servidor está iniciando");
        //TODO El servidor debe mostrar en tiempo real las solicitudes

    }

    /*
    * TODO El servidor debe mantener un log con las solicitudes en un archivo de texto.
    *  Este log debe tener los campos más importantes del mensaje y el tratamiento que se le da.
    *   por ejemplo si la solicitud corresponde a un “sitio web virtual” se debe indicar el URL al que se reenviará.
    */

    /*
    * TODO Manejar tipo de solicitud GET.
    *  El servidor solo se preocupa por el atributo Host que especifica el servidor web al que hay que conectarse y
    *  enviar la solicitud desde el cliente.
    *  En caso de ser un sitio web real, se reenvía la solicitud sin ningún problema.
    *  En caso de ser un sitio web "virtual"
    */

    /*
    * TODO Manejar tipo de solicitud POST.
    *  Se utilizan por enviar datos desde el browser hacia el servidor, como formas por ejemplo
    *  Hay varias líneas que indican encabezados en la solicitud luego una línea en blanco y finalmente
    *  los datos que el usuario ingresó en la forma.
    *  Estos datos deben ser enviados al servidor especificado
    *  Al igual que con la solicitud GET el servidor proxy web debe analizar el atributo Host para
    *  determinar el servidor web al que hay que conectarse y reenviar la solicitud. Adicionalmente, en
    *  el caso de una solicitud POST, se debe analizar el atributo Content-length que especifica el
    *  tamaño de los datos (en bytes) que conforman el cuerpo de la solicitud, esto le permite al proxy
    *  saber donde termina la solicitud.
    */


    /*
    * TODO Responder al cliente. La respuesta del servidor se reenvía al cliente
    *  sin ninguna modificación. El único encabezado de la respuesta a analizar
    *  es el atributo Content-Length, que especifica el tamaño en bytes que conforma
    *  el tamaño de la respuesta. Una vez leídos los encabezados y datos, el proxy pueden
    *  cerrar el socket con el servidor web y con el browser.
    */

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
