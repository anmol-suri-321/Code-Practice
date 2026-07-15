package Practice.LLD.DesignPatterns.LoggerDesignChainResponsibiltyPattern;

public abstract class Logger {
    Logger nextLogger;

    public Logger(Logger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void log(LogType type, String msg) {
        if(nextLogger != null) {
            nextLogger.log(type, msg);
        }
    }

    
}
