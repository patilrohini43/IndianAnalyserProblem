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

    List<IndiaCsvDao> censusCSVList = null;
    Map<String, IndiaCsvDao> censusCSVMap = new HashMap<>();
    Map<SortField, Comparator<IndiaCsvDao>> sortMap = null;

    public StateCensusAnalyser() {
        this.sortMap = new HashMap<>();
        this.sortMap.put(SortField.STATE, Comparator.comparing(census -> census.state));
        this.sortMap.put(SortField.population, Comparator.comparing(census -> census.population));
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
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFilterIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable = () -> censusCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false).
                    forEach(census -> censusCSVMap.put(census.state, new IndiaCsvDao(census)));
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
            return this.getCountCSVFile(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
    }

    public <T> int getCountCSVFile(Iterator<T> censusCSVIterator) {
        int namOfEateries = 0;
        while (censusCSVIterator.hasNext()) {
            namOfEateries++;
        }
        return namOfEateries;
    }

    public String getAlphabeticalOrderData(SortField field) throws CensusAnalyserException {
        if (censusCSVMap == null) {
            throw new CensusAnalyserException("No Data Found", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
        // Comparator<IndiaCsvDao> censusCSVComparator = Comparator.comparing(census -> census.state);
        List<IndiaCsvDao> indiaCsvDaos = censusCSVMap.values().stream().collect(Collectors.toList());
        this.sort(indiaCsvDaos, this.sortMap.get(field).reversed());
        this.writeInGson(indiaCsvDaos);
        return new Gson().toJson(indiaCsvDaos);
    }


        private static void writeInGson(List<IndiaCsvDao> indiaCsvDao) throws CensusAnalyserException {
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

    private void sort(List<IndiaCsvDao> indiaCsvDaos, Comparator<IndiaCsvDao> censusCSVComparator) {
        IndiaCsvDao temp;
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
}