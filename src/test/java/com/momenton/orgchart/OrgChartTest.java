package com.momenton.orgchart;

import com.momenton.orgchart.exception.CycleManagementException;
import com.momenton.orgchart.exception.DuplicatedEmployeeIdException;
import com.momenton.orgchart.exception.InvalidManagerIdException;
import com.momenton.orgchart.exception.ManageSelfException;
import com.momenton.orgchart.exception.MultipleCEOException;
import com.momenton.orgchart.exception.EmptyOrgException;
import com.momenton.orgchart.exception.OrgChartException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrgChartTest {

    @Test(expected = EmptyOrgException.class)
    public void testEmpty() {
        OrgChart chart = new OrgChart();
        chart.build(new ArrayList<>());
    }

    @Test(expected = MultipleCEOException.class)
    public void testMultipleCEO() {
        List<String> input = Arrays.asList(
                "Alan,100,",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = OrgChartException.class)
    public void testInvalidEmployeeId1() {
        List<String> input = Arrays.asList(
                "Alan,a100,150",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = OrgChartException.class)
    public void testInvalidEmployeeId2() {
        List<String> input = Arrays.asList(
                "Alan,-100,150",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = OrgChartException.class)
    public void testInvalidEmployeeName() {
        List<String> input = Arrays.asList(
                ",100,150",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = OrgChartException.class)
    public void testInvalidManagerId1() {
        List<String> input = Arrays.asList(
                "Alan,100,a150",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = OrgChartException.class)
    public void testInvalidManagerId2() {
        List<String> input = Arrays.asList(
                "Alan,100,-150",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = InvalidManagerIdException.class)
    public void testInvalidManager() {
        List<String> input = Arrays.asList(
                "Alan,100,120",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = DuplicatedEmployeeIdException.class)
    public void testDuplicatedEmployeeId() {
        List<String> input = Arrays.asList(
                "Alan,100,120",
                "Martin,100,200"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = ManageSelfException.class)
    public void testManageSelf() {
        List<String> input = Arrays.asList(
                "Alan,100,100"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = CycleManagementException.class)
    public void testCycleManagement1() {
        List<String> input = Arrays.asList(
                "Alan,100,200",
                "Martin,200,100",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    @Test(expected = CycleManagementException.class)
    public void testCycleManagement2() {
        List<String> input = Arrays.asList(
                "Alan,100,200",
                "Martin,200,300",
                "Alex,300,100",
                "Jamie,150,"
        );
        OrgChart chart = new OrgChart();
        chart.build(input);
    }

    private static void hasSubordinates(Employee emp, List<Integer> subs) {
        List<Integer> subs2 = emp.getSubs().stream().map(Employee::getId).collect(Collectors.toList());
        // assertTrue(subs.containsAll(subs2));
        // assertTrue(subs2.containsAll(subs));
        assertEquals(subs, subs2);
    }

    @Test
    public void testNormalOrg() {
        List<String> input = Arrays.asList(
                "Alan,100,150",
                "Martin,220,100",
                "Jamie,150,",
                "Alex,275,100",
                "Steve,400,150",
                "David,190,400");
        OrgChart chart = new OrgChart();
        Org org = chart.build(input);
        Employee ceo = org.getCeo();
        // CEO is Jamie
        assertEquals(ceo.getId(), Integer.valueOf(150));
        assertEquals(ceo.getName(), "Jamie");
        // CEO has two subordinates: 100, 400
        hasSubordinates(ceo, Arrays.asList(100, 400));
        // Alan has two subordinates: 220, 275
        Employee alan = org.findOne(100).orElseThrow(() -> new RuntimeException("Test data error!"));
        assertEquals(alan.getName(), "Alan");
        hasSubordinates(alan, Arrays.asList(220, 275));
        // Martin is not a manager, and his manager is Alan
        Employee martin = org.findOne(220).orElseThrow(() -> new RuntimeException("Test data error!"));
        assertEquals(martin.getName(), "Martin");
        assertEquals(martin.getSubs().size(), 0);
        assertEquals(martin.getManagerId(), Integer.valueOf(100));
        // Steve has one subordinate: 190
        Employee steve = org.findOne(400).orElseThrow(() -> new RuntimeException("Test data error!"));
        assertEquals(steve.getName(), "Steve");
        hasSubordinates(steve, Arrays.asList(190));
        // David is not a manager, and his manager is Alan
        Employee david = org.findOne(190).orElseThrow(() -> new RuntimeException("Test data error!"));
        assertEquals(david.getName(), "David");
        assertEquals(david.getSubs().size(), 0);
        assertEquals(david.getManagerId(), Integer.valueOf(400));

    }

}
