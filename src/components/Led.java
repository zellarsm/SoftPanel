package components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Led {
	private String name;
	private int cell, row, column, status;
	private JLabel label;
	
	public Led(String name, int cell, int row, int column, int status) {
		this.name = name;
		this.cell = cell;
		this.row = row;
		this.column = column;
		this.status = status;
		
		this.label = new JLabel(name); // The JLabel used to represent the led
		this.label.setOpaque(true); // Allows coloring of label backgrounds
		this.label.setBackground(new Color(157, 162, 149));
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	}
	
	public String getName() {
		return this.name;
	}
	public int getCell() {
		return this.cell;
	}
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRow() {
		return this.row;
	}
	public int getColumn() {
		return this.column;
	}
	
	public JLabel getLabel() {
		return this.label;
	}
}
