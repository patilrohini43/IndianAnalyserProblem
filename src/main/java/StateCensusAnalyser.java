import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class StateCensusAnalyser {

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            Iterator<IndiaCensusCSV> censusCSVIterator = this.getCSVFilterIterator(reader,IndiaCensusCSV.class);
            return this.getCountCSVFile(censusCSVIterator);
        }
        catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.TYPE_NOTFOUND);
        }
        catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.HEADERNOTFOUND);
        }
    }

    public int loadIndiaStateCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            Iterator<CsvState> censusCSVIterator = this.getCSVFilterIterator(reader,CsvState.class);
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
            T censusData = censusCSVIterator.next();
            System.out.println(censusData);
        }
        return namOfEateries;
    }

    public <T> Iterator<T> getCSVFilterIterator(Reader reader, Class<T> csvStateClass)
    {
        CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvStateClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<T> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }
}
