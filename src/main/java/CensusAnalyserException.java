public class CensusAnalyserException extends Exception
{
    enum ExceptionType {
        CENSUS_FILE_PROBLEM,TYPE_NOTFOUND,HEADERNOTFOUND
    }

    ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
