import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StateCensusAnalyser {

    List<IndiaCsvDao> censusCSVList=null;

    public  StateCensusAnalyser()
    {
        this.censusCSVList=new ArrayList<IndiaCsvDao>();
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
            while (censusCSVIterator.hasNext())
            {
              this.censusCSVList.add(new IndiaCsvDao(censusCSVIterator.next()));
            }
            return censusCSVList.size();
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
        if(censusCSVList == null)
        {
            throw new CensusAnalyserException("No Data Found", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
            Comparator<IndiaCsvDao> censusCSVComparator = Comparator.comparing(census -> census.state);
            this.sort(censusCSVComparator);
            return new Gson().toJson(censusCSVList);
    }

    private void sort(Comparator<IndiaCsvDao> censusCSVComparator) {
        IndiaCsvDao temp;
        for(int i=0;i<censusCSVList.size();i++){
            for(int j=i+1;j<censusCSVList.size();j++){
                if(censusCSVComparator.compare(censusCSVList.get(i),censusCSVList.get(j)) > 0){
                    temp=censusCSVList.get(i);
                    censusCSVList.set(i, censusCSVList.get(j));
                    censusCSVList.set(j,temp );
                }
            }
        }
    }
}