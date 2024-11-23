package bg.sofia.uni.fmi.mjt.glovo.dataStructures;

import java.util.Comparator;
import java.util.Objects;

public class Pair<T, S> {
    public T first;
    public S second;

    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }

    //TODO ask if it is okay or just leave java to fill them by default with null
    public Pair() {
        this.first = null;
        this.second = null;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null|| o.getClass()!= getClass()){
            return false;
        }
        Pair<?,?>casted=(Pair<?, ?>) o;
        return Objects.equals(casted.first,first)&& Objects.equals(casted.second,second);
    }

    @Override
    public int hashCode(){
        //return Objects.hashCode(first,second);//TODO how was the method to hash more than one obj
        return 0;
    }

}
