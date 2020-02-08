import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICsvBuilder<T> {
    public Iterator<T> getCSVFilterIterator(Reader reader, Class csvClass);
    public List<T> getCSVFilterList(Reader reader, Class csvClass);
}
