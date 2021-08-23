package edu.boisestate.cs.util;

public class Tuple<TElement1, TElement2> {

    private final TElement1 element1;
    private final TElement2 element2;

    public TElement1 get1() {
        return element1;
    }

    public TElement2 get2() {
        return element2;
    }

    public Tuple(TElement1 element1, TElement2 element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (element1 != null ?
            !element1.equals(tuple.element1) :
            tuple.element1 != null) {
            return false;
        }
        return element2 != null ?
               element2.equals(tuple.element2) :
               tuple.element2 == null;
    }

    @Override
    public int hashCode() {
        int result = element1 != null ? element1.hashCode() : 0;
        result = 31 * result + (element2 != null ? element2.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
    	StringBuilder ret = new StringBuilder("[");
    	ret.append(element1).append(",").append(element2).append("]");
    	return ret.toString();
    }
}
