package components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Switch {
	private String name;
	private int switchNum, status;
	private JLabel leverLabel, ledLabel;
	
	public Switch(String name, int switchNum, int status) {
		this.name = name;
		this.switchNum = switchNum;
		this.status = status;
		this.leverLabel = new JLabel();		
		this.leverLabel.setBounds(0,80,75,50);
		
		this.ledLabel = new JLabel();
		this.ledLabel.setBounds(20, 22, 35, 25);
		this.ledLabel.setOpaque(true);
		this.ledLabel.setBackground(Color.GRAY);
	}
	
	public String getName() {
		return this.name;
	}

	public void setStatus(int status) {
		this.status = status;
		
		if (status == 0) {
			// LED is off
			this.ledLabel.setBackground(Color.GRAY);
		}
		else {
			// LED is on
			this.ledLabel.setBackground(new Color(0, 128, 0));
		}
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public JLabel getLeverLabel() {
		return this.leverLabel;
	}
	public void setLeverLabel(ImageIcon icon) {
		this.leverLabel.setIcon(icon);
	}
	
	public JLabel getLEDLabel() {
		return this.ledLabel;
	}
	
}