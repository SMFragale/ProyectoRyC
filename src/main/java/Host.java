/**
 * La clase Host contiene los datos del host real al cual se quiere
 * hacer una solicitud por un sitio web virtual.
 */
public class Host {

    public String hostReal;
    public String directorioRaiz;

    public Host(String hostReal, String directorioRaiz) {
        this.hostReal = hostReal;
        this.directorioRaiz = directorioRaiz;
    }

    public String getHostReal() {
        return hostReal;
    }

    public String getDirectorioRaiz() {
        return directorioRaiz;
    }
}
