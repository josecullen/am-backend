package com.ambackend.callcenter.dispatcher;
import com.ambackend.callcenter.model.Employee;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Stream;

/**
 * Created by josecullen on 11/07/17.
 */
public class CallAttenders {
    PriorityBlockingQueue<Employee> queue;
    public CallAttenders(){
        queue = new PriorityBlockingQueue<Employee>(100,(Employee a, Employee b) ->
            a.attendPriority() < b.attendPriority() ? -1 : 1
        );
    }

    public synchronized boolean add(Employee e){
        return this.queue.add(e);
    }

    public synchronized Employee poll(){
        return queue.poll();
    }

    public Stream<Employee> getAsArray(){
        return queue.stream();
    }

    public synchronized void clear(){
        queue.clear();
    }
}
