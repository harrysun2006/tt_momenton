package com.momenton.orgchart.exception;

public class ManageSelfException extends RuntimeException {

    public ManageSelfException(String name) {
        super("Invalid manager: " + name + " can't not be the manager of themselves!");
    }

}
