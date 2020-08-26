package com.momenton.orgchart.exception;

public class InvalidManagerIdException extends RuntimeException {

    public InvalidManagerIdException(Integer id) {
        super("Invalid manager: employee with id " + id + " does not exist!");
    }

}
