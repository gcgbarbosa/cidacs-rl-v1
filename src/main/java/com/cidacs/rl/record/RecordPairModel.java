package com.cidacs.rl.record;

public class RecordPairModel {
    private RecordModel recordA;
    private RecordModel recordB;
    private double score;

    public RecordPairModel(RecordModel recordA, RecordModel recordB, double score) {
        this.recordA = recordA;
        this.recordB = recordB;
        this.score = score;
    }

    public RecordModel getRecordA() {
        return recordA;
    }

    public void setRecordA(RecordModel recordA) {
        this.recordA = recordA;
    }

    public RecordModel getRecordB() {
        return recordB;
    }

    public void setRecordB(RecordModel recordB) {
        this.recordB = recordB;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
