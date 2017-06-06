package texmining.tools;

import java.awt.EventQueue;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
//import java.awt.SystemColor;

public class Main2 {

	private JFrame frmTextMining;

	private File file[];
	private static String filePath;
	private static ArrayList<String> tokens = new ArrayList<String>();
	private ArrayList<String> result_from_swr = new ArrayList<String>();
	private ArrayList<String> result_from_stm = new ArrayList<String>();
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private ArrayList<String>[] docs;
	private ArrayList<String>[] inputForJaccard;
	private Multiset<Integer> finalFrequentItems = HashMultiset.create();

	/**
	 * Launch the application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					Main2 window = new Main2();
					window.frmTextMining.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTextMining = new JFrame();
		frmTextMining.setTitle("Text Mining");
		frmTextMining.setBounds(100, 100, 450, 300);
		frmTextMining.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTextMining.getContentPane().setLayout(
				new MigLayout("", "[][][grow]", "[grow 50][][grow][][]"));

		JLabel lblFiles = new JLabel("Input Files");
		frmTextMining.getContentPane().add(lblFiles, "cell 0 0");

		JScrollPane scrollPane = new JScrollPane();
		frmTextMining.getContentPane().add(scrollPane, "cell 2 0,grow");

		final JTextArea textArea = new JTextArea();
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
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {slider.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "0."+slider.getValue(), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 255)));
			}
		});
		slider.setMinorTickSpacing(1);
		slider.setToolTipText("");
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(10);
		slider.setMinimum(30);
		slider.setMaximum(90);
		slider.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "0."+50, TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 255)));
		slider.setBackground(new Color(255, 250, 250));
		java.util.Hashtable<Integer,JLabel> lableTable = new java.util.Hashtable<Integer,JLabel>();
		lableTable.put(new Integer(90),  new JLabel("0.9"));
		lableTable.put(new Integer(80),  new JLabel("0.8"));
		lableTable.put(new Integer(70),  new JLabel("0.7"));
		lableTable.put(new Integer(60),  new JLabel("0.6"));
		lableTable.put(new Integer(50),  new JLabel("0.5"));
		lableTable.put(new Integer(40),  new JLabel("0.4"));
		lableTable.put(new Integer(30),  new JLabel("0.3"));
		slider.setLabelTable(lableTable);
		slider.setPaintLabels(true);
		
		frmTextMining.getContentPane().add(slider, "cell 2 1,alignx left,aligny center");

		JLabel lblOutput = new JLabel("output");
		frmTextMining.getContentPane().add(lblOutput, "cell 0 2,alignx center");

		JScrollPane scrollPane_1 = new JScrollPane();
		frmTextMining.getContentPane().add(scrollPane_1, "cell 1 2 2 1,grow");
		
		final JButton btnRun = new JButton("Run");

		final JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
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

		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long start = System.currentTimeMillis();
				int progress=0;
			progressBar.setValue(progress);
			lblNewLabel_1.setText("Tokenizing");
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
					progress++;
					progressBar.setValue(progress);
					lblNewLabel_1.setText("Stopword Removal");
					
					StopWordRemoval swr = new StopWordRemoval(tokens);
					result_from_swr = swr.stopWordRemoval();
					textArea_1.append(" after stop word removal ::: "
							+ result_from_swr.toString() + "\n\n\n");
					progress++;
					progressBar.setValue(progress);
					lblNewLabel_1.setText("Stemming");
					Stemmer stm = new Stemmer();
					result_from_stm = stm.stemming(result_from_swr);
					textArea_1.append(" after stemming ::: "
							+ result_from_stm.toString() + "\n\n\n");
					progress++;
					progressBar.setValue(progress);
					lblNewLabel_1.setText("Input to Apriori");
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
				}
				Apriori apriori = new Apriori(inputForApriori2,support);
				finalFrequentItems = apriori.getFrequentItemSet();
				textArea_1.append(finalFrequentItems.elementSet() + "\n\n");
				progress++;
				progressBar.setValue(progress);
				lblNewLabel_1.setText("Jaccard Processing");
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
				}
				Jacard jac = new Jacard(inputForJaccard, file.length);
				jac.jacard();
				float[][] jcbMatrix = new float[file.length][file.length];
				jcbMatrix = jac.getJaccardMatrix();
				for (int i = 0; i < (jcbMatrix.length); i++) {
					for (int j = 0; j < (jcbMatrix.length); j++) {
						textArea_1.append(jcbMatrix[i][j] + "    ");
					}
					textArea_1.append("\n\n");
				}
				
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
				long end = System.currentTimeMillis();
				progress++;
				progressBar.setValue(progress);
				lblNewLabel_1.setText("Complete"+"Total execution time :" + (end - start));
				
								
				System.err.println("Total execution time :" + (end - start));
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
}
