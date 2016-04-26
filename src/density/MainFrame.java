package density;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import database.DensityData;
import shapesketch.ResultCanvas;
import shapesketch.SketchCanvas;

public class MainFrame {

	DensityData dt = new DensityData();

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
				dt.clearSketch();
			}
		});
		buttonpanel.add(sketchbutton, BorderLayout.CENTER);
		JButton searchbutton = new JButton("search");
		buttonpanel.add(searchbutton, BorderLayout.CENTER);
		searchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dt.search();
			}
		});

		DensitySketch sk = new DensitySketch(dt);
		pane.add(sk, BorderLayout.WEST);// BorderLayout.LINE_START);
		sk.init();
		sk.setPreferredSize(new Dimension(dt.getSketchSize()*50+2, dt.getSketchSize()*50+40));

		JPanel statuspanel = new JPanel();
		pane.add(statuspanel, BorderLayout.PAGE_END);
		statuspanel.add(statuslabel);

		DensityResult rc = new DensityResult(dt);
		pane.add(rc, BorderLayout.CENTER);
		rc.init();
		rc.setPreferredSize(new Dimension(200, dt.getSketchSize()*50+40));
		
		DetailCanvas dc = new DetailCanvas(dt);
		pane.add(dc, BorderLayout.LINE_END);
		dc.init();
		dc.setPreferredSize(new Dimension(500, 500));

	}

	private void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("Density Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		System.out.println(dt.getSketchSize()*50);
		frame.setPreferredSize(new Dimension(dt.getSketchSize()*50+720, dt.getSketchSize()*50+115));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		MainFrame mainFrame = new MainFrame();

		mainFrame.createAndShowGUI();
	}
}
