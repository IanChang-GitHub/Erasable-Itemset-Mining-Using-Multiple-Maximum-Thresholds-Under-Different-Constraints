
public class Cal_Function {

	public Cal_Function () {
		
	}
	
	public double calculate_avg(char[] itemset,double[] MGT, double upperMGT) {
		double constraint = 0;
		for (int i=0; i<itemset.length;i++) {
			constraint += MGT[itemset[i]];
		}
		constraint = constraint/itemset.length;
		
		if (constraint > upperMGT)
			return upperMGT;
		else if (constraint < 0)
			return 0;
		else
			return constraint;
	}
}
