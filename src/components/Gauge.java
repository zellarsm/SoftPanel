package components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Gauge {
	private String name;
	private int pointerLocation, gaugeNum;
	private JLabel pointerLabel;
	
	public Gauge(String name, int gaugeNum, int pointerLocation) {
		this.name = name;
		this.gaugeNum = gaugeNum;
		this.pointerLocation = pointerLocation;
	}
	
	public String getName() {
		return this.name;
	}

	public int getPointerLocation() {
		return this.pointerLocation;
	}
	public void setPointerLocation(int percentage) {
		this.pointerLocation = percentage;
	}
	public JLabel getPointerLabel() {
		return this.pointerLabel;
	}
	public void createPointerLabel(ImageIcon p) {
		this.pointerLabel = new JLabel(p);
		this.pointerLabel.setBounds(3, 110-p.getIconHeight()/2, p.getIconWidth(), p.getIconHeight());
	}
}