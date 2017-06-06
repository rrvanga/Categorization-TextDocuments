package texmining.tools;

import java.util.ArrayList;
import java.util.Arrays;

public class Jacard {
	private ArrayList<String>[] al;
	private int docs_size;
	ArrayList<Float> value_list = new ArrayList<Float>(); // holds minimum
															// jacard
															// coefficient
															// values
	ArrayList<ArrayList<Integer>> list;
	ArrayList<Integer> rows;
	ArrayList<Integer> temprcInd = new ArrayList<Integer>(); // holds all rows
																// and columns
																// which needs
																// to be
																// processed
																// further
	ArrayList<Integer> temp1 = new ArrayList<Integer>(); // holds the (r,c)
															// indexes which are
															// processed and
															// need not to
															// process in the
															// next pass
															// by deleting from
															// temicIndex
	MyList cluster = new MyList();
	Boolean visited[] ;
	
	int leftOutCluster = 999
			;
	Jacard(ArrayList<String>[] al, int docs_size) {
		this.al = al;
		this.docs_size = docs_size;
	}
	public float jacard_matrix[][] = new float[docs_size][docs_size];
	public void jacard() {
		float tot_count = 0;
		float value = 0;
		float jacard_matrix[][] = new float[docs_size][docs_size];
		int count = 0, i, j;
		for (i = 0; i < docs_size; i++) {
			for (j = i + 1; j < docs_size && j > i; j++) {
				for (String s : al[i]) {
					if (al[j].contains(s))
						count++;
				}
				tot_count = al[i].size() + al[j].size();
				value = (tot_count - count) / tot_count;
				if (value != 0.0 && value_list.contains(value) == false)
					value_list.add(value);
				jacard_matrix[i][j] = value;
				tot_count = 0;
				value = 0;
				count = 0;
				System.out.println();
			}
		}
		this.jacard_matrix = jacard_matrix;
		java.util.Collections.sort(value_list);
		System.out.println("Sorted Array is " + value_list.toString());
		for (i = 0; i < docs_size; i++) {
			for (j = 1; j < docs_size; j++) {
				System.out.format(" %.3f", jacard_matrix[i][j]);
			}
			System.out.println();
		}

		for (i = 0; i < docs_size; i++)
			temprcInd.add(i);

		for (float num : value_list) { // for each minimum value in the jaccard
										// matrix
			if (!temprcInd.isEmpty()) {
				list = new ArrayList<ArrayList<Integer>>();
				System.out.println("for minimum value = " + num + "\n");
				for (int i1 : temprcInd) {
					for (int j1 : temprcInd) {
						if (num == jacard_matrix[i1][j1]) {
							System.err.println("****found  " + num + " at "
									+ (i1 + 1) + " --- " + (j1 + 1));
							rows = new ArrayList<Integer>();
							rows.add(i1 + 1);
							rows.add(j1 + 1);
							list.add(rows);
							if (!temp1.contains(i1) || !temp1.contains(j1)) {
								temp1.add(i1);
								temp1.add(j1);
							}
						}
						j1++;
					}
					i1++;
				}
				if (temprcInd.size() == 1) {
					leftOutCluster = temprcInd.get(0);
					System.err.println("left out cluster is ["
							+ (leftOutCluster + 1) +" ]");
					break;
				}
				if (!temp1.isEmpty()) { // after checking for single min value
										// removing all rows/columns for next
										// pass
					for (int i2 : temp1) {
						temprcInd.remove((Object) i2);
					}
				}
				if (!list.isEmpty())
					System.out
							.println(" printing row and columns for minimum value "
									+ num + " ----- > " + list + "\n");
				visited = new Boolean[list.size()];
				Arrays.fill(visited, Boolean.FALSE);
				int currentIndex = 0;
				for (ArrayList<Integer> tempList : list) {
					cluster.add(tempList);
					currentIndex++;
					visited[list.indexOf(tempList)] = Boolean.TRUE;
					ArrayList<Integer> tempList2 ;
					for (int number : tempList) {					
						for (int i1 = list.size(); i1 >= 0; i1--) {
							tempList2 = new ArrayList<Integer>(); 							
							if( (tempList2 = getNextList(currentIndex)) == null){
								break;
								}							
							for (int number2 : tempList2) {
								if (number == number2) {
									cluster.add(tempList2);
									visited[list.indexOf(tempList2)] = Boolean.TRUE;
								}
							}
						}
					}
				}
				cluster.printList();				
			}
		}
	cluster.addLeftOutCluster( (leftOutCluster+1) );
	}

	public ArrayList<Integer> produceCluster(
			ArrayList<ArrayList<Integer>> arrayList) {
		return null;

	}

	public ArrayList<Integer> getNextList(int currentIndex) {
		for(int count = currentIndex ; count < list.size(); count++ ){
			if( visited[count] == Boolean.FALSE ){
				return list.get(count);
			}
		}
		return null;

	}

	public Boolean isListFinished() {
		for (int i = 0; i < visited.length; i++) {
			if (!visited[i])
				return false;
		}
		return true;

	}
	public float[][] getJaccardMatrix(){
		return this.jacard_matrix;
		
	}
}
