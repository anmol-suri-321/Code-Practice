package Practice.LLD.DesignPatterns.LoggerDesignChainResponsibiltyPattern;

public class InfoLogger extends Logger {

    public InfoLogger(Logger nextLogger) {
        super(nextLogger);
    }

    public void log(LogType type, String msg) {
        if(type == LogType.INFO) {
            System.out.println("Info: " + msg);
        } else {
            super.log(type, msg);
        }
    }
    
}
