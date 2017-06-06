package texmining.tools;
import java.util.ArrayList;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
public class MyList {
	Multiset<Integer> myCluster = HashMultiset.create();
	public static StringBuffer output = new StringBuffer();
	public static ArrayList<ArrayList<Integer>> finalClusters = new ArrayList<ArrayList<Integer>>();
	public void add(ArrayList<Integer> al) {
		for (int i : al) {
			myCluster.add(i);
		}
	}
	public void printList(ArrayList<Integer> nlist)
	{
		Multiset<Integer> myCluster1 = HashMultiset.create();
		myCluster1.addAll(nlist);
		System.out.println("CLuster ###" +myCluster1.elementSet());
	}
	public void printList() {
		System.err.print("The list items");
		if (!myCluster.isEmpty()) {			
			ArrayList<Integer> temp = new ArrayList<Integer>();
			System.err.println(myCluster.elementSet());
			output.append(" clusters are ");
			output.append(myCluster.elementSet() + "\n");
			temp.addAll(myCluster.elementSet());
			finalClusters.add(temp);
		} else {
			System.err.print(" null ");
		}
		myCluster.clear();
	}
	public void addLeftOutCluster(int leftOutCluster) {
		if (leftOutCluster != 1000) {
			output.append(" left out cluster is [" + leftOutCluster +"]");
		}
	}
}