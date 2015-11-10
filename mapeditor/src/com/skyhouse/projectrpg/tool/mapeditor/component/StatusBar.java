package com.skyhouse.projectrpg.tool.mapeditor.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = -4582703684012594565L;
	
	private JPanel overallArea;
	private JPanel workStatusArea;
	private JLabel overallLabel;
	private JLabel workStatusLabel;
	
	public StatusBar() {
		setPreferredSize(new Dimension(this.getWidth(), 25));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
		
		prepareOverallArea();
		prepareWorkStatusArea();
	}
	
	private void prepareOverallArea() {
		overallArea = new JPanel();
		overallArea.setPreferredSize(new Dimension(this.getWidth() - 300, getHeight()));
		overallArea.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
		overallArea.setLayout(new BoxLayout(overallArea, BoxLayout.X_AXIS));
		add(overallArea, BorderLayout.CENTER);
		
		overallLabel = new JLabel("");
		overallArea.add(overallLabel);
		
		updateOverall("Overall updated!");
	}
	
	private void prepareWorkStatusArea() {
		workStatusArea = new JPanel();
		workStatusArea.setBackground(Color.GRAY);
		workStatusArea.setPreferredSize(new Dimension(300, getHeight() - 2));
		workStatusArea.setLayout(new BoxLayout(workStatusArea, BoxLayout.Y_AXIS));
		add(workStatusArea, BorderLayout.EAST);
		
		workStatusLabel = new JLabel("");
		workStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		workStatusLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		workStatusArea.add(workStatusLabel);
		
		updateWorkStatus("Work updated!");
	}
	
	public void updateOverall(String message) {
		overallLabel.setText("  " + message);
	}
	
	public void updateWorkStatus(String message) {
		updateWorkStatus(message, Color.BLACK);
	}
	
	public void updateWorkStatus(String message, Color fgcolor) {
		updateWorkStatus(message, fgcolor, Color.GRAY);
	}
	
	public void updateWorkStatus(String message, Color fgcolor, Color color) {
		workStatusLabel.setText(message);
		workStatusLabel.setForeground(fgcolor);
		workStatusArea.setBackground(color);
	}
	
}
