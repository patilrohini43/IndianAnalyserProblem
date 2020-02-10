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

public class CensusLoader {

    public Map<String,CensusDao> loadCensusData(StateCensusAnalyser.Country country, String... censusCsvFilePath) throws CensusAnalyserException {
        if(country.equals(StateCensusAnalyser.Country.India))
        {
           return this.loadCensusData(IndiaCensusCSV.class,censusCsvFilePath);
        }
        else if(country.equals(StateCensusAnalyser.Country.US))
        {
            return this.loadCensusData(UsCensusCSV.class,censusCsvFilePath);
        }
        else throw new CensusAnalyserException("Invalid Country", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
    }

    private   <E>  Map<String, CensusDao> loadCensusData(Class<?> censusCSVClass, String... csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDao> censusCSVMap = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));
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
           if(csvFilePath.length == 1) return censusCSVMap;
           this.loadIndiaStateCSVCensusData(censusCSVMap,csvFilePath[1]);
           return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
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
