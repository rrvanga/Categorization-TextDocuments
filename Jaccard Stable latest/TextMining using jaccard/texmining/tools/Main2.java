package texmining.tools;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.awt.Font;
public class Main2 {
	
	private JFrame frmTextMining;
	private static File file[];
	private static String filePath;
	private static ArrayList<String> tokens = new ArrayList<String>();
	private ArrayList<String> result_from_swr = new ArrayList<String>();
	private ArrayList<String> result_from_stm = new ArrayList<String>();
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private ArrayList<String>[] docs;
	private ArrayList<String>[] inputForJaccard;
	private Multiset<Integer> finalFrequentItems = HashMultiset.create();
	/*
	 * Application starts with main()
	 * here a window is created using Java API
	 */
	public static void main(String[] args) { 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					Main2 window = new Main2();
					Toolkit tk = Toolkit.getDefaultToolkit();  
					int xSize = ((int) tk.getScreenSize().getWidth());  
					int ySize = ((int) tk.getScreenSize().getHeight());  
					window.frmTextMining.setSize(xSize, ySize);
					window.frmTextMining.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*
	 * call to initaliz() method
	 */
	public Main2() {
		initialize();
	}
	
	/*
	 * All UI elements, variables are initialized, and actions for all buttons are defined.
	 */
	
	private void initialize() {
		
		frmTextMining = new JFrame();
		frmTextMining.getContentPane().setBackground(new Color(173, 216, 230));
		frmTextMining.setTitle("Text Mining (using Jaccard)");
		frmTextMining.setBounds(100, 100, 450, 300);
		frmTextMining.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTextMining.getContentPane().setLayout(
				new MigLayout("", "[][][grow]", "[grow 50][][grow][][]"));
		JLabel lblFiles = new JLabel("Input Files");
		frmTextMining.getContentPane().add(lblFiles, "cell 0 0");
		JScrollPane scrollPane = new JScrollPane();
		frmTextMining.getContentPane().add(scrollPane, "cell 2 0,grow");
		final JTextArea textArea = new JTextArea();
		textArea.setRows(2);
		textArea.setBackground(new Color(250, 250, 210));
		scrollPane.setViewportView(textArea);
		JLabel lblNewLabel = new JLabel(" Support Value for Apriori");
		frmTextMining.getContentPane().add(lblNewLabel,
				"cell 1 1,alignx center");
		JLabel lblProgress = new JLabel("Progress:");
		frmTextMining.getContentPane().add(lblProgress, "cell 1 3,alignx center,aligny center");
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMaximum(5);
		frmTextMining.getContentPane().add(progressBar, "cell 2 3,growx,aligny center");
		final	JLabel lblNewLabel_1 = new JLabel("Select files and hit run");
		frmTextMining.getContentPane().add(lblNewLabel_1, "cell 2 4,alignx center,aligny bottom");
			final JSlider slider = new JSlider();
		slider.setMaximum(90);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {slider.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "0."+slider.getValue(), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 255)));
			}
		});
		slider.setMinorTickSpacing(1);
		slider.setToolTipText("");
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(10);
		slider.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "0."+50, TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 255)));
		slider.setBackground(new Color(238, 232, 170));
		java.util.Hashtable<Integer,JLabel> lableTable = new java.util.Hashtable<Integer,JLabel>();
				lableTable.put(new Integer(90),  new JLabel(".9"));
		lableTable.put(new Integer(80),  new JLabel(".8"));
		lableTable.put(new Integer(70),  new JLabel(".7"));
		lableTable.put(new Integer(60),  new JLabel(".6"));
		lableTable.put(new Integer(50),  new JLabel(".5"));
		lableTable.put(new Integer(40),  new JLabel(".4"));
		lableTable.put(new Integer(30),  new JLabel(".3"));
		lableTable.put(new Integer(20),  new JLabel(".2"));
		lableTable.put(new Integer(10),  new JLabel(".1"));
		lableTable.put(new Integer(0),  new JLabel("0"));
		slider.setLabelTable(lableTable);
		slider.setPaintLabels(true);
		frmTextMining.getContentPane().add(slider, "cell 2 1,growx,aligny center");
		JLabel lblOutput = new JLabel("output");
		frmTextMining.getContentPane().add(lblOutput, "cell 0 2,alignx center");
		JScrollPane scrollPane_1 = new JScrollPane();
		frmTextMining.getContentPane().add(scrollPane_1, "cell 1 2 2 1,grow");
		final JButton btnRun = new JButton("Run");
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("Consolas", Font.PLAIN, 14));
		textArea_1.setLineWrap(true);
		textArea_1.setBackground(new Color(250, 250, 210));
		scrollPane_1.setViewportView(textArea_1);
		JButton btnBrowse = new JButton("Browse");
		/*
		 * Action Listener for browse button.
		 * Here input files are taken and the paths are stored in an array.
		 */
		btnBrowse.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				progressBar.setIndeterminate(true);
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.showOpenDialog(frmTextMining);
				file = chooser.getSelectedFiles();
				if ( areTextFiles(file)) {
					for (File f : file)
						textArea.append(f.getAbsolutePath() + "\n");
					docs = new ArrayList[file.length];
					inputForJaccard = new ArrayList[file.length];
					btnRun.setVisible(true);
				} else {
					file = null;
					JOptionPane.showMessageDialog(textArea,
							"choose text files only", "Error",
							JOptionPane.ERROR_MESSAGE);
					btnRun.setVisible(false);
				}
			}
		});
		frmTextMining.getContentPane().add(btnBrowse, "flowx,cell 1 0");
		/*
		 * Actual mining starts in this method.
		 */
		btnRun.addActionListener(new ActionListener() {
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				long start = System.currentTimeMillis();
				double support = (double)slider.getValue()/100;
				System.out.println("Minimum Support for Apriori = " + support);			
				int counter = 0;
				int key_map = 0;
				StringBuffer inputForApriori = new StringBuffer();
				String[] inputForApriori2 = new String[file.length];
				for (File f : file) {
					inputForApriori = new StringBuffer();
					filePath = f.getAbsolutePath();
					textArea_1.append("processing file " + f.getAbsolutePath()
							+ "\n");
					Tokenizer tok = new Tokenizer(filePath);
					while (tok.hasNextToken) {
						tokens.add(tok.nextToken());
					}
					textArea_1.append(" after tokenization ::: "
							+ tokens.toString() + "\n\n\n");
					StopWordRemoval swr = new StopWordRemoval(tokens);
					result_from_swr = swr.stopWordRemoval();
					textArea_1.append(" after stop word removal ::: "
							+ result_from_swr.toString() + "\n\n\n");
					Stemmer stm = new Stemmer();
					result_from_stm = stm.stemming(result_from_swr);
					textArea_1.append(" after stemming ::: "
							+ result_from_stm.toString() + "\n\n\n");
					Multiset<String> words = HashMultiset.create();
					words.addAll(result_from_stm);
					Set<String> uWords = words.elementSet();
					docs[counter] = new ArrayList<String>();
					for (String s : uWords) {
							docs[counter].add(s);
					}
					for (String uWord : uWords) {
						if (!hashMap.containsKey(uWord)) {
							hashMap.put(uWord, key_map);
						}
						key_map++;
						inputForApriori.append(hashMap.get(uWord));
						inputForApriori.append(" ");
					}
					inputForApriori2[counter] = inputForApriori.toString();
					System.err.println("Input for apriori ------> "
							+ inputForApriori.toString());
					System.out.println(hashMap);
					counter++;
					tokens.clear();
					result_from_stm.clear();
					result_from_swr.clear();
				}
				for (ArrayList<String> al : docs) {
					textArea_1.append(" after pre processing remaining words "
							+ al.toString() + "\n\n\n");
				}if(support!=0.0){
				Apriori apriori = new Apriori(inputForApriori2,support);
				finalFrequentItems = apriori.getFrequentItemSet();
				textArea_1.append(finalFrequentItems.elementSet() + "\n\n");}
				int nCounter = 0;
				for (ArrayList<String> arrayList : docs) {
					inputForJaccard[nCounter] = new ArrayList<String>();
					for (String string : arrayList) {
						int key = hashMap.get(string);
						if (finalFrequentItems.contains(key)) {
							inputForJaccard[nCounter].add(string);
						}
					}
					nCounter++;
				}
				System.out.println(" The frequent item set words are::: ");
				nCounter = 0;
				for (ArrayList<String> arrayList : inputForJaccard) {
					System.out.print(nCounter + " ---->> ");
					for (String string : arrayList) {
						System.out.print(string + "\t");
					}
					System.out.println();
				}if(support!=0.0){
				Jaccard jac = new Jaccard(inputForJaccard, file.length);
				jac.jacard();
				float[][] jcbMatrix = new float[file.length][file.length];
				jcbMatrix = jac.getJaccardMatrix();
				for (int i = 0; i < (jcbMatrix.length); i++) {
					for (int j = 0; j < (jcbMatrix.length); j++) {
						textArea_1.append(jcbMatrix[i][j] + "    ");
					}
					textArea_1.append("\n\n");
				}}else
				{Jaccard jac = new Jaccard(docs, file.length);
				jac.jacard();
				float[][] jcbMatrix = new float[file.length][file.length];
				jcbMatrix = jac.getJaccardMatrix();
				for (int i = 0; i < (jcbMatrix.length); i++) {
					for (int j = 0; j < (jcbMatrix.length); j++) {
						textArea_1.append(jcbMatrix[i][j] + "    ");
					}
					textArea_1.append("\n\n");
				}}
				textArea_1.append(MyList.output.toString());
				int mCount = 1;
				for (ArrayList<Integer> temp : MyList.finalClusters) {
					textArea_1.append("cluster " + mCount + " [ ");
					for (int num : temp) {
						textArea_1.append(file[num - 1].getName() + "\t");
					}
					textArea_1.append(" ]\n");
					mCount++;
				}
				printFinalClusters(textArea_1);
				long end = System.currentTimeMillis();
				progressBar.setIndeterminate(false);
				progressBar.setValue(5);
				lblNewLabel_1.setText("Complete"+"Total execution time :" + ((double)(end - start))/1000 + " seconds");
				System.err.println("Total execution time :" + ((double)(end - start)));
			}

			
		});
		frmTextMining.getContentPane().add(btnRun, "cell 1 0");
	}
	public boolean areTextFiles(File[] files) {
		for (File f : files) {
			if ( !f.getAbsolutePath().endsWith(".txt")) {
				System.out.println(f.getAbsolutePath() +"  "+ f.getAbsolutePath().endsWith(".txt"));
				return false;
			}
		}
		return true;
	}
	public static void printFinalClusters(final JTextArea textArea_1) {		
		
		textArea_1.append("\n\n\n\n Final Clusters : \n" );
		
		int fCounter = 1;
		for( Multiset<Integer> temp : Jaccard.finalClusters){
			Jaccard.dissimilarDocuments.removeAll(temp);
			textArea_1.append(fCounter + "  :  "+ temp.elementSet().toString() + "\n");
			fCounter++;
		}
		for(int temp : Jaccard.dissimilarDocuments){
			textArea_1.append( fCounter + "  :  "+ temp + "\n");
			fCounter++;
		}
		fCounter = 1;
		
		
		for( Multiset<Integer> temp : Jaccard.finalClusters){
			Jaccard.dissimilarDocuments.removeAll(temp);
			textArea_1.append( fCounter + "  :  [ ");
			for( int ntemp : temp.elementSet()){
			textArea_1.append( file[ ntemp].getName() +"\t");			
			}
			textArea_1.append( "] \n");
			fCounter++;
		}
		for(int temp : Jaccard.dissimilarDocuments){
			textArea_1.append( fCounter + "  :  "+ file[temp].getName() + "\n");
			fCounter++;
		}
	}
}
