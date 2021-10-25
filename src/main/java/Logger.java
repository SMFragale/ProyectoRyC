import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class Logger {

    Calendar calendar;
    String pathLogger;
    String date;
    public enum Tipo {
        SR,
        WARNING,
        ERROR
    }
    /**
    @param pathArchivo path y nombre del archivo en el que se guardará el log. No debe tener la extensión .txt.
     **/
    public Logger(String pathArchivo)
    {
        this.calendar = Calendar.getInstance();
        this.date = calendar.get(Calendar.DAY_OF_MONTH) + "-" +(calendar.get(Calendar.MONTH)+1) + "-" +calendar.get(Calendar.YEAR);
        if(pathArchivo.endsWith(".txt"))
        {
            pathArchivo = pathArchivo.substring(0, pathArchivo.length()-4);
        }
        this.pathLogger =  pathArchivo +"Logs_"+ date + ".txt";
    }

    public void log(String linea, Tipo tipo)
    {
        calendar = Calendar.getInstance();
        String finalLog = (tipo.name() == "SR" ? "SOLICITUD RECIBIDA" : tipo.name()) + ": " + calendar.getTime()+ ":\n" + linea;
        try{
            File logFile = new File(pathLogger);
            PrintWriter out = new PrintWriter(new FileWriter(logFile, true));
            out.append(finalLog+"\n");
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("Error al escribir un log en el archivo: " + pathLogger);
            e.printStackTrace();
        }
        System.out.println(finalLog);
    }

    public String getPathLogger() {
        return pathLogger;
    }

    public void setPathLogger(String pathLogger) {
        this.pathLogger = pathLogger;
    }
}
