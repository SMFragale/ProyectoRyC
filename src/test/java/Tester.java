import org.junit.Test;

import java.io.IOException;

public class Tester {

    @Test
    public void probarConexionServidor() {

        try {
            System.out.println("Iniciando conexión");
            ServidorProxy proxy = new ServidorProxy(8095);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
