package com.momenton.orgchart.exception;

public class DuplicatedEmployeeIdException extends RuntimeException {

    public DuplicatedEmployeeIdException(Integer id) {
        super("Duplicated employee id: " + id);
    }

}
