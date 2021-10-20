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
    public enum tipo {
        GET,
        POST
    }
    /**
    @param pathArchivo path y nombre del archivo en el que se guardará el log. No debe tener la extensión .txt.
     **/
    public Logger(String pathArchivo)
    {
        this.calendar = Calendar.getInstance();
        this.date = calendar.get(Calendar.YEAR) + "-" +(calendar.get(Calendar.MONTH)+1) + "-" +calendar.get(Calendar.DAY_OF_MONTH);
        if(pathArchivo.endsWith(".txt"))
        {
            pathArchivo = pathArchivo.substring(0, pathArchivo.length()-4);
        }
        this.pathLogger =  pathArchivo +"_"+ date + ".txt";
    }

    public void log(String l, tipo lt)
    {
        String finalLog = "Log tipo "+ lt.name() + ": " + calendar.getTime()+ ": " + l;
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
