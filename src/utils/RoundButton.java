package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

public class RoundButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Color color; // Default to black
	private int buttonNum;
	
	public RoundButton(int buttonNum, int diameter) {
		super("");
		this.buttonNum = buttonNum;
		Dimension size = getPreferredSize();
		size.width = size.height = diameter;
		setPreferredSize(size);
		setFocusable(false);
		setContentAreaFilled(false);
	}
	
	protected void paintComponent(Graphics g) {
		/*if (getModel().isArmed()) {
			g.setColor(Color.RED);
		}
		else {
			g.setColor(Color.GREEN);
		}
		
	*/	
		g.setColor(this.color);
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
		
		super.paintComponent(g);
	}
	
	protected void paintBorder(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}
	
	Shape shape;
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		
		return shape.contains(x, y);
	}
	
	public void setButtonColor(Color color) {
		this.color = color;
	}
	
	public int getButtonNum() {
		return this.buttonNum;
	}
}











