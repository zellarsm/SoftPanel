package components;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import my.softpanel.SoftPanelUI;

public class Switch {
	private String name;
	private int switchNum, status, width, height;
	private JLabel leverLabel, onLightLabel, offLightLabel;
	private ImageIcon greenLight, redLight, offLight;
	
	public Switch(String name, int switchNum, int status, int width, int height) {
		this.name = name;
		this.switchNum = switchNum;
		this.status = status;
		this.width = width;
		this.height = height;
		
		this.leverLabel = new JLabel();		
		this.leverLabel.setBounds(0,(int)(height/1.625),width,(int)(height/2.6));
		
		int lightSize = (int)(height/13);
		
		this.onLightLabel = new JLabel();
		this.onLightLabel.setBounds((int)(width*0.75)-lightSize/2, (int)(height*0.25), lightSize, lightSize);
		
		this.offLightLabel = new JLabel();
		this.offLightLabel.setBounds((int)(width*0.25)-lightSize/2, (int)(height*0.25), lightSize, lightSize);
		//this.ledLabel.setOpaque(true);
		//this.ledLabel.setBackground(Color.GRAY);
		
		this.greenLight = createImageIcon("/switch_images/greenlight.gif");
		this.redLight = createImageIcon("/switch_images/redlight.gif");
		this.offLight = createImageIcon("/switch_images/offlight.gif");
		
		this.greenLight = resizeIcon(this.greenLight, lightSize, lightSize);
		this.redLight = resizeIcon(this.redLight, lightSize, lightSize);
		this.offLight = resizeIcon(this.offLight, lightSize, lightSize);
	}
	
	public String getName() {
		return this.name;
	}

	/**
	 * @return true if switch changed in state, false otherwise
	 */
	public boolean setStatus(int status) {
		boolean changed = true;
		if (this.status == status) {
			// Switch state did not change, will be returning false
			changed = false;
		}

		this.status = status;
		
		if (status == 0) {
			// on light is off, off light is on (lol).
			this.onLightLabel.setIcon(this.offLight);
			this.offLightLabel.setIcon(this.redLight);
		}
		else {
			// on light is on, off light is off.
			this.offLightLabel.setIcon(this.offLight);
			this.onLightLabel.setIcon(this.greenLight);
			//this.ledLabel.setBackground(new Color(0, 128, 0));
		}
		
		return changed;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public JLabel getLeverLabel() {
		return this.leverLabel;
	}
	public void setLeverLabel(ImageIcon icon) {
		this.leverLabel.setIcon(icon);
	}
	
	public JLabel getOnLightLabel() {
		return this.onLightLabel;
	}
	public JLabel getOffLightLabel() {
		return this.offLightLabel;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SoftPanelUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private ImageIcon resizeIcon(ImageIcon old, int width, int height) {
		Image pImg = old.getImage();
		Image newimg = pImg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);

		return new ImageIcon(newimg);
	}
}