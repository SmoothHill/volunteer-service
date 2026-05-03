package com.yueping.volunteer.model;

public class AreaStatView {

    private String label;
    private int value;

    public AreaStatView() {
    }

    public AreaStatView(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
