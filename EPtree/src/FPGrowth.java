/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package fpgrowth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class FPGrowth {

	Map<String, Double> threshold;
	Map<String, Double> MGT = new HashMap<String, Double>();
    double maxMGT=0;
	Vector<Vector <String>> product = new Vector<Vector <String>>(); //儲存資料庫
	Vector<Integer> profit = new Vector<Integer>();; //儲存產品收益
    //fp-tree constructing fileds
    Vector<FPtree> headerTable;
    FPtree fptree;
    //fp-growth
    Map<String, Integer> frequentPatterns; //Map<key, value> 紀錄frequent itemset

    public FPGrowth(File file, File threshold_file) throws FileNotFoundException { //建構子
        //this.threshold = threshold;
        fptree(file,threshold_file);
        fpgrowth(fptree, threshold, headerTable);
        print();

    }


    private void fptree(File file, File threshold_file) throws FileNotFoundException {
        //preprocessing fields
    	Map<String, Double> threshold = new HashMap<String, Double>();
        Map<String, Integer> itemsMaptoFrequencies = new HashMap<String, Integer>();
        Scanner input = new Scanner(file);
        Scanner input2 = new Scanner(threshold_file);
        List<String> sortedItemsbyFrequencies = new LinkedList<String>();
        Vector<String> itemstoRemove = new Vector<String>();
        preProcessing(file, itemsMaptoFrequencies, threshold, MGT, input, input2, sortedItemsbyFrequencies, itemstoRemove);
        construct_fpTree(file, itemsMaptoFrequencies, MGT, input, sortedItemsbyFrequencies, itemstoRemove); // 檔名,每個item的count,

    }

    private void preProcessing(File file, Map<String, Integer> itemsMaptoFrequencies,  Map<String, Double> threshold, Map<String, Double> MGT, Scanner input, Scanner input2, List<String> sortedItemsbyFrequencies, Vector<String> itemstoRemove) throws FileNotFoundException {
        int totalProfit = 0;
      
    	while (input.hasNext()) { //計算每一個item的gain
            String line = input.nextLine();
            //System.out.println(line);
            String[] temp = line.split("_");
            String[] temp2 = temp[1].split(", ");
            totalProfit += Integer.parseInt(temp[0]);
            profit.add(Integer.parseInt(temp[0]));
            for (int i=0;i<temp2.length;i++){
            	if (itemsMaptoFrequencies.containsKey(temp2[i])) { //檢查hashmap是否存在item
            		int count = itemsMaptoFrequencies.get(temp2[i]); //取得item原本的count
            		itemsMaptoFrequencies.put(temp2[i], count+ Integer.parseInt(temp[0])); // 原本的count+1
            	} 
            	else {//找不到
            		itemsMaptoFrequencies.put(temp2[i], Integer.parseInt(temp[0])); //新建一個item並設count=1
            	}
            
            }
    	}
    	while (input2.hasNext()) //紀錄多門檻
        {
        	String line = input2.next();
        	String[] temp = line.split("_");
        	threshold.put(temp[0], Double.parseDouble(temp[1]));
        	MGT.put(temp[0], Double.parseDouble(temp[1])*totalProfit);
        	if(MGT.get(temp[0]) > maxMGT){
        		maxMGT = MGT.get(temp[0]);
        	}
        }
        input.close();
        input2.close();
        //orderiiiiiiiiiiiiiiiiiiiiiiiiiiiing
        //also elimating non-frequents

        //for breakpoint for comparison
        //將item由大到小排序
        sortedItemsbyFrequencies.add("null"); //linkedlist
        itemsMaptoFrequencies.put("null", totalProfit+1);
        threshold.put("null", 2.0); //hashmap<string, int>
        for (String item : itemsMaptoFrequencies.keySet()) //讀取每個item 讀取每個map中的key
        { 
            double thresh = threshold.get(item);//取得該item的count
            System.out.println( item+ " "+ thresh );
            int i = 0;
            for (String listItem : sortedItemsbyFrequencies) 
            {
                if (threshold.get(listItem) > thresh) 
                {
                    sortedItemsbyFrequencies.add(i, item); //add(index, item)
                    //System.out.println(i+","+item);
                    //System.out.println(i+","+item+","+itemsMaptoFrequencies.get(listItem)+","+count);
                    break;
                }
                i++;
            }
        }
        
        
        for (String listItem : sortedItemsbyFrequencies) {
        	System.out.println(listItem);
        }
        
        //removing non-frequents
        //this pichidegi is for concurrency problem in collection iterators
        for (String listItem : sortedItemsbyFrequencies) {
            if (itemsMaptoFrequencies.get(listItem) > maxMGT ) {
                itemstoRemove.add(listItem);
            }
        }
        for (String itemtoRemove : itemstoRemove) { //移除support<threshold item
            sortedItemsbyFrequencies.remove(itemtoRemove);
        }
        //printttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt
        //     for ( String key : list )
        //        System.out.printf( "%-10s%10s\n", key, items.get( key ) );

    }

    private void construct_fpTree(File file, Map<String, Integer> itemsMaptoFrequencies, Map<String, Double>MGT, Scanner input, List<String> sortedItemsbyFrequencies, Vector<String> itemstoRemove) throws FileNotFoundException {
        //HeaderTable Creation
        // first elements use just as pointers
        headerTable = new Vector<FPtree>();
        for (String itemsforTable : sortedItemsbyFrequencies) { //將由大到小的item加入header
            //System.out.println(itemsforTable);
        	headerTable.add(new FPtree(itemsforTable));
        }
        //FPTree constructing
        input = new Scanner(file);
        //the null node!
        fptree = new FPtree("null"); //增加樹根節點
        fptree.item = null;
        fptree.root = true;
        //ordering frequent items transaction
        int i=0;
        while (input.hasNextLine()) {
            String line = input.nextLine();
            //System.out.println(line);
            String[] temp = line.split("_");
            int profit = Integer.parseInt(temp[0]);
            StringTokenizer tokenizer = new StringTokenizer(temp[1],", ");
            Vector<String> transactionSortedbyFrequencies = new Vector<String>(); //將每筆交易由大到小排列
            product.add(new Vector<String>());
            while (tokenizer.hasMoreTokens()) {
                String item = tokenizer.nextToken();
            	//System.out.println(item);
                if (itemstoRemove.contains(item)) { //已刪除item 不列入排序
                    continue;
                }
                int index = 0;
                for (String vectorString : transactionSortedbyFrequencies) {
                    //some lines of condition is for alphabetically check in equals situatioans
                	//System.out.println(vectorString);
                    if (MGT.get(item) < MGT.get(vectorString) || (Double.toString(MGT.get(vectorString)).equals(Double.toString(MGT.get(item))) && (vectorString.compareToIgnoreCase(item) < 0 ? true : false))) {
                        //在排序中的vector找到位子插入item,插入的item.count大於排序中的某一個item,則插入在前面，若兩個count相同，則依照字典順序，順序在前的擺前面
                    	transactionSortedbyFrequencies.add(index, item);
                    	product.get(i).add(index,item);
                        break;
                    }
                    index++;
                }
                if (!transactionSortedbyFrequencies.contains(item)) { //處理排序陣列每個都比插入的item大
                    transactionSortedbyFrequencies.add(item); //直接加在最後
                    product.get(i).add(item);
                }
            }
            //printing transactionSortedbyFrequencies
            /*
            for (String vectorString : transactionSortedbyFrequencies) {
            System.out.printf("%-10s%10s ", vectorString, itemsMaptoFrequencies.get(vectorString));
            }
            System.out.println();
             *
             */
            //printing transactionSortedbyFrequencies
            /*
            for (String vectorString : transactionSortedbyFrequencies) {
            System.out.printf("%-10s%10s ", vectorString, itemsMaptoFrequencies.get(vectorString));
            }
            System.out.println();
             *
             */
            //adding to tree
            
            for (String vectorString : transactionSortedbyFrequencies) {
            	System.out.print(vectorString+" ");
            }
            System.out.println("==============");
            
            i++;
            insert(transactionSortedbyFrequencies, fptree, headerTable, profit);
            transactionSortedbyFrequencies.clear();
        }
        //headertable reverse ordering
        //first calculating item frequencies in tree
        for (FPtree item : headerTable) { //計算header每個item.count
            int count = 0;
            FPtree itemtemp = item;
            while (itemtemp.next != null) {
                itemtemp = itemtemp.next;
                count += itemtemp.count;
            }
            item.count = count;
            //System.out.println(item.item+" "+item.count);
        }
        Comparator c = new frequencyComparitorinHeaderTable(MGT); //反轉排序 變成由小到大
        Collections.sort(headerTable, c);
        
        for (FPtree item : headerTable) { 
            System.out.println(item.item+" "+MGT.get(item.item)+"--");
        }
        
        input.close();
    }

    void insert(Vector<String> transactionSortedbyFrequencies, FPtree fptree, Vector<FPtree> headerTable, int profit) {
        if (transactionSortedbyFrequencies.isEmpty()) {
            return;
        }
        String itemtoAddtotree = transactionSortedbyFrequencies.firstElement();
        FPtree newNode = null; //結點指標
        boolean ifisdone = false;
        for (FPtree child : fptree.children) { //檢查每個子結點是否有相同的item
            if (child.item.equals(itemtoAddtotree)) {
                newNode = child;
                child.count+= profit;
                ifisdone = true;
                break;
            }
        }
        if (!ifisdone) //沒有相同的子結點
        { 
            newNode = new FPtree(itemtoAddtotree); //新建一個子結點
            newNode.count = profit;
            newNode.parent = fptree; 
            fptree.children.add(newNode);
            for (FPtree headerPointer : headerTable) {
                if (headerPointer.item.equals(itemtoAddtotree)) {
                    while (headerPointer.next != null) {
                        headerPointer = headerPointer.next;
                    }
                    headerPointer.next = newNode;
                }
            }
        }
        transactionSortedbyFrequencies.remove(0); //移除最前面的item
        insert(transactionSortedbyFrequencies, newNode, headerTable, profit);
    }
    
    private FPtree conditional_eptree_constructor(Vector<Vector<String>> dataClone, Vector<Integer> profitClone, Vector<FPtree> conditional_headerTable) {
        //FPTree constructing
        //the null node!      
    	FPtree conditional_eptree = new FPtree("null");
        conditional_eptree.item = null;
        conditional_eptree.root = true;
        //remember our transactions here has oredering and non-frequent items for condition items
        for (int i=0; i<dataClone.size();i++)
        {
        	Vector<String> pattern_vector = new Vector<String>();
        	for (int j=0;j<dataClone.get(i).size();j++)
        	{
        		pattern_vector.add(dataClone.get(i).get(j));
        	}
	            //the insert method
	        insert(pattern_vector, conditional_eptree, conditional_headerTable, profitClone.get(i));
	            //end of insert method
        }
        return conditional_eptree;
    }

    private void fpgrowth(FPtree fptree, Map<String, Double> threshold, Vector<FPtree> headerTable) {
        frequentPatterns = new HashMap<String, Integer>();
        FPgrowth(product, profit, fptree, null, 0, headerTable, frequentPatterns);
    }

    void FPgrowth(Vector<Vector<String>> data,Vector<Integer> profit, FPtree fptree, String base, int baseGain, Vector<FPtree> headerTable, Map<String, Integer> frequentPatterns) {
        for (FPtree iteminTree : headerTable) 
        {
        	Vector<String> deleteItem = new Vector<String>();
        	String baseItem = iteminTree.item; 
        	Vector<Vector<String>> dataClone = new Vector<Vector<String>>();
        	for (Vector<String> product:data) {
        		dataClone.add((Vector<String>) product.clone());
        	}
        	Vector<Integer> profitClone = (Vector<Integer>) profit.clone();
        	
        	int gainofCurrentPattern = 0;
        	while (iteminTree.next != null) { //計算該item的gain
                iteminTree = iteminTree.next;
                gainofCurrentPattern += iteminTree.count; //此item的support
                /*
                //String conditionalPattern = null;
                FPtree conditionalItem = iteminTree.parent;
                
                while (!conditionalItem.isRoot()) { //父節點不是樹根
                    //conditionalPattern = conditionalItem.item + " " + (conditionalPattern != null ? conditionalPattern : ""); //紀錄item上面路徑有哪些結點(B C D)
                	conditionalItem.count = conditionalItem.count - iteminTree.count;
                	conditionalItem = conditionalItem.parent; //指標向上移
                }
                */              
            }
            gainofCurrentPattern += baseGain;
        	
            String currentPattern = (base != null ? base : "") + (base != null ? " " : "") + iteminTree.item; //(true:false)
            System.out.println("currentPattern: "+currentPattern+" gain: "+gainofCurrentPattern);
            System.out.println("=================================");
            if (gainofCurrentPattern <= maxMGT)
            {
            	frequentPatterns.put(currentPattern, gainofCurrentPattern); //hashmap 
            	System.out.println("erasable:"+currentPattern+" gain:"+gainofCurrentPattern);
            
            	if (!iteminTree.item.equals(headerTable.firstElement().item)) //刪除排在header table後面item的所有結點
            	{
            		int i = 0;
            		while(!headerTable.get(i).item.equals(iteminTree.item))
            		{
        			/*
        			FPtree deleteNode = headerTable.get(i);
        			while (deleteNode.next != null)
        			{
        				deleteNode = deleteNode.next;
        				deleteNode.parent.children.removeElement(deleteNode);
        			}
        			headerTable.get(i).next = null;
        			*/
        			deleteItem.add(headerTable.get(i).item);
        			i++;
            		}
            	}
            	for (String delete : deleteItem) {
                	System.out.println("deleteItem: " + delete);
                	}
            	
            	
            	Vector<FPtree> conditional_headerTable = new Vector<FPtree>();
            	Set<String> item_headerTable = new HashSet<String>();
            	outer:for (int i=dataClone.size()-1;i>=0;i--)
            	{
            		for (int j=dataClone.get(i).size()-1;j>=0;j--)
            		{
            			if (deleteItem.contains(dataClone.get(i).get(j)))
            			{
            				dataClone.get(i).remove(j);
            			}
            			else if (dataClone.get(i).get(j).equals(iteminTree.item))
            			{
            				//dataClone.get(i).remove(j);
            				dataClone.remove(i);
            				profitClone.remove(i);
            				//profitClone.add(i, 0);
            				continue outer;
            			}
            			else
            			{
            				item_headerTable.add(dataClone.get(i).get(j));
            			}
            		}
            	}
                for (String itemsforTable : item_headerTable) { //將由大到小的item加入header
                    //System.out.println(itemsforTable);
                	conditional_headerTable.add(new FPtree(itemsforTable));
                }
            	FPtree conditional_eptree = conditional_eptree_constructor(dataClone, profitClone, conditional_headerTable);
            	Collections.sort(conditional_headerTable, new frequencyComparitorinHeaderTable(MGT));
            	if (!conditional_eptree.children.isEmpty()) {
                    FPgrowth(dataClone, profitClone, conditional_eptree, currentPattern, gainofCurrentPattern, conditional_headerTable, frequentPatterns);
                }
            
            }
        	
        	//System.out.println("iteminTree: " + iteminTree.item);
        }
    }

    private void print() throws FileNotFoundException {
        /*
        Vector<String> sortedItems = new Vector<String>();
        sortedItems.add("null");
        frequentPatterns.put("null", 0);
        for (String item : frequentPatterns.keySet()) {
            int count = frequentPatterns.get(item);

            int i = 0;
            for (String listItem : sortedItems) {
                if (frequentPatterns.get(listItem) < count) {
                    sortedItems.add(i, item);
                    break;
                }
                i++;
            }
        }
         * 
         */
        Formatter output = new Formatter("a.txt");
        for (String frequentPattern : frequentPatterns.keySet()) {
        	System.out.println(frequentPattern+" "+frequentPatterns.get(frequentPattern));
            output.format("%s\t%d\n", frequentPattern,frequentPatterns.get(frequentPattern)); //%s字串, \t:tab, %d十進制整數,\n換行
        }
        output.close();
    }
}
