package com.cidacs.rl.config;

public class ColumnConfigModel {
    private String id;
    private String type;
    private String indexA;
    private String indexB;
    private double weight;

    public ColumnConfigModel(String id, String type, String indexA, String indexB, double weight) {
        this.id = id;
        this.type = type;
        this.indexA = indexA;
        this.indexB = indexB;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndexdA() {
        return indexA;
    }

    public void setIndedA(String indexA) {
        this.indexA = indexA;
    }

    public String getIndexB() {
        return indexB;
    }

    public void setIndexB(String indexB) {
        this.indexB = indexB;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
