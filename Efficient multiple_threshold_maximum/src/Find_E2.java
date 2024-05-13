import java.util.*;

public class Find_E2 {
	ElementaryTable C1 = new ElementaryTable();
	ElementaryTable EI = new ElementaryTable();
	//---------------------------------
	// 輸入參數設定
	double[] maxGainThreshold ;
	int[] transProfit;	  

	//-------------------------------
	// 取得「處理結果」		
	int CK_Num=0;
	int EI_Num=0;
	int maxLength =0;

	//-------------------------------
	//取E2的使用的記憶體
	static	float freeMemory, totalMemory; 		//宣告未使用前記憶體狀況與使用後記憶體狀況，兩者相減就是使用記憶體量！
	static  int diffmemory_int;
	//static	Runtime r = Runtime.getRuntime(); //這是宣告相關參數
	static	String usedStr;   			          //列印「使用記憶體量」字串		
	
	public Find_E2(ElementaryTable EI, int[] transProfit, double[] maxGainThreshold, ElementaryTable C1)
	{
		try{
			//------------------------------------------
			// 設定「初始值」
			this.maxGainThreshold = maxGainThreshold;
			this.transProfit = transProfit;
			this.C1 = C1;
			this.EI = EI;
			
			//System.out.println(" E1.size():"+E1.size());
			    
			//freeMemory = (float) r.freeMemory();//未使用前的記憶體！
							
			//------------------------------------------
			// 01. get the set of E2
			
			//List<char[]> e1Arrays = new ArrayList<>();
			char[] arrays = new char[C1.size()]; //Erasable 1-itemset
			Vector<Character> e1Array = new Vector<>();
			int index=0;
			Set set1 = C1.ElementSet();
     		Iterator iter1 = set1.iterator();
			Element nx1;
			while (iter1.hasNext()) {
				nx1 = (Element)iter1.next();
				arrays[index] = nx1.getKey()[0];
				index++;
				//System.out.println(nx1.getKey()[0] + ":  " + EI.getpidSet(nx1.getKey()[0]));
			}
			
 			QuickSort_Item qsi = new QuickSort_Item();
 			qsi.put(arrays,maxGainThreshold);
 			for (char element : arrays) {
 				//char[] temp = new char[1];
 				//temp[0] = element;
 				//e1Array.add(temp);
 				e1Array.add(element);
 				//System.out.println((int)element);
 			}
 
 			Find_E2(EI,e1Array);
		
	 		//totalMemory = (float) r.totalMemory(); //使用了多少記憶體！ 
	 		//diffmemory_int = ((int) (totalMemory - freeMemory)/1024);
	 		//usedStr = String.valueOf(diffmemory_int)+ "K used";   
			//System.out.println(" Mining Erasable 2-Itemsets  在記憶體方面，共使用: "+usedStr); 
	    }
		catch(Exception ex)
		{
			System.out.println(" Error about finding Erasable 1-Itemsets information (in Find_E2.java):"+ex.getMessage());
	    }		
	}
	//------------------------------------------------------------------	
	//------------------------------------------------------------------	
	//回傳「相關結果資訊」
	
	public int getCK_Num()
	{
	    return CK_Num;	
	}
	
