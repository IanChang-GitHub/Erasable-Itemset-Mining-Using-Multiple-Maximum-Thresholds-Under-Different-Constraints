public class QuickSort_Item //for sorted closure
{
	char[] sortedArray;   //參考至要排序的陣列
	double[] maxGainThreshold;
	
	private void swap(int i, int j){
		char temp = sortedArray[i];
		sortedArray[i]=sortedArray[j];
		sortedArray[j]=temp;
	}
	/*
	public char[][] put(char[][] arr){
		String[] temp = new String[arr.length];
		for (int i=0; i<temp.length; i++)
			temp[i] = new String(arr[i]);
		put(temp);
		
		char[][] ans = new char[temp.length][];
		for (int i=0; i<ans.length; i++)
			ans[i] = temp[i].toCharArray(); //字串轉字元陣列
		return ans;
		
	}
	*/
	
	public void put(char[] arr, double[] maxGainThreshold){
		sortedArray = arr;
		this.maxGainThreshold=maxGainThreshold;
		Sort(0,arr.length-1);
		//return trans;
	}
	
	public void Sort(int L_bound, int R_bound){
		if (R_bound-L_bound <= 0 ) 
			return;
    		double p=maxGainThreshold[(int)sortedArray[R_bound]];    //p代表pivot(標準)
    		int l=L_bound;          //l代表左指標
    		int r=R_bound-1;        //r代表右指標
    		while (true) {       //此while迴圈進行切割
      			while (maxGainThreshold[(int)sortedArray[l]] > p)  l++;  //l往右移直到找到比p大或等於
      			while (r>0 && (maxGainThreshold[(int)sortedArray[r]] <= p)) r--; //r往左移直到碰到比p小或等於p的元素
      			if (l>=r) 
      				break;   //若左右指標交叉表示切割完成                      
      			swap(l,r);         //將左右指標l、r所指之元素對調
    		}
    		swap(l,R_bound);        //將p調整至左指標l之位置
    		Sort(L_bound,l-1);  //繼續以遞迴方式處理小於p的部份
    		Sort(l+1,R_bound);  //繼續以遞迴方式處理大於p的部份
    		return;
	}
}
