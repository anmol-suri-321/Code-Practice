package Practice.LLD.DesignPatterns.LoggerDesignChainResponsibiltyPattern;

public class Main {
    public static void main(String args[]) {
        Logger logger = new InfoLogger(new WarningLogger(new SevereLogger(null)));
        logger.log(LogType.INFO, "This is a info log");
        logger.log(LogType.SEVERE, "This is a severe log");
        logger.log(LogType.WARNING, "This is a warning log");
    }
}
