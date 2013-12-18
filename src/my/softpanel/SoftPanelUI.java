package my.softpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

import actionlisteners.ExitSoftPanel;
import components.Led;

public class SoftPanelUI extends JPanel {
	private static JLayeredPane[] gPane;
	private static JLabel[] gP;
	private Led[][] ledArray;
	private static JLabel[][] ledLabel;
	private static boolean guiBuilt;
	
    public SoftPanelUI() {
    	//JPanel panel = new JPanel();
    	// Create and initialize components.
    	setupLEDs();
    	
    	// Setup the user interface
    	
        JPanel lightboxPanel = setupLedBoxPanel();
        JPanel gaugePanel = setupGaugePanel();
        setupButtonPane();
        setupSwitchPane();
        
        GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lightboxPanel)
						.addComponent(gaugePanel))
			);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(lightboxPanel)
				.addComponent(gaugePanel)
			);
		
		//JScrollPane scrollPane = new JScrollPane(panel);
		//scrollPane.setPreferredSize(new Dimension(600, 800));
		//add(scrollPane);
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
		ImageIcon pointerIcon = createImageIcon("/gauge_images/gauge_pointer.gif"); // The orange pointer
		ImageIcon gauge1Icon = createImageIcon("/gauge_images/watertemp_c_0_100_gauge.gif"); // Water temp gauge
		// Resize them
		ImageIcon pIcon = resizeIcon(pointerIcon, 12, 6);
		ImageIcon g1Icon = resizeIcon(gauge1Icon,50,120);
		
		// Create and set up each layered pane.
		gPane = new JLayeredPane[24];
		
		// 1st layer = gauge, 2nd layer = pointer
		JLabel[] gL = new JLabel[24];
		gP = new JLabel[24];
		
		// All gauges will be added to one panel.
		JPanel gaugePanel = new JPanel();
		gaugePanel.setPreferredSize(new Dimension(450,600));
		gaugePanel.setLayout(new GridLayout(5,5));
		gaugePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
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
    
	private void setupButtonPane() {
		
	}
	
	private void setupSwitchPane() {
		
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
    	fileMenu.setMnemonic(KeyEvent.VK_ALT); // Pressing the Alt key accesses this menu
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
    	   int k=0, flag=1;
    	   
    	   @Override
    	   public void run() {
    		   if (gP[3] != null)
    			   movePointer(gP[3], k);
    		   
    		   if (k == 100)
    			   flag = 0;
    		   else if (k == 0)
    			   flag = 1;
    		   if (flag == 0)
    			   k--;
    		   else k++;
    		   
    	   }
       }, delay, delay/30);
    }
    /**
     * Moves a gauge's pointer to a desired location.
     * 
     * @param pointer: The label of the pointer to move
     * @param percentage: Used for location, 0% is min and 100% is max
     */
    private static void movePointer(JLabel pointer, int percentage) {
    	// Set the new bounds
    	pointer.setBounds(3, 107 - percentage, 12, 6);
    }

}