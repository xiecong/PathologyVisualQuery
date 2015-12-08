package density;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame {

	DensityData dt = new DensityData();

	public MainFrame() {
		dt.getEmptyAreaList();
	}

	public void addComponentsToPane(Container pane) {

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		OveviewCanvas oc = new OveviewCanvas(this.dt);
		pane.add(oc, BorderLayout.CENTER);// BorderLayout.LINE_START);
		oc.init();
		oc.setPreferredSize(new Dimension(dt.xWidth, dt.yWidth+40));

	}

	private void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("Density Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		frame.setPreferredSize(new Dimension(dt.xWidth, dt.yWidth+40));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		MainFrame mainFrame = new MainFrame();

		mainFrame.createAndShowGUI();
	}
}
