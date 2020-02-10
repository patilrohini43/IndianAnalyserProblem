import Model.IndiaCensusCSV;
import Model.UsCensusCSV;

import java.util.Map;

public class UsCensusAdapter extends CensusAdater{

    @Override
    public Map<String, CensusDao> loadCensusData(String... censusCsvFilePath) throws CensusAnalyserException {
        Map<String,CensusDao> censusDaoMap=super.loadCensusData(UsCensusCSV.class,censusCsvFilePath[0]);
        return censusDaoMap;
    }
}
