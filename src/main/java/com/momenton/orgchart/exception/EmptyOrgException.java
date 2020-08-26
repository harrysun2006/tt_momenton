package com.momenton.orgchart.exception;

public class EmptyOrgException extends RuntimeException {

    public EmptyOrgException() {
        super("Invalid org chart: empty organisation!");
    }

}
