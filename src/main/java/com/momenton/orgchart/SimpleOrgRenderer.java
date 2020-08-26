package com.momenton.orgchart;

import java.io.OutputStream;
import java.io.PrintStream;

public class SimpleOrgRenderer implements OrgRenderer {

    public static final int DEFAULT_COLUMN_WIDTH = 30;
    private PrintStream stream;
    private int columnWidth;

    public SimpleOrgRenderer() {
        this(System.out, DEFAULT_COLUMN_WIDTH);
    }

    public SimpleOrgRenderer(int columnWidth) {
        this(System.out, columnWidth);
    }

    public SimpleOrgRenderer(PrintStream stream, int columnWidth) {
        this.stream = stream;
        this.columnWidth = columnWidth;
    }

    private void renderEmployee(Employee e, int level) {
        if (level == 0) {
            String format = "%1$-30s";
            stream.println(String.format(format, e.getName()));
        } else {
            String format = "%1$" + columnWidth * level + "s%2$-30s";
            stream.println(String.format(format, " ", e.getName()));
        }
        e.getSubs().forEach(sub -> renderEmployee(sub, level + 1));
    }

    public void render(Org org) {
        renderEmployee(org.getCeo(), 0);
    }
}
