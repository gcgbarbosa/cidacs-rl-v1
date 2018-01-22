package util;

public class Column {
    private String name;
    private String type;
    private int column;
    private double weight;


    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }
    
    public int getColumn(){
        return this.column;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public double getWeight(){
        return this.weight;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
}