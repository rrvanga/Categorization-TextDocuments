package texmining.Xor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class ModifiedXor {

	public static Boolean[][] xorComparisonTable;
	public static int[][] xorValuesTable;
	public static ArrayList<Multiset<Integer>> finalClusters ;
	public static int counterForAllClusters = 0;
	
	private ArrayList<Integer> xorTableValues = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> list;
	private ArrayList<Integer> rows;
	private ArrayList<ArrayList<Integer>> nlist;
	private ArrayList<Integer> temp1 = new ArrayList<Integer>();
	
	
	private Boolean visited[];
	
	
	public void buildComparisonTable() {

		int row = 0;
		xorComparisonTable = new Boolean[Main2.docs.length][Main2.mFinalFrequentWords
		                                                    .size()];
		for( int i = 0; i<Main2.docs.length;i++ )
		{
			Arrays.fill(xorComparisonTable[i], Boolean.FALSE);
		}

		for (ArrayList<String> al : Main2.docs) {

			for (String word : al) {

				if (Main2.mFinalFrequentWords.contains(word)) {

					xorComparisonTable[row][Main2.mFinalFrequentWords
					                        .indexOf(word)] = Boolean.TRUE;

				}


			}

			row++;

		}

	}

	public void buildXorValuesTable() {

		xorValuesTable = new int[Main2.docs.length][Main2.docs.length];

		int i,j,k;
		for( i=0; i < Main2.docs.length; i++)
		{

			for( j = i+1; j < Main2.docs.length; j++)
			{

				int value = 0;
				for( k = 0; k < Main2.mFinalFrequentWords.size(); k++)
				{

					value += calculateXorValue(xorComparisonTable[i][k], xorComparisonTable[j][k]);

				}

				xorValuesTable[i][j] = value;
				if( !xorTableValues.contains(value))
				{
					xorTableValues.add(value);
				}

			}
		}


	}

	private int calculateXorValue(Boolean boolean1, Boolean boolean2) {

		//		once try the condition using boolean1 && boolean

		if( boolean1 == Boolean.TRUE && boolean2 == Boolean.TRUE)			
			return 1;
		else				
			return 0;
	}

	public void formClusters() {		

		ArrayList<Integer> temprcInd = new ArrayList<Integer>();
		finalClusters = new ArrayList<Multiset<Integer>>();
		Collections.sort(xorTableValues, Collections.reverseOrder());
		
		System.err.println("The sorted xor table values : "+xorTableValues);
		
		for (int i = 0; i < Main2.docs.length; i++)
			temprcInd.add(i);
		
		
		for (int num : xorTableValues) { // for each minimum value in the jaccard

			if (!temprcInd.isEmpty()) {

				list = new ArrayList<ArrayList<Integer>>();

				System.out.println("for maximum value = " + num + "\n");

				for (int i1 : temprcInd) 
				{
					for (int j1 : temprcInd) 
					{
						if (num == xorValuesTable[i1][j1]) 
						{							
							System.err.println("found " + num + " at "
									+ (i1) + " --- " + (j1));
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
//					leftOutCluster = temprcInd.get(0);
//					System.err.println("left out cluster is ["
//							+ (leftOutCluster + 1) + " ]");
					addToFinalCluster(temprcInd.get(0) + 1);
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
						System.out.println("\nnew list size :"+nlist.size() + " -- visited value : "+ visited[list.indexOf(tempList)]);

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
//					cluster.printList(ClusterNumbers);
					System.out.println(" cluster number is empty  "
							+ ClusterNumbers.isEmpty());
					if (isListFinished() == Boolean.TRUE) 
					{
						break;
					} 
				}				
			}
		}
//		cluster.addLeftOutCluster((leftOutCluster + 1));
	}
	public Boolean isListFinished() {
		for (int i = 0; i < visited.length; i++) {
			if (visited[i] == Boolean.FALSE)
				return false;
		}
		return true;
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

}

