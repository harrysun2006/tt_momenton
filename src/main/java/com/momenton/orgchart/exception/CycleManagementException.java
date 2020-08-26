package com.momenton.orgchart.exception;

public class CycleManagementException extends RuntimeException {

    public CycleManagementException() {
        super("Invalid manager: management cycle detected!");
    }

}
