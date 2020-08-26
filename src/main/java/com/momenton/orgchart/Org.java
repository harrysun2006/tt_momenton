package com.momenton.orgchart;

import java.util.Map;
import java.util.Optional;

public class Org {

    // the CEO of the organisation
    private Employee ceo;
    // a map of all employees for a quick lookup
    private Map<Integer, Employee> allEmployees;

    protected Org(Employee ceo, Map<Integer, Employee> emps) {
        this.ceo = ceo;
        this.allEmployees = emps;
    }

    public Employee getCeo() {
        return ceo;
    }

    public Optional<Employee> findOne(Integer employeeId) {
        return Optional.ofNullable(allEmployees.get(employeeId));
    }
}
