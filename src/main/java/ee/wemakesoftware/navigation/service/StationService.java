package ee.wemakesoftware.navigation.service;

import java.util.List;

public interface StationService<T> {
    Iterable<T> getAll();

    T saveStation (T station);
}
