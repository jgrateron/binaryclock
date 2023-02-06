package com.fresco;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;

	public void init() {
		setTitle("Java Binary Clock");
		setSize(912, 612);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		BinaryClock panel = new BinaryClock();
		add(panel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				panel.start();
			}
		});
	}

	public static void main(String[] args) throws InterruptedException {
		var app = new App();
		app.init();
		app.setVisible(true);
	}

}
