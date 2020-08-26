package com.momenton.orgchart;

import java.util.List;

public class Employee {

    private Integer id;
    private String name;
    private Integer managerId;
    // employee's subordinates
    private List<Employee> subs;

    public Employee() {
    }

    public Employee(Integer id, String name, Integer managerId, List<Employee> subs) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
        this.subs = subs;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public List<Employee> getSubs() {
        return subs;
    }
}
