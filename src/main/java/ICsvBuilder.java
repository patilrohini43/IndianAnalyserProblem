import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import Exception.CsvBuilderException;

public interface ICsvBuilder<T> {

    public Iterator<T> getCSVFilterIterator(Reader reader, Class csvClass);
    public List<T> getCSVFilteerList(Reader reader, Class csvClass);

}
