package org.armorer.engine;

public class Soldier {

    
    private int num;
    
    private int attr1;
    
    private int attr2;
    
    private int attr3;
    
    private int attr4;
    
    private int attr5;

    private int attr6;
    
    public Soldier(int num, int attr1, int attr2, int attr3, int attr4, int attr5,int attr6) {
        this.num = num;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.attr4 = attr4;
        this.attr5 = attr5;
        this.attr6 = attr6;
    }
    public Soldier( int attr1, int attr2, int attr3, int attr4, int attr5,int attr6) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.attr4 = attr4;
        this.attr5 = attr5;
        this.attr6 = attr6;
    }
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAttr1() {
        return attr1;
    }

    public void setAttr1(int attr1) {
        this.attr1 = attr1;
    }

    public int getAttr2() {
        return attr2;
    }

    public void setAttr2(int attr2) {
        this.attr2 = attr2;
    }

    public int getAttr3() {
        return attr3;
    }

    public void setAttr3(int attr3) {
        this.attr3 = attr3;
    }

    public int getAttr4() {
        return attr4;
    }

    public void setAttr4(int attr4) {
        this.attr4 = attr4;
    }

    public int getAttr5() {
        return attr5;
    }

    public void setAttr5(int attr5) {
        this.attr5 = attr5;
    }
    
    
    
}
