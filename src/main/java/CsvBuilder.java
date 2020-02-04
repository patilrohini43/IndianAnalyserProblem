import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;

public class CsvBuilder<T> implements ICsvBuilder {
      public  Iterator<T> getCSVFilterIterator(Reader reader, Class csvClass) {
        CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<T> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }

}
