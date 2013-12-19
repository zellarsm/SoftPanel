package my.softpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

import actionlisteners.ExitSoftPanel;
import components.Led;

public class SoftPanelUI extends JPanel {
	private static JLayeredPane[] gPane;
	private static JLabel[] gP, sL, sLedLabel; // gP = gauge pointer, sL = switch lever
	private static ImageIcon[] lever;
	private Led[][] ledArray;
	private static JLabel[][] ledLabel;
	private static boolean guiBuilt;
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
    public SoftPanelUI() {
    	
    	// Create and initialize components.
    	setupLEDs();
    	
    	// Setup the user interface
    	
        JPanel lightboxPanel = setupLedBoxPanel();
        JPanel gaugePanel = setupGaugePanel();
        setupButtonPane();
        JPanel switchPanel = setupSwitchPanel();
        
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lightboxPanel);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        panel.add(switchPanel);
        
        c.fill = GridBagConstraints.HORIZONTAL;
       // c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 0;
        panel.add(gaugePanel);
        
        
        
        
        /*
        GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lightboxPanel)
						.addComponent(switchPanel))
				.addComponent(gaugePanel)
			);
		layout.setVerticalGroup(layout.createSequentialGroup()
						.addComponent(lightboxPanel)
						.addComponent(gaugePanel)
				.addComponent(switchPanel)
			);
		*/
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize(new Dimension(screen.width - 100, screen.height - 100));
		add(scrollPane);
    }
    
    private void setupLEDs() {
    	ledArray = new Led[3][9];
    	ledLabel = new JLabel[3][9];
    	
    	// Create leds and labels used to represent them.
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<9; j++) {
    			ledArray[i][j] = new Led("Test", i*9 + j, i, j, 0); // The led object
    			ledLabel[i][j] = new JLabel(ledArray[i][j].getName()); // The JLabel used to represent the led
    			ledLabel[i][j].setOpaque(true); // Allows coloring of label backgrounds
    			ledLabel[i][j].setBackground(new Color(157, 162, 149));
    			ledLabel[i][j].setHorizontalAlignment(SwingConstants.CENTER);
    			ledLabel[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    		}
    	}
    }
    
	private JPanel setupLedBoxPanel() {
		// Main container
		JPanel lightbox = new JPanel();
		lightbox.setPreferredSize(new Dimension(608,176));
		lightbox.setOpaque(true);
		lightbox.setBackground(Color.BLACK);
		lightbox.setLayout(new GroupLayout(lightbox));
		
		// Container for the lights
		JPanel ledBoxPanel = new JPanel();
		ledBoxPanel.setPreferredSize(new Dimension(568,136));
		ledBoxPanel.setLayout(new GridLayout(3,9));
		
		// Container for the row numbers
		JPanel rowPanel = new JPanel();
		rowPanel.setPreferredSize(new Dimension(15,136));
		rowPanel.setLayout(new GridLayout(3,1));
		
		// Container for the column numbers
		JPanel columnPanel = new JPanel();
		columnPanel.setPreferredSize(new Dimension(568,15));
		columnPanel.setLayout(new GridLayout(1,9));

		// Add led labels to panel
		for (int i=0; i<3; i++)
			for (int j=0; j<9; j++)
				ledBoxPanel.add(ledLabel[i][j]);
		
		// Create row labels
		JLabel[] rL = new JLabel[3];
		rL[0] = new JLabel("A");
		rL[1] = new JLabel("B");
		rL[2] = new JLabel("C");

		// Add row labels to row panel
		for (int i=0; i<3; i++) {
			rL[i].setHorizontalAlignment(SwingConstants.CENTER);
			rowPanel.add(rL[i]);
		}
		
		// Create column labels
		JLabel[] cL = new JLabel[9];
		cL[0] = new JLabel("0");
		cL[1] = new JLabel("1");
		cL[2] = new JLabel("2");
		cL[3] = new JLabel("3");
		cL[4] = new JLabel("4");
		cL[5] = new JLabel("5");
		cL[6] = new JLabel("6");
		cL[7] = new JLabel("7");
		cL[8] = new JLabel("8");
		
		// Add column labels to panel
		for (int i=0; i<9; i++) {
			cL[i].setHorizontalAlignment(SwingConstants.CENTER);
			columnPanel.add(cL[i]);
		}

		// Add all panels to lightbox.
		GroupLayout layout = new GroupLayout(lightbox);
		lightbox.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(rowPanel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(ledBoxPanel)
						.addComponent(columnPanel))
			);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(rowPanel)
						.addComponent(ledBoxPanel))
				.addComponent(columnPanel)
			);
		
		return lightbox;
	}
	
	private JPanel setupGaugePanel() {
		// Create and load all icons.
		ImageIcon pIcon = createImageIcon("/gauge_images/gauge_pointer.gif"); // The orange pointer
		ImageIcon g1Icon = createImageIcon("/gauge_images/watertemp_c_0_100_gauge.gif"); // Water temp gauge
		// Resize them
		pIcon = resizeIcon(pIcon, 12, 6);
		g1Icon = resizeIcon(g1Icon,50,120);
		
		// Create and set up each layered pane.
		gPane = new JLayeredPane[24];
		
		// 1st layer = gauge, 2nd layer = pointer
		JLabel[] gL = new JLabel[24];
		gP = new JLabel[24];
		
		// All gauges will be added to one panel.
		JPanel gaugePanel = new JPanel();
		gaugePanel.setPreferredSize(new Dimension(400,600));
		gaugePanel.setLayout(new GridLayout(5,5));
		
		for (int i=0; i<24; i++) {
			gL[i] = new JLabel(g1Icon);
			gP[i] = new JLabel(pIcon);
			
			gPane[i] = new JLayeredPane();
			gPane[i].setPreferredSize(new Dimension(50,120));
			
			gL[i].setBounds(0,0,g1Icon.getIconWidth(), g1Icon.getIconHeight());
			gP[i].setBounds(3, 110-pIcon.getIconHeight()/2, pIcon.getIconWidth(), pIcon.getIconHeight());
			
			gPane[i].add(gL[i], new Integer(0));
			gPane[i].add(gP[i], new Integer(1));
			gaugePanel.add(gPane[i]); // Add to panel
		}

		return gaugePanel;
	}
	
	private ImageIcon resizeIcon(ImageIcon old, int width, int height) {
		Image pImg = old.getImage();
		Image newimg = pImg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);

		return new ImageIcon(newimg);
	}
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SoftPanelUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
	private JPanel setupSwitchPanel() {
		// Load all icons into an array
		lever = new ImageIcon[19];
		lever[0] = createImageIcon("/switch_images/0.gif"); // Rotated levers
		lever[1] = createImageIcon("/switch_images/5.gif");
		lever[2] = createImageIcon("/switch_images/10.gif");
		lever[3] = createImageIcon("/switch_images/15.gif");
		lever[4] = createImageIcon("/switch_images/20.gif");
		lever[5] = createImageIcon("/switch_images/25.gif");
		lever[6] = createImageIcon("/switch_images/30.gif");
		lever[7] = createImageIcon("/switch_images/35.gif");
		lever[8] = createImageIcon("/switch_images/40.gif");
		lever[9] = createImageIcon("/switch_images/45.gif");
		lever[10] = createImageIcon("/switch_images/50.gif");
		lever[11] = createImageIcon("/switch_images/55.gif");
		lever[12] = createImageIcon("/switch_images/60.gif");
		lever[13] = createImageIcon("/switch_images/65.gif");
		lever[14] = createImageIcon("/switch_images/70.gif");
		lever[15] = createImageIcon("/switch_images/75.gif");
		lever[16] = createImageIcon("/switch_images/80.gif");
		lever[17] = createImageIcon("/switch_images/85.gif");
		lever[18] = createImageIcon("/switch_images/90.gif");
		ImageIcon onOffSwitch = createImageIcon("/switch_images/stop_start_switch.gif"); // The switch itself
		ImageIcon switchBolt = createImageIcon("/switch_images/bolt.gif"); // Bolt to hold lever in place
		
		// Resize them
		for (int i=0; i<19; i++) {
			lever[i] = resizeIcon(lever[i],75,50);
		}
		onOffSwitch = resizeIcon(onOffSwitch,75,130);
		switchBolt = resizeIcon(switchBolt,8,8);
		
		// Create and set up each layered pane.
		JLayeredPane[] sPane = new JLayeredPane[25];
		
		// 1st layer = switch label; 2nd layer = led label, lever, text; 3rd layer = bolt;
		
		// Switch label
		JLabel[] sLabel = new JLabel[25];
		
		
		// Switch bolt label
		JLabel[] sBoltLabel = new JLabel[25]; 
		
		sL = new JLabel[25]; // Switch lever
		sLedLabel = new JLabel[25]; // Switch led label
		//JLabel sTextLabel = new JLabel(); // Switch text label
		
		// All gauges will be added to one panel.
		JPanel switchPanel = new JPanel();
		switchPanel.setPreferredSize(new Dimension(500,700));
		switchPanel.setLayout(new GridLayout(5,5));
		
		
		LevelMouseListener handler = new LevelMouseListener();
		for (int i=0; i<25; i++) {
			sLabel[i] = new JLabel(onOffSwitch);
			sLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			sLabel[i].setBounds(0,0,75,130);
			
			sBoltLabel[i] = new JLabel(switchBolt);
			sBoltLabel[i].setBounds(34,84,8,8);
			
			sL[i] = new JLabel(lever[0]);
			sL[i].setBounds(0,80,75,50);
			
			
			sL[i].addMouseListener(handler); // Add mouse listener for lever movement
			sL[i].addMouseMotionListener(handler);
			sLedLabel[i] = new JLabel();
			sLedLabel[i].setBounds(20, 22, 35, 25);
			sLedLabel[i].setOpaque(true);
			sLedLabel[i].setBackground(Color.GRAY);
			
			sPane[i] = new JLayeredPane();
			sPane[i].setPreferredSize(new Dimension(75,130));
			
			sPane[i].add(sLabel[i], new Integer(0));
			sPane[i].add(sLedLabel[i], new Integer(1));
			sPane[i].add(sL[i], new Integer(2));
			sPane[i].add(sBoltLabel[i], new Integer(3));
			
			switchPanel.add(sPane[i]); // Add to panel
		}

		return switchPanel;
	}
	
	private void setupButtonPane() {
		
	}
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Soft Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JMenuBar menuBar = setupMenuBar(); // Add menu bar
        frame.setJMenuBar(menuBar);
        
        //Create and set up the content pane.
        JComponent newContentPane = new SoftPanelUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    private static JMenuBar setupMenuBar() {
    	// Create the menu bar
    	JMenuBar menuBar = new JMenuBar();
    	
    	// Build the File menu
    	JMenu fileMenu = new JMenu("File");
    	JMenuItem exitItem = new JMenuItem("Exit");
    	fileMenu.add(exitItem);
    	// Build the Window menu
    	JMenu windowMenu = new JMenu("Window");
    	
    	// Build the Help menu
    	JMenu helpMenu = new JMenu("Help");
		
    	menuBar.add(fileMenu);
    	menuBar.add(windowMenu);
    	menuBar.add(helpMenu);
    	
    	// Add listeners for each menu item.
    	exitItem.addActionListener(new ExitSoftPanel());
    	
    	return menuBar;
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
        /****************************************************************************************
         * Testing
         */
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        moveLever(5,0);
        moveLever(6,1);
        moveLever(7,2);
        moveLever(8,3);
        moveLever(9,4);
        moveLever(10,5);
        moveLever(11,6);
        moveLever(12,7);
        moveLever(13,8);
        moveLever(14,9);
        moveLever(15,10);
        moveLever(16,11);
        moveLever(17,12);
        moveLever(18,13);
        moveLever(19,14);
        moveLever(20,15);
        moveLever(21,16);
        moveLever(22,17);
        moveLever(23,18);
        

        // Test LEDs   
        int delay = 1000; //milliseconds
        // Flash led for no reason
       Timer timer = new Timer();
       timer.scheduleAtFixedRate(new TimerTask() {
    	   @Override
    	   public void run() {
    		   if (ledLabel[0][4] != null && ledLabel[1][6] != null && ledLabel[2][1] != null) {
    			   ledLabel[0][4].setBackground(Color.RED);
    		   
	    		   if (ledLabel[1][6].getBackground() == Color.RED) {
	    			   ledLabel[1][6].setBackground(new Color(157, 162, 149));
	    			   ledLabel[2][1].setBackground(new Color(157, 162, 149));
	    	   	   }
	    		   else { 
	    			   ledLabel[1][6].setBackground(Color.RED);
	    			   ledLabel[2][1].setBackground(Color.RED);
	    		   }
    		   }
    	   }
       }, delay, delay);

       Timer timer2 = new Timer();
       
       timer2.scheduleAtFixedRate(new TimerTask() {
    	   int k=0, j=0, flag=1, flag2=1;
    	   
    	   @Override
    	   public void run() {
    		   if (gP[3] != null)
    			   movePointer(3, k);
    		   
    		   if (k == 100)
    			   flag = 0;
    		   else if (k == 0)
    			   flag = 1;
    		   if (flag == 0)
    			   k--;
    		   else k++;
    		   
    		   if (sL[1] != null) {
    			   moveLever(1, j);
    		   }
    		   if (j == 18)
    			   flag2 = 0;
    		   else if (j == 0)
    			   flag2 = 1;
    		   
    		   if (flag2 == 0)
    			   j--;
    		   else j++;
    	   }
       }, delay, 30);
    }
    /**
     * Moves a gauge's pointer to a specified location.
     * 
     * @param pointer: The label of the pointer to move
     * @param percentage: Used for location, 0% is min and 100% is max
     */
    private static void movePointer(int pointer, int percentage) {
    	// Set the new bounds
    	gP[pointer].setBounds(3, 107 - percentage, 12, 6);
    }
    
    /**
     * Moves a switch's lever to specified rotation
     * 
     * @param 
     */
    private static void moveLever(int s, int rotation) {
    	sL[s].setIcon(lever[rotation]);
    	
    	// Change switch's LED color if necessary.
    	if (rotation == 0) {
    		// Switch is off, gray led.
    		sLedLabel[s].setBackground(Color.GRAY);
    	}
    	else if (rotation == 18) {
    		// Switch is on, green led.
    		sLedLabel[s].setBackground(new Color(0,128,0));
    	}
    }
    
    private class LevelMouseListener extends MouseInputAdapter {
    	boolean leverIsSelected, onFlag;
    	int xMax=0; // The farthest x coordinate the user can drag the level to
    	double dx;
    	JLabel switchNum;
    	
		@Override
		public void mousePressed(MouseEvent e) {
			// Determine if user has clicked the lever.
			int position;
			
			// Get which switch is changing.
			switchNum = (JLabel)e.getSource();
			
			// Get lever points.
			Point[] point = getPoints();
			
			System.out.println("X:" + Integer.toString(e.getX()));
			System.out.println("Y:" + Integer.toString(e.getY()));
			
			Point test = new Point(e.getX(), e.getY());
			
			position = (point[1].x - point[0].x)*(test.y - point[0].y) - (point[1].y - point[0].y)*(test.x-point[0].x);
			if (position >= 0 && test.y > 11) {
				position = (point[3].x - point[2].x)*(test.y - point[2].y) - (point[3].y - point[2].y)*(test.x-point[2].x);
				if (position <= 0) {
					System.out.println("Selected");
					leverIsSelected = true;
					
					// Determine x max.
					if (test.y < 14) {
						xMax = test.x + 10;
						dx = 10/18;
					}
				}
				else {
					System.out.println("Not selected");
					leverIsSelected = false;
				}
			} 
			else {
				System.out.println("Not selected");
				leverIsSelected = false;
			}

		}

		private Point[] getPoints() {
			Point[] point = new Point[4];
			
			if (switchNum.getIcon().equals(lever[0])) { // Lever 0
				System.out.println("got35");			
				point[0] = new Point(12,39);
				point[1] = new Point(31,11);
				point[2] = new Point(13,38);
				point[3] = new Point(36,16);	
			}
			else if (switchNum.getIcon().equals(lever[1])) {
				
			}
			else if (switchNum.getIcon().equals(lever[2])) {
							
						}
			else if (switchNum.getIcon().equals(lever[3])) {
				
			}
			else if (switchNum.getIcon().equals(lever[4])) {
				
			}
			else if (switchNum.getIcon().equals(lever[5])) {
				
			}
			else if (switchNum.getIcon().equals(lever[6])) {
				
			}
			else if (switchNum.getIcon().equals(lever[7])) {
				
			}
			else if (switchNum.getIcon().equals(lever[8])) {
				
			}
			else if (switchNum.getIcon().equals(lever[9])) {
				
			}
			else if (switchNum.getIcon().equals(lever[10])) {
				
			}
			else if (switchNum.getIcon().equals(lever[11])) {
				
			}
			else if (switchNum.getIcon().equals(lever[12])) {
				
			}
			else if (switchNum.getIcon().equals(lever[13])) {
				
			}
			else if (switchNum.getIcon().equals(lever[14])) {
				
			}
			else if (switchNum.getIcon().equals(lever[15])) {
				
			}
			else if (switchNum.getIcon().equals(lever[16])) {
				
			}
			else if (switchNum.getIcon().equals(lever[17])) {
				
			}
			else if (switchNum.getIcon().equals(lever[18])) {
				
			}
			return point;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (leverIsSelected) {
				// Update frame when necessary
				//setFrame(e);
				
			}
		}
		private int setFrame(MouseEvent e) {
			if (e.getY() < 14) {
				
				System.out.println("X:" + Integer.toString(e.getX()));
				System.out.println("Y:" + Integer.toString(e.getY()));
				
				if (e.getX() > 42) {
					switchNum.setIcon(lever[18]);
				}
				else if (e.getX() > 41) {
					switchNum.setIcon(lever[16]);
				}
				else if (e.getX() > 40) {
					switchNum.setIcon(lever[14]);
				}
				else if (e.getX() > 39) {
					switchNum.setIcon(lever[12]);
				}
				else if (e.getX() > 38) {
					switchNum.setIcon(lever[10]);
					onFlag = true;
				}
				else if (e.getX() > 36) {
					switchNum.setIcon(lever[8]);
				}
				else if (e.getX() > 34) {
					switchNum.setIcon(lever[6]);
				}
				else if (e.getX() > 32) {
					switchNum.setIcon(lever[4]);
				}
				else if (e.getX() > 30) {
					switchNum.setIcon(lever[2]);
				}
				else if (e.getX() > 29) {
					switchNum.setIcon(lever[0]);
				}
			}
			return -1;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			leverIsSelected = false;
			
			if (onFlag == true)
				switchNum.setIcon(lever[18]);
			else {
				switchNum.setIcon(lever[0]);
			}
			
			
		}
    	
    }

}