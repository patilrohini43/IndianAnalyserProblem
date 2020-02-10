import Model.IndiaCensusCSV;
import Model.UsCensusCSV;

import java.util.Map;

public class CensusAdapterFactory {

    public static Map<String,CensusDao> getCensusData(StateCensusAnalyser.Country country, String... censusCsvFilePath) throws CensusAnalyserException {
        if(country.equals(StateCensusAnalyser.Country.India))
        {
            return new IndiaCensusAdapter().loadCensusData(censusCsvFilePath);
        }
        else if(country.equals(StateCensusAnalyser.Country.US))
        {
            return new UsCensusAdapter().loadCensusData(censusCsvFilePath);
        }
        else throw new CensusAnalyserException("Invalid Country", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
    }
}
