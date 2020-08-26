package com.momenton.orgchart.exception;

public class MultipleCEOException extends RuntimeException {

    public MultipleCEOException() {
        super("Invalid org chart: more than one CEO!");
    }

}