	public int getE2_memory()
	{
	    return diffmemory_int;
	}
	//------------------------------------------------------------------	
	//------------------------------------------------------------------
	// find the set of erasable 1-itemsets from db
	public void Find_E2(ElementaryTable EI, Vector<Character> generate)
	{		
		try{																
			//char[] gen_arr = new char[generate.size()]; //Erasable 1-itemset
			Vector<char[]> next = new Vector<>();
			int k = 2;

			for(int i=0;i<generate.size()-1;i++) {
				double MGT = maxGainThreshold[generate.get(i)];
				next.clear();
				for (int j=i+1;j<generate.size();j++) {
					//System.out.println("["+(int)generate.get(i) + ", " + (int)generate.get(j) + "]");
					int gain = 0;
					HashSet<Integer> dpidset = new HashSet<>(C1.getpidSet(generate.get(j)));
					dpidset.removeAll(C1.getpidSet(generate.get(i)));
					CK_Num++;
					//System.out.println(EI.getpidSet(generate.get(i)) + "  " + EI.getpidSet(generate.get(j))+"  "+dpidset);
					for (int index:dpidset) {
						gain = gain + transProfit[index];
					}
					gain = gain + C1.getValue(generate.get(i));
					if (gain <= MGT) {
						char[] erasable = new char[2];
						erasable[0] = generate.get(i);
						erasable[1] = generate.get(j);
						EI.add(erasable, gain, dpidset);
						next.add(erasable);
					}
				}
				
				if (next.size() > 1) {
					Find_E2(EI, next, MGT,k	);
				}
				
			}					  
				  
			//System.out.println(" C2.size():"+C2.size());
			//CK_Num = C2.size();
		}
		catch(Exception e)
		{
			System.out.println(" Error about finding the erasable 2-itemsets: "+e.getMessage());	
	    }		
	}
	
	
	public void Find_E2(ElementaryTable EI, Vector<char[]> generate, double MGT, int k)
	{		
		try{	
			Vector<char[]> next = new Vector<>();
			k++;
			
			/*
			for(char[] itemset:generate) {
				for (char item:itemset) {
					System.out.print((int)item+", ");
				}
				System.out.println("");
			}
			System.out.println("==============");
			*/
			for(int i=0;i<generate.size()-1;i++) {
				next.clear();
				for (int j=i+1;j<generate.size();j++) {
					int gain = 0;
					//System.out.println(EI.getpidSet(generate.get(i)) + "  " + EI.getpidSet(generate.get(j)));
					HashSet<Integer> dpidset = new HashSet<>(EI.getpidSet(generate.get(j)));
					dpidset.removeAll(EI.getpidSet(generate.get(i)));
					CK_Num++;
					for (int index:dpidset) {
						gain = gain + transProfit[index];
					}
					gain = gain + EI.getValue(generate.get(i));
					if (gain <= MGT) {
						char[] erasable = new char[generate.get(i).length+1];
						System.arraycopy(generate.get(i),0,erasable,0,generate.get(i).length); //arraycopy(來源, 起始索引, 目的, 起始索引, 複製長度)
						System.arraycopy(generate.get(j),generate.get(i).length-1,erasable,erasable.length-1,1);
						//System.out.println(erasable);
						EI.add(erasable, gain, dpidset);
						next.add(erasable);
					}
				}
				
				if (next.size() > 1) {
					Find_E2(EI, next, MGT,k);
				}
				
			}					  
				  
			//System.out.println(" C2.size():"+C2.size());
			//CK_Num = C2.size();
		}
		
		catch(Exception e)
		{
			System.out.println(" Error about finding the erasable 3-itemsets: "+e.getMessage());	
	    }
	    		
	}

  //------------------------------------------------------------------	
  //------------------------------------------------------------------			
  //列印區
  public void Print(ElementaryTable E1)
  {
	  try
	  {
		  System.out.println("----------------------------------");
		  System.out.println(" Print E1 ");
		  System.out.println();
		  //編號(第幾筆)
		  int tid=1;
		  
		  Set set1 = E1.ElementSet();
		  Iterator iter1 = set1.iterator();
		  Element nx1;
		  while(iter1.hasNext())
		      {				
			        nx1 = (Element)iter1.next();
			
			        System.out.println("------------------------------------");
			        System.out.println(" itemset = ");
			        for(int i=0;i<nx1.getKey().length;i++)
			        {
				          System.out.print( (int)nx1.getKey()[i] + " gain = "+nx1.getValue());
			        }
			        System.out.println();
			
			        System.out.println();
			        System.out.println("------------------------------------");
			        System.out.println();
			
			        tid++;
		      }			
		      System.out.println();
		      System.out.println("----------------------------------");
	    }catch(Exception ex)
	    {
		      System.out.println(" Error about printing E1  (in Find_E1.java):"+ex.getMessage());
	    }
  }	

  //------------------------------------------------------------------			
  //列印區 E2
  public void Print_E2(ElementaryTable E2)
  {				
	    try{
		      System.out.println("----------------------------------");
		      System.out.println(" Print E2 ");
		      System.out.println();
		      //編號(第幾筆)
		      int tid=1;			
		
		      Set set1 = E2.ElementSet();
		      Iterator iter1 = set1.iterator();
		      Element nx1;
		
		      while(iter1.hasNext())
		      {
		    	  nx1 = (Element)iter1.next();

		    	  System.out.println("------------------------------------");
		    	  System.out.print(" itemset = ");
			      for(int i=0;i<nx1.getKey().length;i++)
			      {
			    	  System.out.print((int)nx1.getKey()[i]);
			      }
			      System.out.println(" gain = "+nx1.getValue());
			      System.out.println();
			
			      System.out.println();
			      System.out.println("------------------------------------");
			      System.out.println();
			

			      tid++;
		      }			
		      System.out.println();
		      System.out.println("----------------------------------");
		      }
	    
	    catch(Exception ex)
	    {
	    	System.out.println(" Error about printing E1  (in Find_E1.java):"+ex.getMessage());
	    }
  }	
}
