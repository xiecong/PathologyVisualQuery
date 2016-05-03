package shapeUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cluster.Cluster;
import database.ShapeData;

//mainframe for UI
public class MainFrame {
	ShapeData data = new ShapeData();
	JTextField textField;
	public void addComponentsToPane(Container pane) {
		JPanel buttonpanel = new JPanel();
		// final JLabel statuslabel = new JLabel("Ready");
		pane.add(buttonpanel, BorderLayout.NORTH);
		JButton sketchbutton = new JButton("new sketch");
		sketchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.clearSketch();
			}
		});
		buttonpanel.add(sketchbutton);
		JButton searchbutton = new JButton("search");
		buttonpanel.add(searchbutton);
		searchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.matchShape();
				// statuslabel.setText(data.getResult().size()+" results
				// found");
				Cluster cluster = new Cluster();
				String graph = cluster.shapeClustering(30, data.getResult());
				data.setTree(graph.substring(7) + ":0");
				data.getClusterAverage();
			}
		});

		textField = new JTextField(4);
		JButton clusterbutton = new JButton("set cluster numbers");
		clusterbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.setClusterNum(Integer.parseInt(textField.getText()));
			}
		});

		buttonpanel.add(textField);
		buttonpanel.add(clusterbutton);
		Dimension skDimension = new Dimension(600, 400);
		SketchCanvas sk = new SketchCanvas(data, skDimension);
		pane.add(sk, BorderLayout.LINE_START);
		sk.init();
		sk.setPreferredSize(skDimension);

		Dimension rcDimension = new Dimension(600, 400);
		ResultCanvas rc = new ResultCanvas(data, rcDimension);
		pane.add(rc, BorderLayout.LINE_END);
		rc.init();
		rc.setPreferredSize(rcDimension);

		Dimension ccDimension = new Dimension(1200, 250);
		ClusterCanvas cc = new ClusterCanvas(data, ccDimension);
		pane.add(cc, BorderLayout.PAGE_END);
		cc.init();
		cc.setPreferredSize(ccDimension);

		/*
		 * JPanel statuspanel = new JPanel(); pane.add(statuspanel,
		 * BorderLayout.PAGE_END); statuspanel.add(statuslabel);
		 */

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("ShapeSketchDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		//frame.setSize(new Dimension(1200, 800));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		mainFrame.createAndShowGUI();
	}

}
