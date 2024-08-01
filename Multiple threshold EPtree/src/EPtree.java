/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package fpgrowth;

import java.util.Vector;


public class FPtree //node
{ 

    public FPtree(String item) { //建構子
        this.item = item;
        next = null;
        children = new Vector<FPtree>();
        root = false;
    }

    boolean root;
    String item;
    Vector<FPtree> children; //子節點
    FPtree parent;  //父節點
    FPtree next; //相同item的右兄弟
    int count; //紀錄出現次數

    boolean isRoot(){
        return root;
    }

}
