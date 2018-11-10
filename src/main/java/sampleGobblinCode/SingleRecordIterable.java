package sampleGobblinCode;

/**
 * Created by Anirudha Sathe on 9/11/18.
 */

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

import java.util.Iterator;

public class SingleRecordIterable<T> implements Iterable<T>{

    private final T value;

    public SingleRecordIterable(T value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    //adding iterator
    public Iterator<T> iterator() {
        return Iterators.singletonIterator(this.value);
    }

}
