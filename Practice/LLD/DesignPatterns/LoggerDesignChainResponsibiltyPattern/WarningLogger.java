package Practice.LLD.DesignPatterns.LoggerDesignChainResponsibiltyPattern;

public class WarningLogger extends Logger {

    public WarningLogger(Logger nextLogger) {
        super(nextLogger);
    }
    
    public void log(LogType type, String msg) {
        if(type == LogType.WARNING) {
            System.out.println("Warning: " + msg);
        } else {
            super.log(type, msg);
        }
    }
}
