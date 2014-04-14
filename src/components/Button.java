package components;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import utils.RoundButton;

import my.softpanel.SoftPanelUI;

public class Button {
	private String name;
	private int buttonNum, status, color, width, diameter;
	private RoundButton pushButton;
	private Color MY_GREEN = new Color(27,166,103), MY_DARK_RED = new Color(80,0,0), MY_DARK_GREEN = new Color(0,45,0);
	
	public Button(String name, int buttonNum, int status, int color, int width) {
		this.name = name;
		this.buttonNum = buttonNum;
		this.status = status;
		this.color = color; // 0 = red, 1 = green
		this.width = width;
		this.diameter = (int)(this.width * 0.6);
		
		// Create the pushable part of the button and set it up for display.
		pushButton = new RoundButton(this.buttonNum, this.diameter);
		pushButton.setBounds((int)(this.diameter*0.35), (int)(this.diameter*0.5), this.diameter, this.diameter);
		
		// Turn button off.
		setStatus(0);
	}
	
	
	public void setStatus(int status) {
		this.status = status;
		
		if (status == 0) {
			// Turn button off
			if (this.color == 0) {
				// Button is red
				pushButton.setButtonColor(MY_DARK_RED);
			}
			else {
				// Button is green
				pushButton.setButtonColor(MY_DARK_GREEN);
			}
		}
		else {
			// Turn button on
			if (this.color == 0) {
				// Button is red
				pushButton.setButtonColor(Color.RED);
			}
			else {
				// Button is green
				pushButton.setButtonColor(MY_GREEN);
			}
		}
	}
	
	public String getName() {
		return this.name;
	}
	public int getButtonNum() {
		return this.buttonNum;
	}
	public int getStatus() {
		return this.status;
	}
	public int getWidth() {
		return this.width;
	}
	public int getDiameter() {
		return this.diameter;
	}
	public int getColor() {
		return this.color;
	}
	public RoundButton getPushButton() {
		return this.pushButton;
	}
	public void toggleButton() {
		if (this.status == 0)
			setStatus(1);
		else 
			setStatus(0);
	}


}