import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class CsvBuilder<T> implements ICsvBuilder {
    @Override
    public  Iterator<T> getCSVFilterIterator(Reader reader, Class csvClass) {
          return this.getCsvBean(reader,csvClass).iterator();
      }

    @Override
    public List getCSVFilterList(Reader reader, Class csvClass) {
        return  this.getCsvBean(reader,csvClass).parse();
    }

    private CsvToBean<T> getCsvBean(Reader reader, Class csvClass) {
        CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        return csvToBeanBuilder.build();
    }
}