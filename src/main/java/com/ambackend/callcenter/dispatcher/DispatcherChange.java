package com.ambackend.callcenter.dispatcher;

import com.ambackend.callcenter.model.Employee;

/**
 * Created by josecullen on 12/07/17.
 * Represents the Event Change object for Dispatcher
 */
public class DispatcherChange {
    private Employee employee;
    private Call call;
    private DispatcherChangeType type;

    public DispatcherChange(DispatcherChangeType type, Employee employee, Call call){
        this.type = type;
        this.employee = employee;
        this.call = call;
    }

    public DispatcherChangeType getType(){
        return type;
    }

    public Employee getEmployee(){
        return employee;
    }

    public Call getCall(){
        return call;
    }
}
