package Practice.LLD.DesignPatterns.LoggerDesignChainResponsibiltyPattern;

public class SevereLogger extends Logger {
    public SevereLogger(Logger nextLogger) {
        super(nextLogger);
    }

    public void log(LogType type, String msg) {
        if(type == LogType.SEVERE) {
            System.out.println("Severe: " + msg);
        } else {
            super.log(type, msg);
        }
    }
}
