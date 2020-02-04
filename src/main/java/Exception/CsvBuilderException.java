package Exception;

public class CsvBuilderException extends Exception {


    enum ExceptionType {
        CENSUS_FILE_PROBLEM,TYPE_NOTFOUND,HEADERNOTFOUND;
    }

    ExceptionType type;

    public CsvBuilderException(String message,ExceptionType type) {
        super(message);
        this.type = type;
    }
}
