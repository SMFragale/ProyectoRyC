
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ManejoArchivos {

    String pathArchivoHV = "./virtuales.txt";

    public ManejoArchivos()
    {

    }

    public String getPathArchivoHV() {
        return pathArchivoHV;
    }

    public void setPathArchivoHV(String pathArchivoHV) {
        this.pathArchivoHV = pathArchivoHV;
    }

    public HashMap<String, Host> leerTablaHV()
    {
        HashMap<String,Host> mapHV = new HashMap<>();
        File tablaHV = new File(pathArchivoHV);

        if(tablaHV.exists())
        {
            try{
                //Se lee el archivo
                FileReader fr = new FileReader (tablaHV);
                BufferedReader br = new BufferedReader(fr);
                String linea;
                while((linea = br.readLine())!=null)
                {
                    if(linea != "")
                    {
                        String[] info = linea.split(",");
                        mapHV.put(info[0], new Host(info[1], info[2]));
                    }
                }

            }
            catch(IOException e)
            {
                System.out.println("Error en la lectura del archivo: " + pathArchivoHV);
                e.printStackTrace();
            }

        }
        else
        {
            //Mensaje de error --> El archivo de donde se quiere cargar la tablaHV no existe
            System.out.println("El archivo: " + pathArchivoHV + " no existe. No se cargó ningún valor a la tabla de Virtual Hosts");
        }

        return mapHV;

    }

    public void guardarTablaHV(HashMap<String, Host> mapHV)
    {
        Set<String> keys = mapHV.keySet();
        try {
            FileWriter writer = new FileWriter(pathArchivoHV);
            for(String o : keys)
            {
                String hostV = o;
                Host host = mapHV.get(hostV);
                writer.write(hostV +","+ host.getHostReal() +","+ host.getDirectorioRaiz() + "\n");
            }
            writer.close();
            System.out.println("Tabla de Hosts Virtuales guardada con écito");
        } catch (IOException e) {
            System.out.println("Ocurrió un error en la escritura del archvio: " + pathArchivoHV);
            e.printStackTrace();
        }

    }


}
