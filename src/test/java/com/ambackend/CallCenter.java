package com.ambackend;

import com.ambackend.callcenter.dispatcher.Call;
import com.ambackend.callcenter.dispatcher.Dispatcher;
import com.ambackend.callcenter.model.Director;
import com.ambackend.callcenter.model.Employee;
import com.ambackend.callcenter.model.Operator;
import com.ambackend.callcenter.model.Supervisor;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import rx.Subscription;

import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class CallCenter {

    @BeforeClass
    public static void init(){
        BasicConfigurator.configure();
    }

    @Before
    public void beforeEach(){
        Dispatcher.getInstance().getAttenders().clear();
    }

    @Test
    public void testDispatcherTimeAndPriority() throws InterruptedException {
        Dispatcher dispatcher = populatedDispatcher(7,4,2);
        final Counter counter = new Counter(10, 0);

        Subscription subscription = dispatcher.observe().subscribe(dispatcherChange -> {
            switch (dispatcherChange.getType()){
                case CALL_STARTED:
                    if(dispatcherChange.getEmployee().attendPriority() > Employee.OPERATOR){
                        assertPriority(dispatcherChange.getEmployee(), dispatcher.getAttenders().getAsArray());
                    }
                    break;
                case CALL_ENQUEUE:
                    assertTrue("With "+ counter.totalCalls +" and 14 Employees we can't have a waiting call", false);
                    break;
                case CALL_ENDED:
                    counter.endedCalls++;
                    break;
            }

        });

        for(int i = 0; i < counter.totalCalls; i++){
            dispatcher.dispatchCall(new Call());
            TimeUnit.MILLISECONDS.sleep((long) 200);
        }

        TimeUnit.SECONDS.sleep((long) 15);
        assertEquals("We must have the same ended calls as total calls", counter.endedCalls, counter.totalCalls);
        subscription.unsubscribe();
    }

    @Test
    public void testDispatcherMoreCallsThanThreads() throws InterruptedException {
        Dispatcher dispatcher = populatedDispatcher(7,4,2);
        final Counter counter = new Counter(30, 0);

        Subscription subscription = dispatcher.observe().subscribe(dispatcherChange -> {
            switch (dispatcherChange.getType()){
                case CALL_STARTED:
                    if(dispatcherChange.getEmployee().attendPriority() > Employee.OPERATOR){
                        assertPriority(dispatcherChange.getEmployee(), dispatcher.getAttenders().getAsArray());
                    }
                    break;
                case CALL_ENQUEUE:
                    assertEquals("If a call is enqueued, attenders queue must be empty",
                            0,
                            dispatcher.getAttenders().getAsArray().count());
                    break;
                case CALL_ENDED:
                    counter.endedCalls++;
                    break;
            }

        });

        for(int i = 0; i < counter.totalCalls; i++){
            dispatcher.dispatchCall(new Call());
            TimeUnit.MILLISECONDS.sleep((long) 200);
        }

        TimeUnit.SECONDS.sleep((long) 30);
        assertEquals("We must have the same ended calls as total calls", counter.endedCalls, counter.totalCalls);
        subscription.unsubscribe();
    }

    @Test
    public void testLessEmployeesThanCalls() throws InterruptedException {
        Dispatcher dispatcher = populatedDispatcher(3,2,1);
        final Counter counter = new Counter(10, 0);

        Subscription subscription = dispatcher.observe().subscribe(dispatcherChange -> {
            switch (dispatcherChange.getType()){
                case CALL_STARTED:
                    if(dispatcherChange.getEmployee().attendPriority() > Employee.OPERATOR){
                        assertPriority(dispatcherChange.getEmployee(), dispatcher.getAttenders().getAsArray());
                    }
                    break;
                case CALL_ENQUEUE:
                    assertEquals("If a call is enqueued, attenders queue must be empty",
                            0,
                            dispatcher.getAttenders().getAsArray().count());
                    break;
                case CALL_ENDED:
                    counter.endedCalls++;
                    break;
            }

        });

        for(int i = 0; i < counter.totalCalls; i++){
            dispatcher.dispatchCall(new Call());
            TimeUnit.MILLISECONDS.sleep((long) 200);
        }

        TimeUnit.SECONDS.sleep((long) 20);
        assertEquals("We must have the same ended calls as total calls", counter.endedCalls, counter.totalCalls);
        subscription.unsubscribe();
    }

    void assertPriority(Employee attender, Stream<Employee> queue){
        boolean noMorePriorityEmployee = queue
                .allMatch(e -> e.attendPriority() >= attender.attendPriority());
        assertTrue("Some user in queue has priority to attend the call", noMorePriorityEmployee);
    }

    Dispatcher populatedDispatcher(int operators, int supervisors, int directors){
        Dispatcher dispatcher = Dispatcher.getInstance();
        for(int i = 0; i < operators; i++){
            dispatcher.getAttenders().add(new Operator());
        }

        for(int i = 0; i < supervisors; i++){
            dispatcher.getAttenders().add(new Supervisor());
        }

        for(int i = 0; i < directors; i++){
            dispatcher.getAttenders().add(new Director());
        }

        return dispatcher;
    }

    class Counter {
        int totalCalls;
        int endedCalls;
        public Counter(int totalCalls, int endedCalls){
            this.totalCalls = totalCalls;
            this.endedCalls = endedCalls;
        }
    }
}
