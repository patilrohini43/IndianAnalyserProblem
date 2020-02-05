import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class StateCensusAnalyser {

    List<IndiaCensusCSV> censusCSVIterator=null;

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCsvBuilder();
            censusCSVIterator = csvBuilder.getCSVFilterList(reader, IndiaCensusCSV.class);
            System.out.println("size"+censusCSVIterator.size());
            return censusCSVIterator.size();
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
            Iterator<CsvState> censusCSVIterator =csvBuilder.getCSVFilterIterator(reader,CsvState.class);
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

    public String getAlphabeticalOrderData() throws CensusAnalyserException {
        System.out.println(censusCSVIterator);
        if(censusCSVIterator == null)
        {
            throw new CensusAnalyserException("No Data Found", CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }

            Comparator<IndiaCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.state);
            this.sort(censusCSVComparator);
            String jsondata = new Gson().toJson(censusCSVIterator);
            return jsondata;
    }

    private void sort(Comparator<IndiaCensusCSV> censusCSVComparator) {
        IndiaCensusCSV temp;
        for(int i=0;i<censusCSVIterator.size();i++){

            for(int j=i+1;j<censusCSVIterator.size();j++){

                if(censusCSVComparator.compare(censusCSVIterator.get(i),censusCSVIterator.get(j)) > 0){
                    temp=censusCSVIterator.get(i);
                    censusCSVIterator.set(i, censusCSVIterator.get(j));
                    censusCSVIterator.set(j,temp );
                }
            }
        }
    }
}