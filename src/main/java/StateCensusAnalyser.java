import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {

    List<IndiaCsvDao> censusCSVList=null;
    Map<String,IndiaCsvDao> censusCSVMap=null;

    public  StateCensusAnalyser()
    {
        this.censusCSVMap=new HashMap<>();
    }


    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            censusCSVList = csvBuilder.getCSVFilterList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
        }
        catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
        catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.HEADERNOTFOUND);
        }
    }

    public int loadIndiaStateCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFilterIterator(reader,IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable = () -> censusCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(),false).
                    forEach(census->censusCSVMap.put(census.state,new IndiaCsvDao(census)));
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
            Iterator<CsvState> censusCSVIterator = csvBuilder.getCSVFilterIterator(reader,CsvState.class);
            return this.getCountCSVFile(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
    }

    public <T> int getCountCSVFile(Iterator<T> censusCSVIterator)
    {
        int namOfEateries = 0;
        while (censusCSVIterator.hasNext()) {
            namOfEateries++;
        }
        return namOfEateries;
    }

    public String getAlphabeticalOrderData(String state) throws CensusAnalyserException {
        if(censusCSVMap == null)
        {
            throw new CensusAnalyserException("No Data Found", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
            Comparator<IndiaCsvDao> censusCSVComparator = Comparator.comparing(census -> census.state);
            List<IndiaCsvDao> indiaCsvDaos = censusCSVMap.values().stream().collect(Collectors.toList());
            this.sort(indiaCsvDaos,censusCSVComparator);
            return new Gson().toJson(indiaCsvDaos);
    }

    private void sort(List<IndiaCsvDao> indiaCsvDaos, Comparator<IndiaCsvDao> censusCSVComparator) {
        IndiaCsvDao temp;
        for(int i=0;i<indiaCsvDaos.size();i++){
            for(int j=i+1;j<indiaCsvDaos.size();j++){
                if(censusCSVComparator.compare(indiaCsvDaos.get(i),indiaCsvDaos.get(j)) > 0){
                    temp=indiaCsvDaos.get(i);
                    indiaCsvDaos.set(i, indiaCsvDaos.get(j));
                    indiaCsvDaos.set(j,temp );
                }
            }
        }
    }
}