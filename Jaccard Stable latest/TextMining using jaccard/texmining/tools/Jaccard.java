package texmining.tools;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Jaccard {
	public static ArrayList<Multiset<Integer>> finalClusters ;
	public static int counterForAllClusters = 0;
	public static ArrayList<Integer> dissimilarDocuments = new ArrayList<>();
	private ArrayList<String>[] al;
	private int docs_size;
	private ArrayList<Float> value_list = new ArrayList<Float>(); // holds
																	// minimum
	private ArrayList<ArrayList<Integer>> list;
	private ArrayList<Integer> rows;
	private ArrayList<Integer> temprcInd = new ArrayList<Integer>(); // holds
																		// all
																		// rows
	private ArrayList<Integer> temp1 = new ArrayList<Integer>(); // holds the
																	// (r,c)
	private ArrayList<ArrayList<Integer>> nlist;
	
	private MyList cluster = new MyList();
	private Boolean visited[];

	private int leftOutCluster = 999;

	Jaccard(ArrayList<String>[] al, int docs_size) {
		this.al = al;
		this.docs_size = docs_size;
	}

	// this is the table holding calculated jaccard values
	private float jacard_matrix[][] = new float[docs_size][docs_size];

	public void jacard() {
		
		finalClusters = new ArrayList<Multiset<Integer>>();

		float tot_count = 0;
		float value = 0;
		float jacard_matrix[][] = new float[docs_size][docs_size];
		int count = 0, i, j;

		// calculating the common words in two different docs

		for (i = 0; i < docs_size; i++) {
			for (j = i + 1; j < docs_size && j > i; j++) {
				for (String s : al[i]) {
					if (al[j].contains(s))
						count++;
				}
				
				tot_count = al[i].size() + al[j].size();

				value = (tot_count - count) / tot_count;
				
				
				if ( isNan(value) ) 
				{
					
					System.err.println("nan occured ");
					value = (float) 0.99;
					if( !dissimilarDocuments.contains( i ))
					dissimilarDocuments.add(i);
					if( !dissimilarDocuments.contains( j ) )
					dissimilarDocuments.add(j);	
					
				} 
				
				
				else if (value == 1.0) {
					if( !dissimilarDocuments.contains( i ) )
					dissimilarDocuments.add(i);
					if( !dissimilarDocuments.contains( j ))
					dissimilarDocuments.add(j);				

				}

				else if (value != 0.0) 
				{
					if(value_list.contains(value) == false)
					{
						
						
						value_list.add(value);
					}
				}

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
			
			if (!temprcInd.isEmpty()) {
				
				list = new ArrayList<ArrayList<Integer>>();
				
				System.out.println("for minimum value = " + num + "\n");
				
				for (int i1 : temprcInd) 
				{
					for (int j1 : temprcInd) 
					{
						if (num == jacard_matrix[i1][j1]) 
						{							
							System.err.println("****found" + num + " at "
									+ (i1 + 1) + " --- " + (j1 + 1));
							rows = new ArrayList<Integer>();
							rows.add(i1 );
							rows.add(j1 );
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
							+ (leftOutCluster + 1) + " ]");
					addToFinalCluster(leftOutCluster + 1);
					break;
				}
				
				if (!temp1.isEmpty()) { // after checking for single min value
					for (int i2 : temp1) {
						temprcInd.remove((Object) i2);
					}
				}
				
				if (!list.isEmpty())
				{
					System.out
							.println(" printing row and columns for minimum value "
									+ num + " ----- > " + list + "\n");
				}
				
				visited = new Boolean[list.size()];				
				Arrays.fill(visited, Boolean.FALSE);
				
				nlist = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> ClusterNumbers = new ArrayList<Integer>();
//				ArrayList<Integer> ClusterNumbers1 = new ArrayList<Integer>();
				/*
				 * forming the clusters
				 */
				
				while ( !isListFinished() ) 
				{
					ClusterNumbers = new ArrayList<>();
					for (ArrayList<Integer> tempList : list) 
					{
						System.out.println("processing ==> " + tempList);
						System.out.println(nlist.size() + " --"+ visited[list.indexOf(tempList)]);
						
						if (visited[list.indexOf(tempList)] == Boolean.FALSE) 
						{
							if (ClusterNumbers.isEmpty()) 
							{
								nlist.add(tempList);
								ClusterNumbers.addAll(tempList);
//								ClusterNumbers1.addAll(tempList);
								visited[list.indexOf(tempList)] = Boolean.TRUE;
							} 
							else if (isListFinished() == Boolean.TRUE) 
							{
								break;
							} 
							else 
							{
								for (int num1 : tempList) 
								{
//									Boolean isOver = Boolean.FALSE;
									System.out.println("comparing [ "+ num1 + " ]  with existing list [ "+ ClusterNumbers + " ]");
									if(ClusterNumbers.contains(num1)) 
									{										
											nlist.add(tempList);											
											ClusterNumbers.addAll(tempList);
											visited[list.indexOf(tempList)] = Boolean.TRUE;
											
//											System.out.println("number1 " + num1 + " == "	+ " number2 " + num2);
//											isOver = Boolean.TRUE;
									}
									
									System.out.println("templist is visited [ "+tempList+" ] ->  "+ visited[list.indexOf(tempList)]);
									
									if( visited[list.indexOf(tempList)] )
										break;
									
									
								}
								if (isListFinished() == Boolean.TRUE) 
								{
									break;
								} 
							}
						}
					}
					addAllToFinalCluster(ClusterNumbers);
					cluster.printList(ClusterNumbers);
					System.out.println(" cluster number is empty  "
							+ ClusterNumbers.isEmpty());
					if (isListFinished() == Boolean.TRUE) 
					{
						break;
					} 
				}
			}
		}
		cluster.addLeftOutCluster((leftOutCluster + 1));
	}

	private boolean isNan(float tot_count) {
		return tot_count != tot_count;
	}

	private void addAllToFinalCluster(ArrayList<Integer> clusterNumbers) {
		System.out.println("counterForAllClusters "+counterForAllClusters);
		System.out.println("inserting" + clusterNumbers);
		Multiset<Integer> temp = HashMultiset.create();
		temp.addAll(clusterNumbers);
		finalClusters.add(temp);
		counterForAllClusters++;
	
	}

	private void addToFinalCluster(int tempVariable) {
		System.out.println("counterForAllClusters "+counterForAllClusters);
		System.out.println("inserting" + tempVariable);
		Multiset<Integer> temp = HashMultiset.create();
		temp.add(tempVariable);
		finalClusters.add(temp);
		counterForAllClusters++;
		
	}

	public ArrayList<Integer> getNextList(int currentIndex) {
		for (int count = currentIndex; count < list.size(); count++) {
			if (visited[count] == Boolean.FALSE) {
				return list.get(count);
			}
		}
		return null;
	}

	public Boolean isListFinished() {
		for (int i = 0; i < visited.length; i++) {
			if (visited[i] == Boolean.FALSE)
				return false;
		}
		return true;
	}

	public float[][] getJaccardMatrix() {
		return this.jacard_matrix;
	}
}