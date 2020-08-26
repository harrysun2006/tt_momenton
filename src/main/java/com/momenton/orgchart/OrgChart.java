package com.momenton.orgchart;

import com.momenton.orgchart.exception.CycleManagementException;
import com.momenton.orgchart.exception.DuplicatedEmployeeIdException;
import com.momenton.orgchart.exception.EmptyOrgException;
import com.momenton.orgchart.exception.InvalidManagerIdException;
import com.momenton.orgchart.exception.ManageSelfException;
import com.momenton.orgchart.exception.MultipleCEOException;
import com.momenton.orgchart.exception.OrgChartException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OrgChart {

    // ceo's employee id
    private List<Integer> ceos = new ArrayList<>();
    // id -> name
    private Map<Integer, String> names = new Hashtable<>();
    // id -> managerId
    private Map<Integer, Integer> reports = new HashMap<>();
    // manager ids
    private Set<Integer> managers = new HashSet<>();
    // all employees
    private Map<Integer, Employee> allEmployees = new Hashtable<>();

    // return null if the value is not a valid integer
    private static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    // parse a line of input data, do a basic validation and save the values to local map vars
    private void parseLine(String line) {
        String[] values = line.split(",");
        // discard extra input
        if (values.length < 2) {
            throw new OrgChartException("Invalid input: " + line);
        }
        // trim each value
        String name = values[0].trim();
        Integer id = parseInt(values[1]);
        // assume employee id is a positive integer
        if (id == null || id <= 0) {
            throw new OrgChartException("Invalid input: employee id should be a positive integer!");
        }
        // validate name, and we allow employees to have same name
        if ("".equals(name)) {
            throw new OrgChartException("Invalid input: name can't be empty!");
        }
        // duplication, already read the employee id
        if (names.containsKey(id)) {
            throw new DuplicatedEmployeeIdException(id);
        }
        names.put(id, name);
        // the employee is the CEO of the organisation
        if (values.length == 2 || values[2].trim().length() == 0) {
            ceos.add(id);
            // for CEO, we keep a special report relationship for the sake of build employee later
            reports.put(id, null);
            return;
        }
        // assume manager id is a positive integer
        Integer managerId = parseInt(values[2]);
        if (managerId == null || managerId <= 0) {
            throw new OrgChartException("Invalid input: manager id should be a positive integer!");
        }
        // people can't be the manager of themselves!
        if (id.equals(managerId)) {
            throw new ManageSelfException(name);
        }
        // detect management cycle
        Set<Integer> rr = new HashSet<>();
        Integer m = managerId;
        do {
            m = reports.get(m);
            if (m != null) {
                rr.add(m);
            }
        } while (m != null);
        if (rr.contains(id)) {
            throw new CycleManagementException();
        }
        // the report relationship is valid
        reports.put(id, managerId);
        managers.add(managerId);
    }

    private Employee createEmployee(Integer id) {
        Employee emp = new Employee(id, names.get(id), reports.get(id), createTeam(id));
        allEmployees.put(id, emp);
        return emp;
    }

    private List<Employee> createTeam(Integer id) {
        return reports.entrySet().stream()
                .filter(e -> id.equals(e.getValue()))
                .map(e -> createEmployee(e.getKey()))
                .sorted(Comparator.comparing(Employee::getId))
                .collect(Collectors.toList());
    }

    public Org build(List<String> input) {
        input.forEach(this::parseLine);
        // check if there are more than one CEO in the organisation
        if (ceos.size() > 1) {
            throw new MultipleCEOException();
        }
        // check if it's an empty input or there is no CEO in the organisation
        if (ceos.isEmpty() || names.isEmpty()) {
            throw new EmptyOrgException();
        }
        // check if all the manager ids are valid
        managers.stream()
                .filter(mid -> !(names.containsKey(mid)))
                .findAny().ifPresent(mid -> {
                    throw new InvalidManagerIdException(mid);
        });
        // build the organisation chart
        Employee ceo = createEmployee(ceos.get(0));
        return new Org(ceo, allEmployees);
    }

    public static void main(String[] args) throws Exception {
        File input;
        if (args.length == 0) {
            input = new File(ClassLoader.getSystemResource("input.txt").toURI());
        } else {
            input = Paths.get(args[0]).toFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;
        List<String> data = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            data.add(line);
        }
        OrgChart chart = new OrgChart();
        Org org = chart.build(data);
        OrgRenderer renderer = new SimpleOrgRenderer();
        renderer.render(org);
    }
}
