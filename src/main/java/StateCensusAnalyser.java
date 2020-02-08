import Model.IndiaCensusCSV;
import Model.UsCensusCSV;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {

    List<CensusDao> censusCSVList = null;
    Map<String, CensusDao> censusCSVMap = new HashMap<>();
    Map<SortField, Comparator<CensusDao>> sortMap = null;

    public StateCensusAnalyser() {
        this.sortMap = new HashMap<>();
        this.sortMap.put(SortField.STATE, Comparator.comparing(census -> census.state));
        this.sortMap.put(SortField.population, Comparator.comparing(census -> census.population));
        this.sortMap.put(SortField.areaInSqKm, Comparator.comparing(census -> census.totalArea));
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            censusCSVList = csvBuilder.getCSVFilterList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.HEADERNOTFOUND);
        }
    }

    public int loadIndiaStateCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath,IndiaCensusCSV.class);
    }

    public int loadUsCsvCensusData(String usCsvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(usCsvFilePath,UsCensusCSV.class);
    }

    private <E> int loadCensusData(String csvFilePath, Class<?> censusCSVClass) throws CensusAnalyserException {
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
            return censusCSVMap.size();
            } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
            }
    }

    public int loadIndiaStateCSVCensusData(String csvFilePath) throws CensusAnalyserException {
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

    public String getAlphabeticalOrderData(SortField field) throws CensusAnalyserException {
        if (censusCSVMap == null) {
            throw new CensusAnalyserException("No Data Found", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
        // Comparator<IndiaCsvDao> censusCSVComparator = Comparator.comparing(census -> census.state);
        List<CensusDao> indiaCsvDaos = censusCSVMap.values().stream().collect(Collectors.toList());
        this.sort(indiaCsvDaos, this.sortMap.get(field).reversed());
        this.writeInGson(indiaCsvDaos);
        return new Gson().toJson(indiaCsvDaos);
    }

    private void sort(List<CensusDao> indiaCsvDaos, Comparator<CensusDao> censusCSVComparator) {
        CensusDao temp;
        System.out.println(censusCSVComparator);
        for (int i = 0; i < indiaCsvDaos.size(); i++) {
            for (int j = i + 1; j < indiaCsvDaos.size(); j++) {
                if (censusCSVComparator.compare(indiaCsvDaos.get(i), indiaCsvDaos.get(j)) > 0) {
                    temp = indiaCsvDaos.get(i);
                    indiaCsvDaos.set(i, indiaCsvDaos.get(j));
                    indiaCsvDaos.set(j, temp);
                }
            }
        }
    }

    private static void writeInGson(List<CensusDao> indiaCsvDao) throws CensusAnalyserException {
        Gson gson = new Gson();
        String json = gson.toJson(indiaCsvDao);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("././src/test/resources/SampleCsv.json");
            fileWriter.write(json);
            fileWriter.close();
        }catch (IOException e)
        {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
    }
}