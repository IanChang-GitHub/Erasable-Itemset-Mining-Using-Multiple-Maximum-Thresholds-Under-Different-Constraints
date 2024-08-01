/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package fpgrowth;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author Kamran
 */
class frequencyComparitorinHeaderTable implements Comparator<FPtree>{

	Map<String, Double> MGT;
    public frequencyComparitorinHeaderTable(Map<String, Double> MGT) {
    	this.MGT = MGT;
    }

    public int compare(FPtree o1, FPtree o2) {
        if(MGT.get(o1.item) < MGT.get(o2.item)){
            return 1;
        }
        else if(MGT.get(o1.item) > MGT.get(o2.item))
            return -1;
        else
            return 0;
    }

}
