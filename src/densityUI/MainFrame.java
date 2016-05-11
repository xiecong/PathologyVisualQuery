package densityUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import database.DensityData;

public class MainFrame {

	DensityData dt = new DensityData();

	public void addComponentsToPane(Container pane) {
		JPanel buttonpanel = new JPanel();

		JLabel title = new JLabel("Pathology Data");
		
		final JLabel statuslabel = new JLabel("Ready");
		pane.add(buttonpanel, BorderLayout.PAGE_START);
		buttonpanel.add(title);

		JPanel statuspanel = new JPanel();
		pane.add(statuspanel, BorderLayout.PAGE_END);
		statuspanel.add(statuslabel);

		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
		left.setPreferredSize(new Dimension(201,701));
		
		JLabel label = new JLabel("Options");
		pane.add(left, BorderLayout.WEST);
		left.add(label);
		//file structure
		//window size

		JButton switchbutton = new JButton("switch to direction");
		left.add(switchbutton, BorderLayout.CENTER);
		switchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dt.setSketchStatus();
				JButton button = (JButton) e.getSource();
				if (dt.sketchType == 0) {
					button.setLabel("switch to direction");
				} else {
					button.setLabel("switch to density");
				}
			}
		});
		JButton sketchbutton = new JButton("newsketch");
		sketchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dt.clearSketch();
			}
		});
		left.add(sketchbutton, BorderLayout.CENTER);
		JButton searchbutton = new JButton("search");
		left.add(searchbutton, BorderLayout.CENTER);
		
		searchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dt.search();
			}
		});
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		switchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		searchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sketchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
		center.setPreferredSize(new Dimension(501,701));
		right.setPreferredSize(new Dimension(501,701));

		Dimension ocDim = new Dimension(500, 200);
		OverviewCanvas oc = new OverviewCanvas(dt, ocDim);
		oc.setPreferredSize(ocDim);

		Dimension skDim = new Dimension(dt.getSketchSize() * 50 + 2, dt.getSketchSize() * 50 + 40);
		SketchCanvas sk = new SketchCanvas(dt);
		sk.setPreferredSize(skDim);

		Dimension rcDim = new Dimension(dt.getSketchSize() * 50 + 40, 200);
		ResultCanvas rc = new ResultCanvas(dt, rcDim);
		rc.setPreferredSize(rcDim);

		Dimension dcDim = new Dimension(500, 500);
		DetailCanvas dc = new DetailCanvas(dt, dcDim);
		dc.setPreferredSize(dcDim);

		pane.add(center, BorderLayout.CENTER);
		center.add(oc);
		center.add(sk);
		pane.add(right, BorderLayout.EAST);
		right.add(rc);
		right.add(dc);

		sk.init();
		rc.init();
		oc.init();
		dc.init();
	}

	private void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("Density Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		//System.out.println(dt.getSketchSize() * 50);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		MainFrame mainFrame = new MainFrame();

		mainFrame.createAndShowGUI();
	}
}
