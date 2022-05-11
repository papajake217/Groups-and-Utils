
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BijectionGroup {

    public static <T> Group<Function<T,T>> bijectionGroup(Set<T> elements) {

        Group<Function<T, T>> bijections = new Group<Function<T, T>>() {
            @Override
            public Function<T, T> binaryOperation(Function<T, T> one, Function<T, T> other) {
                Function<T,T> func = new Function<T, T>() {
                    @Override
                    public T apply(T t) {
                        return one.apply(other.apply(t));
                    }
                };

                return func;
            }

            @Override
            public Function<T, T> identity() {
                Function<T, T> identityFunc = new Function<T, T>() {
                    @Override
                    public T apply(T t) {
                        return t;
                    }
                };
                return identityFunc;
            }

            @Override
            public Function<T, T> inverseOf(Function<T, T> ttFunction) {
                Function<T,T> inverseFunc = new Function<T, T>() {


                    HashMap<T, T> mappings = new HashMap<T, T>() {
                        public HashMap<T, T> addMappings(Function<T, T> fun,Set<T> elements) {
                            for (T element : elements) {
                                T output = fun.apply(element);
                                this.put(output, element);
                            }
                            return this;
                        }
                    }.addMappings(ttFunction,elements);


                    @Override
                    public T apply(T t) {
                        return mappings.get(t);
                    }
                };
                return inverseFunc;
            }
        };
        return bijections;

    }


    public static <T> Collection<Function<T,T>> bijectionsOf(Set<T> domain){
        ArrayList<Function<T,T>> bijections = new ArrayList<Function<T, T>>();
        ArrayList<T> d = new ArrayList<T>(domain);
        ArrayList<T> ogList = (ArrayList<T>) d.clone();
        bijections = permute(ogList,d,0);


        return bijections;
    }


    public static <T> ArrayList<Function<T,T>> permute(List<T> ogList,List<T> input, int k){
        ArrayList<Function<T,T>> output = new ArrayList<Function<T,T>>();
        for(int i = k; i < input.size(); i++){
            java.util.Collections.swap(input, i, k);
            output.addAll(permute(ogList,input, k+1));
            java.util.Collections.swap(input, k, i);
        }
        if (k == input.size() -1){

            Function<T, T> func = new Function<T, T>() {

                public HashMap<T,T> getMappings(){
                    return this.mappings;
                }


                public HashMap<T,T> mappings = new HashMap<T,T>(){
                    public HashMap<T,T> addPerms(List<T> ogList,List<T> input){
                        for(int i=0;i<ogList.size();i++){
                            this.put(ogList.get(i),input.get(i));
                        }
                        return this;
                    }


                }.addPerms(ogList,input);

                @Override
                public T apply(T t) {
                    return mappings.get(t);
                }

            };

            output.add(func);
            return output;

        }
        return output;
    }


    public static void main(String... args) {
        Set<Integer> a_few = Stream.of(1, 2, 3).collect(Collectors.toSet());

        Collection<Function<Integer,Integer>> bijections = bijectionsOf(a_few);
        bijections.forEach(aBijection -> {
            a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
            System.out.println();
        });

        Group<Function<Integer,Integer>> g = bijectionGroup(a_few);
        Function<Integer,Integer> f1 = bijectionsOf(a_few).stream().skip(5).findFirst().get();
        Function<Integer,Integer> f2 = g.inverseOf(f1);
        Function<Integer,Integer> id = g.identity();

       for(Integer element : a_few){
           System.out.println(element + "->" + f1.apply(element));
       }

        for(Integer element : a_few){
            System.out.println(element + "->" + f2.apply(element));
        }

        for(Integer element : a_few){
            System.out.println(element + "->" + id.apply(element));
        }

    }

}
