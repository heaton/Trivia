package com.adaptionsoft.games.trivia.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class TextConsole extends PrintStream {

    private List<String> lines;

    public TextConsole() {
        super(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        });
        lines = new ArrayList<String>();
    }

    @Override
    public void println(String x) {
        lines.add(x);
    }

    @Override
    public void println(Object x) {
        lines.add(x.toString());
    }

    public List<String> lines(){
        return lines;
    }

    public void clear() {
        lines.clear();
    }
}
