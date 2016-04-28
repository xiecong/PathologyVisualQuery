package shapesketch;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import weka.gui.hierarchyvisualizer.HierarchyVisualizer;
import cluster.Cluster;
import database.ShapeSketchData;
//mainframe for UI
public class MainFrame {
	ShapeSketchData data = new ShapeSketchData();
	HierarchyVisualizer visualizer;
	public void addComponentsToPane(Container pane) {

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		JPanel buttonpanel = new JPanel();
		final JLabel statuslabel = new JLabel("Ready");
		pane.add(buttonpanel, BorderLayout.PAGE_START);
		JButton sketchbutton = new JButton("newsketch");
		sketchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.clearSketch();
			}
		});
		buttonpanel.add(sketchbutton, BorderLayout.CENTER);
		JButton searchbutton = new JButton("search");
		buttonpanel.add(searchbutton, BorderLayout.CENTER);
		searchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.matchShape();
				statuslabel.setText(data.getResult().size()+" results found");
				Cluster cluster = new Cluster();
				cluster.clustering(30, data.getResult());
				try {
					visualizer = new HierarchyVisualizer(cluster.clusterer.graph());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		SketchCanvas sk = new SketchCanvas(data);
		pane.add(sk, BorderLayout.CENTER);// BorderLayout.LINE_START);
		sk.init();
		sk.setPreferredSize(new Dimension(800, 800));

		JPanel statuspanel = new JPanel();
		pane.add(statuspanel, BorderLayout.PAGE_END);
		statuspanel.add(statuslabel);

		ResultCanvas rc = new ResultCanvas(data);
		pane.add(rc, BorderLayout.LINE_END);
		rc.init();
		rc.setPreferredSize(new Dimension(200, 800));
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
		frame.setPreferredSize(new Dimension(1000, 800));
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
