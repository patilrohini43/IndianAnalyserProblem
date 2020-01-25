import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class StateCensusAnalyser { public int loadIndiaCensusData(String csvFilePath) throws IOException {

        Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
        CsvToBeanBuilder<IndiaCensusCSV> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(IndiaCensusCSV.class);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<IndiaCensusCSV> csvToBean = csvToBeanBuilder.build();
        Iterator<IndiaCensusCSV> censusCSVIterator = csvToBean.iterator();
        ;
        int namOfEateries = 0;
        while (censusCSVIterator.hasNext()) {
            namOfEateries++;
            IndiaCensusCSV censusData = censusCSVIterator.next();
        }
        return namOfEateries;
    }
}
