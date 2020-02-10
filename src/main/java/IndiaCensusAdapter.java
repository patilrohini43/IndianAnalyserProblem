import Model.IndiaCensusCSV;
import Model.UsCensusCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class IndiaCensusAdapter  extends CensusAdater{


    @Override
    public Map<String, CensusDao> loadCensusData(String... censusCsvFilePath) throws CensusAnalyserException {
        Map<String,CensusDao> censusDaoMap=super.loadCensusData(IndiaCensusCSV.class,censusCsvFilePath[0]);
        this.loadIndiaStateCSVCensusData(censusDaoMap,censusCsvFilePath[1]);
        return censusDaoMap;
    }

    public int loadIndiaStateCSVCensusData(Map<String, CensusDao> censusCSVMap,String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            Iterator<CsvState> censusCSVIterator = csvBuilder.getCSVFilterIterator(reader, CsvState.class);
            Iterable<CsvState> csvStatesItearble = () -> censusCSVIterator;
            StreamSupport.stream(csvStatesItearble.spliterator(), false)
                    .filter(csvState -> censusCSVMap.get(csvState.state) != null )
                    .forEach(census -> censusCSVMap.get(census.state).stateCode = census.stateCode);
            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
    }
}
