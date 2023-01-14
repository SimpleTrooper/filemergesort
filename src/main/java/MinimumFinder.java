import java.util.Comparator;
import java.util.Iterator;

public class MinimumFinder {
    public static <T> T findMinimum(Iterable<T> iterable, Comparator<T> comparator) {
        Iterator<T> iterator = iterable.iterator();
        T min = null;
        if (iterator.hasNext()) {
            min = iterator.next();
        }
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (comparator.compare(min, next) > 0) {
                min = next;
            }
        }
        return min;
    }
}
