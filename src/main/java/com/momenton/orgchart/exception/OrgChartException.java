package com.momenton.orgchart.exception;

public class OrgChartException extends RuntimeException {

    private static final long serialVersionUID = 6920207173852590205L;

    public OrgChartException(String message) {
        super(message);
    }

    public OrgChartException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrgChartException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
