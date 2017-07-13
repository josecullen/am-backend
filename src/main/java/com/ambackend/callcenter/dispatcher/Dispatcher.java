package com.ambackend.callcenter.dispatcher;

import com.ambackend.callcenter.model.Employee;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.subjects.PublishSubject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by josecullen on 11/07/17.
 * Dispatch calls from a thread pool.
 * Manage the employees to assign calls.
 */
public class Dispatcher {
    private static Logger logger = Logger.getLogger(Dispatcher.class);
    private static Dispatcher dispatcher = null;
    private PublishSubject<DispatcherChange> dispatcherChange = PublishSubject.create();
    private ExecutorService calls = Executors.newFixedThreadPool(10);
    private CallAttenders attenders = new CallAttenders();
    private Queue<Call> waitingCalls = new LinkedList<>();

    private Dispatcher(){}

    public static Dispatcher getInstance(){
        if(dispatcher == null){
            dispatcher = new Dispatcher();
        }
        return dispatcher;
    }

    /**
     * Dispatch a call from a thread pool.
     * The dispatching implies assing a high priority to attend Employee
     * @param call
     */
    public synchronized void dispatchCall(Call call){
        CompletableFuture<Employee> future = CompletableFuture.supplyAsync(() -> {
            Employee employee = attenders.poll();
            if(employee != null){
                call.setAttender(employee);
                dispatcherChange.onNext(new DispatcherChange(DispatcherChangeType.CALL_STARTED, employee, call));
                call.resolveCall();
            } else {
                dispatcherChange.onNext(new DispatcherChange(DispatcherChangeType.CALL_ENQUEUE, null, call));
                logger.info("Call "+call.getId()+" waiting for attender . . .");
                this.waitingCalls.add(call);
            }
            return employee;
        }, calls);

        future.thenAccept((Employee e) -> {
            attenders.add(e);
            if(!waitingCalls.isEmpty()){
                dispatchCall(waitingCalls.poll());
            }
            dispatcherChange.onNext(new DispatcherChange(DispatcherChangeType.CALL_ENDED, e, call));
        });
    }

    public Observable<DispatcherChange> observe(){
        return  dispatcherChange.asObservable();
    }

    public CallAttenders getAttenders(){
        return attenders;
    }

    public void shutdown(){
        this.calls.shutdown();
    }

}


