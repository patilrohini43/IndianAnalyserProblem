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

public abstract class CensusAdapter {

    public abstract Map<String,CensusDao> loadCensusData(String... censusCsvFilePath) throws CensusAnalyserException;

    <E>  Map<String, CensusDao> loadCensusData(Class<?> censusCSVClass, String csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDao> censusCSVMap = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            Iterator<E> censusCSVIterator = csvBuilder.getCSVFilterIterator(reader, censusCSVClass);
            Iterable<E> csvIterable = () -> censusCSVIterator;
            if(censusCSVClass.getName().equals("Model.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(census -> censusCSVMap.put(census.state, new CensusDao(census)));
            }
            else if(censusCSVClass.getName().equals("Model.UsCensusCSV"))
            {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(UsCensusCSV.class::cast)
                        .forEach(census -> censusCSVMap.put(census.state, new CensusDao(census)));
            }
            return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
    }
}
