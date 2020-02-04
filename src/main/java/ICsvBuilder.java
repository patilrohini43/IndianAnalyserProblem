import java.io.Reader;
import java.util.Iterator;

public interface ICsvBuilder<T> {

    public Iterator<T> getCSVFilterIterator(Reader reader, Class csvClass);

}
