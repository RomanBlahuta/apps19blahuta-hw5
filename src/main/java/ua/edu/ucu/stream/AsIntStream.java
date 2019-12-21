package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import java.util.ArrayList;

public class AsIntStream implements IntStream {

    private int[] values;
    private ArrayList<Object> streamActions = new ArrayList<>();



    private AsIntStream() {
        this.values = new int[] {};
    }

    private AsIntStream(int[] values) {
        this.values = values;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }



    /** Terminal method **/
    @Override
    public Double average() {
        AsIntStream processedStream = invokeTerminalMethod();
        emptyStreamHandler(processedStream);
        double avg = 0;
        for (int i: processedStream.toArray()) {
            avg += i;
        }
        avg /= processedStream.values.length;
        return avg;
    }


    /** Terminal method **/
    @Override
    public Integer max() {
        AsIntStream processedStream = invokeTerminalMethod();
        emptyStreamHandler(processedStream);
        int maxVal = Integer.MIN_VALUE;
        for (int i: processedStream.toArray()) {
            if (i >= maxVal) {
                maxVal = i;
            }
        }
        return maxVal;
    }


    /** Terminal method **/
    @Override
    public Integer min() {
        AsIntStream processedStream = invokeTerminalMethod();
        emptyStreamHandler(processedStream);
        int minVal = Integer.MAX_VALUE;
        for (int i: processedStream.toArray()) {
            if (i <= minVal) {
                minVal = i;
            }
        }
        return minVal;
    }


    /** Terminal method **/
    @Override
    public long count() {
        AsIntStream processedStream = invokeTerminalMethod();
        return processedStream.toArray().length;
    }


    /** Terminal method **/
    @Override
    public Integer sum() {
        AsIntStream processedStream = invokeTerminalMethod();
        emptyStreamHandler(processedStream);
        return processedStream.reduce(0, (sum, x) -> sum += x);
    }


    @Override
    public IntStream filter(IntPredicate predicate) {
        streamActions.add(predicate);
        return this;
    }

    private IntStream applyFilter(IntPredicate predicate) {
        ArrayList<Integer> filtered = new ArrayList<>();
        for (int i: values) {
            if (predicate.test(i)) {
                filtered.add(i);
            }
        }
        int[] filteredArr = asIntArray(filtered);
        return new AsIntStream(filteredArr);
    }


    /** Terminal method **/
    @Override
    public void forEach(IntConsumer action) {
        AsIntStream processedStream = invokeTerminalMethod();
        for (int i: processedStream.toArray()) {
            action.accept(i);
        }
    }


    @Override
    public IntStream map(IntUnaryOperator mapper) {
        streamActions.add(mapper);
        return this;
    }

    private IntStream applyMap(IntUnaryOperator mapper) {
        ArrayList<Integer> mapped = new ArrayList<>();
        for (int i: values) {
            mapped.add(mapper.apply(i));
        }
        int[] mappedArr = asIntArray(mapped);
        return new AsIntStream(mappedArr);
    }


    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        streamActions.add(func);
        return this;
    }

    private IntStream applyFlatMap(IntToIntStreamFunction func) {
        ArrayList<AsIntStream> streams = new ArrayList<>();
        for (int i: values) {
            streams.add((AsIntStream) func.applyAsIntStream(i));
        }
        int resLen = streams.get(0).values.length;
        int[] total = new int[resLen * streams.size()];
        for (int i = 0; i < streams.size(); i++) {
            System.arraycopy(streams.get(i).toArray(),
                    0,
                    total,
                    resLen*i,
                    streams.get(i).values.length);
        }
        return new AsIntStream(total);
    }


    /** Terminal method **/
    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        AsIntStream processedStream = invokeTerminalMethod();
        int res = identity;
        for (int i: processedStream.toArray()) {
            res = op.apply(res, i);
        }
        return res;
    }


    /** Terminal method **/
    @Override
    public int[] toArray() {
        AsIntStream processedStream = invokeTerminalMethod();
        int[] res = new int[processedStream.values.length];
        System.arraycopy(processedStream.values, 0, res, 0, processedStream.values.length);
        return res;
    }


    private int[] asIntArray(ArrayList<Integer> someArr) {
        int[] intArr = new int[someArr.size()];
        for (int i = 0; i < someArr.size(); i++) {
            intArr[i] = (int) someArr.get(i);
        }
        return intArr;
    }


    private AsIntStream invokeTerminalMethod() {
        AsIntStream res = (AsIntStream) AsIntStream.of(values);
        for (Object streamAction : streamActions) {
            if (streamAction instanceof IntPredicate) {
                res = (AsIntStream) res.applyFilter((IntPredicate) streamAction);
            }
            else if (streamAction instanceof IntUnaryOperator) {
                res = (AsIntStream) res.applyMap((IntUnaryOperator) streamAction);
            }
            else if (streamAction instanceof IntToIntStreamFunction) {
                res = (AsIntStream) res.applyFlatMap((IntToIntStreamFunction) streamAction);
            }
        }
        streamActions.clear();
        return res;
    }


    private void emptyStreamHandler(AsIntStream processedStream) {
        if (processedStream.count() == 0) {
            throw new IllegalArgumentException("Empty stream");
        }
    }
}
