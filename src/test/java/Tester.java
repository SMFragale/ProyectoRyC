import org.junit.Test;
import rawhttp.core.EagerHttpResponse;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.client.TcpRawHttpClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Tester {

    @Test //Para ejecutar este caso de prueba se requiere que un navegador envíe una solicitud a la máquina local con puerto 8095
    public void probarConexion() {
        try {
            int puerto = 8095;
            ServerSocket servidor = new ServerSocket(puerto);
            Socket cliente;
            //Bloquea hasta recibir respuesta
            cliente = servidor.accept();
            assert cliente != null;
        } catch(Exception e) {
            assert false;
        }
    }

    @Test
    public void enviarMensaje() {
        try {
            RawHttp http = new RawHttp();
            RawHttpRequest req = http.parseRequest(
                    "GET / HTTP/1.\r\n" +
                            "Host: openjdk.java.net\r\n" +
                            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0\r\n" +
                            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                            "Accept-Language: en-US,en;q=0.5\r\n" +
                            "Accept-Encoding: gzip, deflate\r\n" +
                            "Connection: keep-alive\r\n" +
                            "Upgrade-Insecure-Requests: 1\r\n\r\n"
            );
            TcpRawHttpClient clienteRaw = new TcpRawHttpClient();
            EagerHttpResponse<?> respuesta = clienteRaw.send(req).eagerly();
            assert respuesta.getStatusCode() == 200;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void redirigirMensajeVirtual() {
        try {
            RawHttp http = new RawHttp();
            RawHttpRequest request = http.parseRequest(
                    "GET / HTTP/1.1\r\n" +
                            "Host: e.ruiz\r\n" +
                            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0\r\n" +
                            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                            "Accept-Language: en-US,en;q=0.5\r\n" +
                            "Accept-Encoding: gzip, deflate\r\n" +
                            "Connection: keep-alive\r\n" +
                            "Upgrade-Insecure-Requests: 1\r\n" +
                            "\r\n\r\n"
            );
            HashMap<String, Host> virtuales = new ManejoArchivos().leerTablaHV();
            assert virtuales.containsKey(request.getUri().getHost());
            request = new ServidorProxy(8095).modificarSolicitud(request.getUri().getHost(), request);
            assert request != null;

        } catch (Exception e) {
            assert false;
        }
    }
}
