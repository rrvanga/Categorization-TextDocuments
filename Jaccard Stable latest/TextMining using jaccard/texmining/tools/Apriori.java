package texmining.tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.StringTokenizer;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
public class Apriori extends Observable {
	private List<int[]> itemsets;
	private List<int[]> finalFrequentItemSet = new ArrayList<int[]>();
	private Multiset<Integer> finalFreqentItems = HashMultiset.create();
	private int numItems;
	private int numTransactions;
	private double minSup;
	private boolean usedAsLibrary = false;
	private String[] transactions;
	public Apriori(String[] transactions, double minSup) {
		this.transactions = transactions;
		this.minSup = minSup;
		configure(transactions);
		go();
	}
	private void configure(String[] args) {
		numItems = 0;
		numTransactions = 0;
		for (String line : args) {
			if (line.matches("\\s*"))
				continue; // be friendly with empty lines
			numTransactions++;
			System.out.println("lines[]  --->" + line);
			StringTokenizer t = new StringTokenizer(line, " ");
			while (t.hasMoreTokens()) {
				int x = Integer.parseInt(t.nextToken());
				if (x + 1 > numItems)
					numItems = x + 1;
			}
		}
		outputConfig();
	}
	private void outputConfig() {
		log("Input configuration: " + numItems + " items, " + numTransactions
				+ " transactions, ");
		log("minsup = " + minSup + "%");
	}
	public void go() {
		long start = System.currentTimeMillis();
		createItemsetsOfSize1();
		int itemsetNumber = 1; // the current itemset being looked at
		int nbFrequentSets = 0;
		while (itemsets.size() > 0) {
			calculateFrequentItemsets();
			if (itemsets.size() != 0) {
				nbFrequentSets += itemsets.size();
				log("Found " + itemsets.size() + " frequent itemsets of size "
						+ itemsetNumber + " (with support " + (minSup * 100)
						+ "%)");
				;
				createNewItemsetsFromPreviousOnes();
			}
			itemsetNumber++;
		}
		long end = System.currentTimeMillis();
		log("Execution time is: " + ((double) (end - start) / 1000)
				+ " seconds.");
		log("Found " + nbFrequentSets + " frequents sets for support "
				+ (minSup * 100) + "% (absolute "
				+ Math.round(numTransactions * minSup) + ")");
		log("Done");
		for (int[] a : finalFrequentItemSet) {
			for (int k = 0; k < a.length; k++) {
				finalFreqentItems.add(a[k]);
			}
		}
		System.err.println(" final freqent items ---> "
				+ finalFreqentItems.elementSet());
	}
	public Multiset<Integer> getFrequentItemSet() {
		return finalFreqentItems;
	}
	private void foundFrequentItemSet(int[] itemset, int support) {
		if (usedAsLibrary) {
			this.setChanged();
			notifyObservers(itemset);
		} else {
			System.out.println(Arrays.toString(itemset) + "  ("
					+ ((support / (double) numTransactions)) + " " + support
					+ ")");
		}
	}
	private void log(String message) {
		if (!usedAsLibrary) {
			System.err.println(message);
		}
	}
	private void createItemsetsOfSize1() {
		itemsets = new ArrayList<int[]>();
		for (int i = 0; i < numItems; i++) {
			int[] cand = { i };
			itemsets.add(cand);
		}
	}
	private void createNewItemsetsFromPreviousOnes() {
		int currentSizeOfItemsets = itemsets.get(0).length;
		log("Creating itemsets of size " + (currentSizeOfItemsets + 1)
				+ " based on " + itemsets.size() + " itemsets of size "
				+ currentSizeOfItemsets);
		finalFrequentItemSet = itemsets;
		HashMap<String, int[]> tempCandidates = new HashMap<String, int[]>(); 
		for (int i = 0; i < itemsets.size(); i++) {
			for (int j = i + 1; j < itemsets.size(); j++) {
				int[] X = itemsets.get(i);
				int[] Y = itemsets.get(j);
				assert (X.length == Y.length);
				int[] newCand = new int[currentSizeOfItemsets + 1];
				for (int s = 0; s < newCand.length - 1; s++) {
					newCand[s] = X[s];
				}
				int ndifferent = 0;
				for (int s1 = 0; s1 < Y.length; s1++) {
					boolean found = false;
					for (int s2 = 0; s2 < X.length; s2++) {
						if (X[s2] == Y[s1]) {
							found = true;
							break;
						}
					}
					if (!found) { // Y[s1] is not in X
						ndifferent++;
						newCand[newCand.length - 1] = Y[s1];
					}
				}
				assert (ndifferent > 0);
				if (ndifferent == 1) {
					Arrays.sort(newCand);
					tempCandidates.put(Arrays.toString(newCand), newCand);
				}
			}
		}
		itemsets = new ArrayList<int[]>(tempCandidates.values());
		if (itemsets.isEmpty() == false)
			finalFrequentItemSet = new ArrayList<int[]>(tempCandidates.values());
		log("Created " + itemsets.size() + " unique itemsets of size "
				+ (currentSizeOfItemsets + 1));
	}
	private void line2booleanArray(String line, boolean[] trans) {
		Arrays.fill(trans, false);
		StringTokenizer stFile = new StringTokenizer(line, " "); // read a line
		while (stFile.hasMoreTokens()) {
			int parsedVal = Integer.parseInt(stFile.nextToken());
			trans[parsedVal] = true; // if it is not a 0, assign the value to
		}
	}
	private void calculateFrequentItemsets() {
		log("Passing through the data to compute the frequency of "
				+ itemsets.size() + " itemsets of size "
				+ itemsets.get(0).length);
		List<int[]> frequentCandidates = new ArrayList<int[]>(); // the frequent
		boolean match; // whether the transaction has all the items in an
		int count[] = new int[itemsets.size()]; // the number of successful
		boolean[] trans = new boolean[numItems];
		for (String line : transactions) {
			line2booleanArray(line, trans);
			for (int c = 0; c < itemsets.size(); c++) {
				match = true; // reset match to false
				int[] cand = itemsets.get(c);
				for (int xx : cand) {
					if (trans[xx] == false) {
						match = false;
						break;
					}
				}
				if (match) { // if at this point it is a match, increase the
					count[c]++;
				}
			}
		}
		for (int i = 0; i < itemsets.size(); i++) {
			if ((count[i] / (double) (numTransactions)) >= minSup) {
				foundFrequentItemSet(itemsets.get(i), count[i]);
				frequentCandidates.add(itemsets.get(i));
			}
		}
		itemsets = frequentCandidates;
	}
}