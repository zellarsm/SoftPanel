package my.softpanel;

import javax.swing.text.Position;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.NavigationFilter;
import javax.swing.text.NavigationFilter.FilterBypass;

import utils.JTextFieldLimit;
import utils.RequestFocusListener;

import networking.UDPClient;

import actionlisteners.ExitSoftPanel;
import components.Gauge;
import components.Led;
import components.Switch;

public class SoftPanelUI extends JPanel {
	private static ImageIcon[] lever;
	private static Led[][] ledArray;
	private static Gauge[] gaugeArray;
	private static Switch[] switchArray;
	private static Point[][] leverPoints;
	private static ArrayList<JLabel> flashingLEDs;
	private static Hashtable<JLabel, Integer> leverHash;
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static UDPClient clientCon;
	
    public SoftPanelUI() {
    	
    	// Create and initialize components.
    	setupLEDs();
    	setupGauges();
    	setupSwitches();
    	
    	// Setup the user interface
    	
        JPanel lightboxPanel = setupLedBoxPanel();
        JPanel gaugePanel = setupGaugePanel();
        setupButtonPanel();
        JPanel switchPanel = setupSwitchPanel();
        
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(239,231,183));
        panel.add(lightboxPanel);
        panel.add(switchPanel);
        panel.add(gaugePanel);
        /*
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
        
        
        */
        
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
    	
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<9; j++) {
    			// Create the led object
    			ledArray[i][j] = new Led("Test", i*9 + j, i, j, 0); // The led object
    		}
    	}
    }
    
    private void setupGauges() {
    	gaugeArray = new Gauge[24];
    	
    	for (int i=0; i<24; i++) {
    		// Create gauge with pointer location set to 0.
    		gaugeArray[i] = new Gauge("Test", i, 0);
    			
    	}
    }
    
    private void setupSwitches() {
    	switchArray = new Switch[25];
    	
    	for (int i=0; i<25; i++) {
    		// Create switch with status set to off (0).
    		switchArray[i] = new Switch("Test", i, 0);
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
				ledBoxPanel.add(ledArray[i][j].getLabel());
		
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
		JLayeredPane[] gPane = new JLayeredPane[24];
		
		// 1st layer = gauge, 2nd layer = pointer
		JLabel[] gL = new JLabel[24];
		//gP = new JLabel[24];
		
		// All gauges will be added to one panel.
		JPanel gaugePanel = new JPanel();
		gaugePanel.setBackground(new Color(239,231,183));
		gaugePanel.setPreferredSize(new Dimension(400,600));
		gaugePanel.setLayout(new GridLayout(5,5));
		
		for (int i=0; i<24; i++) {
			gL[i] = new JLabel(g1Icon);
			gaugeArray[i].createPointerLabel(pIcon);//gP[i] = new JLabel(pIcon);
			
			gPane[i] = new JLayeredPane();
			gPane[i].setPreferredSize(new Dimension(50,120));
			
			gL[i].setBounds(0,0,g1Icon.getIconWidth(), g1Icon.getIconHeight());
			//gP[i].setBounds(3, 110-pIcon.getIconHeight()/2, pIcon.getIconWidth(), pIcon.getIconHeight());
			
			gPane[i].add(gL[i], new Integer(0));
			gPane[i].add(gaugeArray[i].getPointerLabel(), new Integer(1));
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
		
		// Switch label
		JLabel[] sLabel = new JLabel[25];
		
		// Switch bolt label
		JLabel[] sBoltLabel = new JLabel[25]; 
		
		// All gauges will be added to one panel.
		JPanel switchPanel = new JPanel();
		switchPanel.setPreferredSize(new Dimension(500,700));
		switchPanel.setLayout(new GridLayout(5,5));
		switchPanel.setBackground(new Color(239,231,183));
		
		// Set up points for every lever icon.
		setPoints();
		
		LeverMouseListener handler = new LeverMouseListener();
		
		leverHash = new Hashtable<JLabel, Integer>();
		
		for (int i=0; i<25; i++) {
			sLabel[i] = new JLabel(onOffSwitch);
			sLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			sLabel[i].setBounds(0,0,75,130);
			
			sBoltLabel[i] = new JLabel(switchBolt);
			sBoltLabel[i].setBounds(34,84,8,8);
			
			//switchArray.sL[i] = new JLabel(lever[0]);
			switchArray[i].setLeverLabel(lever[0]);
			switchArray[i].getLeverLabel().addMouseListener(handler); // Add mouse listener for lever movement
			switchArray[i].getLeverLabel().addMouseMotionListener(handler);
			
			// Associate labels with switch numbers by adding them to hash table.
			leverHash.put(switchArray[i].getLeverLabel(), i);
			
			sPane[i] = new JLayeredPane();
			sPane[i].setPreferredSize(new Dimension(75,130));
			
			sPane[i].add(sLabel[i], new Integer(0));
			sPane[i].add(switchArray[i].getLEDLabel(), new Integer(1));
			sPane[i].add(switchArray[i].getLeverLabel(), new Integer(2));
			sPane[i].add(sBoltLabel[i], new Integer(3));
			
			switchPanel.add(sPane[i]); // Add to panel
		}
		
		return switchPanel;
	}
	
	private void setupButtonPanel() {
		
	}
	
	/**
	 * Stores point values for actual lever locations relative to the switch's lever label icon.
	 */
	private void setPoints() {
		int iconWidth = lever[0].getIconWidth(); // Icon width is the same for every switch
		leverPoints = new Point[19][4];
		// Set the points for lever 0
		leverPoints[0][0] = new Point((int)Math.round(.16 * iconWidth), (int)Math.round(.52 * iconWidth));
		leverPoints[0][1] = new Point((int)Math.round(.4133 * iconWidth), (int)Math.round(.1467 * iconWidth));
		leverPoints[0][2] = new Point((int)Math.round(.173 * iconWidth), (int)Math.round(.5067 * iconWidth));
		leverPoints[0][3] = new Point((int)Math.round(.48 * iconWidth), (int)Math.round(.2133 * iconWidth));	
		// Lever 1
		leverPoints[1][0] = new Point((int)Math.round(.16 * iconWidth), (int)Math.round(.52 * iconWidth));
		leverPoints[1][1] = new Point((int)Math.round(.4133 * iconWidth), (int)Math.round(.1467 * iconWidth));
		leverPoints[1][2] = new Point((int)Math.round(.173 * iconWidth), (int)Math.round(.5067 * iconWidth));
		leverPoints[1][3] = new Point((int)Math.round(.48 * iconWidth), (int)Math.round(.2133 * iconWidth));
		// Lever 2
		leverPoints[2][0] = new Point((int)Math.round(.1733 * iconWidth), (int)Math.round(.5067 * iconWidth));
		leverPoints[2][1] = new Point((int)Math.round(.4133 * iconWidth), (int)Math.round(.16 * iconWidth));
		leverPoints[2][2] = new Point((int)Math.round(.2133 * iconWidth), (int)Math.round(.5333 * iconWidth));
		leverPoints[2][3] = new Point((int)Math.round(.4667 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[3][0] = new Point((int)Math.round(.2 * iconWidth), (int)Math.round(.5467 * iconWidth));
		leverPoints[3][1] = new Point((int)Math.round(.4133 * iconWidth), (int)Math.round(.2 * iconWidth));
		leverPoints[3][2] = new Point((int)Math.round(.24 * iconWidth), (int)Math.round(.5467 * iconWidth));
		leverPoints[3][3] = new Point((int)Math.round(.48 * iconWidth), (int)Math.round(.2267 * iconWidth));
		
		leverPoints[4][0] = new Point((int)Math.round(.24 * iconWidth), (int)Math.round(.5733 * iconWidth));
		leverPoints[4][1] = new Point((int)Math.round(.4267 * iconWidth), (int)Math.round(.2 * iconWidth));
		leverPoints[4][2] = new Point((int)Math.round(.28 * iconWidth), (int)Math.round(.5867 * iconWidth));
		leverPoints[4][3] = new Point((int)Math.round(.48 * iconWidth), (int)Math.round(.2267 * iconWidth));
		
		leverPoints[5][0] = new Point((int)Math.round(.28 * iconWidth), (int)Math.round(.5867 * iconWidth));
		leverPoints[5][1] = new Point((int)Math.round(.4267 * iconWidth), (int)Math.round(.2 * iconWidth));
		leverPoints[5][2] = new Point((int)Math.round(.32 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[5][3] = new Point((int)Math.round(.4933 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[6][0] = new Point((int)Math.round(.32 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[6][1] = new Point((int)Math.round(.4267 * iconWidth), (int)Math.round(.2133 * iconWidth));
		leverPoints[6][2] = new Point((int)Math.round(.36 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[6][3] = new Point((int)Math.round(.5067 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[7][0] = new Point((int)Math.round(.36 * iconWidth), (int)Math.round(.6133 * iconWidth));
		leverPoints[7][1] = new Point((int)Math.round(.4267 * iconWidth), (int)Math.round(.2533 * iconWidth));
		leverPoints[7][2] = new Point((int)Math.round(.4 * iconWidth), (int)Math.round(.6133 * iconWidth));
		leverPoints[7][3] = new Point((int)Math.round(.5067 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[8][0] = new Point((int)Math.round(.4 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[8][1] = new Point((int)Math.round(.4533 * iconWidth), (int)Math.round(.2 * iconWidth));
		leverPoints[8][2] = new Point((int)Math.round(.44 * iconWidth), (int)Math.round(.6133 * iconWidth));
		leverPoints[8][3] = new Point((int)Math.round(.52 * iconWidth), (int)Math.round(.2267 * iconWidth));
	
		leverPoints[9][0] = new Point((int)Math.round(.44 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[9][1] = new Point((int)Math.round(.4333 * iconWidth), (int)Math.round(.24 * iconWidth));
		leverPoints[9][2] = new Point((int)Math.round(.48 * iconWidth), (int)Math.round(.6267 * iconWidth));
		leverPoints[9][3] = new Point((int)Math.round(.52 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[10][0] = new Point((int)Math.round(.4667 * iconWidth), (int)Math.round(.6133 * iconWidth));
		leverPoints[10][1] = new Point((int)Math.round(.4667 * iconWidth), (int)Math.round(.2267* iconWidth));
		leverPoints[10][2] = new Point((int)Math.round(.52 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[10][3] = new Point((int)Math.round(.5333 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[11][0] = new Point((int)Math.round(.5067 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[11][1] = new Point((int)Math.round(.4667 * iconWidth), (int)Math.round(.2133 * iconWidth));
		leverPoints[11][2] = new Point((int)Math.round(.56 * iconWidth), (int)Math.round(.6133 * iconWidth));
		leverPoints[11][3] = new Point((int)Math.round(.5467 * iconWidth), (int)Math.round(.2 * iconWidth));

		leverPoints[12][0] = new Point((int)Math.round(.6 * iconWidth), (int)Math.round(.6267 * iconWidth));
		leverPoints[12][1] = new Point((int)Math.round(.48 * iconWidth), (int)Math.round(.2133 * iconWidth));
		leverPoints[12][2] = new Point((int)Math.round(.64 * iconWidth), (int)Math.round(.6133 * iconWidth));
		leverPoints[12][3] = new Point((int)Math.round(.56 * iconWidth), (int)Math.round(.2 * iconWidth));

		leverPoints[13][0] = new Point((int)Math.round(.6267 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[13][1] = new Point((int)Math.round(.52 * iconWidth), (int)Math.round(.2533 * iconWidth));
		leverPoints[13][2] = new Point((int)Math.round(.72 * iconWidth), (int)Math.round(.5733 * iconWidth));
		leverPoints[13][3] = new Point((int)Math.round(.5733 * iconWidth), (int)Math.round(.2 * iconWidth));
		
		leverPoints[14][0] = new Point((int)Math.round(.68 * iconWidth), (int)Math.round(.6 * iconWidth));
		leverPoints[14][1] = new Point((int)Math.round(.52 * iconWidth), (int)Math.round(.2533 * iconWidth));
		leverPoints[14][2] = new Point((int)Math.round(.72 * iconWidth), (int)Math.round(.5733 * iconWidth));
		leverPoints[14][3] = new Point((int)Math.round(.5733 * iconWidth), (int)Math.round(.2 * iconWidth));
		
		leverPoints[15][0] = new Point((int)Math.round(.7067 * iconWidth), (int)Math.round(.56 * iconWidth));
		leverPoints[15][1] = new Point((int)Math.round(.52 * iconWidth), (int)Math.round(.2267 * iconWidth));
		leverPoints[15][2] = new Point((int)Math.round(.76 * iconWidth), (int)Math.round(.5467 * iconWidth));
		leverPoints[15][3] = new Point((int)Math.round(.5867 * iconWidth), (int)Math.round(.2 * iconWidth));
		
		leverPoints[16][0] = new Point((int)Math.round(.76 * iconWidth), (int)Math.round(.56 * iconWidth));
		leverPoints[16][1] = new Point((int)Math.round(.5333 * iconWidth), (int)Math.round(.2267 * iconWidth));
		leverPoints[16][2] = new Point((int)Math.round(.7867 * iconWidth), (int)Math.round(.52 * iconWidth));
		leverPoints[16][3] = new Point((int)Math.round(.6 * iconWidth), (int)Math.round(.2133 * iconWidth));
		
		leverPoints[17][0] = new Point((int)Math.round(.7867 * iconWidth), (int)Math.round(.5333 * iconWidth));
		leverPoints[17][1] = new Point((int)Math.round(.5467 * iconWidth), (int)Math.round(.2267 * iconWidth));
		leverPoints[17][2] = new Point((int)Math.round(.8267 * iconWidth), (int)Math.round(.5067 * iconWidth));
		leverPoints[17][3] = new Point((int)Math.round(.6 * iconWidth), (int)Math.round(.1733 * iconWidth));
		
		leverPoints[18][0] = new Point((int)Math.round(.8267 * iconWidth), (int)Math.round(.5067 * iconWidth));
		leverPoints[18][1] = new Point((int)Math.round(.5467 * iconWidth), (int)Math.round(.2133 * iconWidth));
		leverPoints[18][2] = new Point((int)Math.round(.8533 * iconWidth), (int)Math.round(.4667 * iconWidth));
		leverPoints[18][3] = new Point((int)Math.round(.6 * iconWidth), (int)Math.round(.16 * iconWidth));
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
    
    public static void main(String[] args) throws InvocationTargetException, InterruptedException, IOException {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
        // Create the control objects
        setInitControlStates();
        
        // Get the Power Plant Simulator's IP address from user.
        byte[] address = getServerAddress();

        // Establish connection with Power Plant simulator.
        clientCon = new UDPClient(InetAddress.getByAddress(address));
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        	int transmission = 1;
     	   @Override
     	   public void run() {
     		   try {
				clientCon.transmit("message: " + Integer.toString(transmission) + "|");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     		   transmission++;
     	   }
        }, 1000, 5000);
        // Establish connection with Hard Panel.
        /**
         * Saves the state of the control panel at shut down time.
         */
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	
        	@Override
        	public void run() {
        		// Save the control states to "controls.txt"
        		BufferedWriter writer = null;
        		try {
					writer = new BufferedWriter(new FileWriter("controls.txt"));
					
					// Write LED statuses
					for (int i=0; i<3; i++)
						for (int j=0; j<9; j++)
							writer.write(ledArray[i][j].getStatus() + " ");
					
					writer.write("\r\n"); // New line
					
					// Write gauge statuses
					for (int i=0; i<24; i++) 
						writer.write(gaugeArray[i].getPointerLocation() + " ");
					
					writer.write("\r\n"); // New line
					
					// Write switch states
					for (int i=0; i<25; i++)
						writer.write(switchArray[i].getStatus() + " ");
					
					writer.write("\r\n"); // New line
					
					for (int i=0; i<25; i++)
						writer.write("0" + " ");
					
					writer.write("\r\n"); // New line
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						// Close the writer
						writer.close();
					} catch (Exception e) {
					}
				}
        	}
        });

    }
    
    /**
     * Displays a custom OptionDialog to collect the Power Plant Server's
     * IP address from the user.
     * 
     * @return address: An array of ints representing the four octects of the address.
     */
    private static byte[] getServerAddress() {
    	int listeningField = 1;
    	
    	JPanel panel = new JPanel();
    	JPanel textPanel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	
    	JLabel label = new JLabel("Enter IP Address of Power Plant:");
    	
    	final JTextField field1 = new JTextField(3);
    	final JTextField field2 = new JTextField(3);
    	final JTextField field3 = new JTextField(3);
    	final JTextField field4 = new JTextField(3);
 	
    	// Use navigation filters to automically move cursor to next text field.
        field1.setNavigationFilter(new NavigationFilter() {

            @Override
            public void setDot(FilterBypass fb, int dot, Position.Bias bias) {
                if (dot >= 3) {
                    fb.setDot(0, bias);
                    field1.transferFocus();
                    return;
                }
                fb.setDot(dot, bias);
            }
        });
        field2.setNavigationFilter(new NavigationFilter() {

            @Override
            public void setDot(FilterBypass fb, int dot, Position.Bias bias) {
                if (dot >= 3) {
                    fb.setDot(0, bias);
                    field2.transferFocus();
                    return;
                }
                fb.setDot(dot, bias);
            }
        });
        field3.setNavigationFilter(new NavigationFilter() {

            @Override
            public void setDot(FilterBypass fb, int dot, Position.Bias bias) {
                if (dot >= 3) {
                    fb.setDot(0, bias);
                    field3.transferFocus();
                    return;
                }
                fb.setDot(dot, bias);
            }
        });
        
    	// Focus on the first text field
    	RequestFocusListener rfListener = new RequestFocusListener();
    	field1.addAncestorListener(rfListener);

    	// Limit each text field to 3 character input
    	field1.setDocument(new JTextFieldLimit(3));
    	field2.setDocument(new JTextFieldLimit(3));
    	field3.setDocument(new JTextFieldLimit(3));
    	field4.setDocument(new JTextFieldLimit(3));

    	JLabel period1 = new JLabel(".");
    	JLabel period2 = new JLabel(".");
    	JLabel period3 = new JLabel(".");

    	panel.add(label);
    	
    	textPanel.add(field1);
    	textPanel.add(period1);
    	textPanel.add(field2);
    	textPanel.add(period2);
    	textPanel.add(field3);
    	textPanel.add(period3);
    	textPanel.add(field4);
    	
    	panel.add(textPanel);
    	
    	boolean badInput = true;
    	byte[] address = new byte[4];
    	int loopFlag = -1;
    	
    	// Loop until user enters proper address
    	while (badInput) {
    		loopFlag++;
    		if (loopFlag == 1) {
    			// Inform user they entered a bad number.
    			panel.add(new JLabel("<html><font color='red'>Error: Bad number format</font></html>"));
    		}
    		
    		// Show the dialog.
    		JOptionPane.showMessageDialog(null, panel);
    		
    		// Remove focus listener from necessary text field.
    		if (listeningField == 1)
    			field1.removeAncestorListener(rfListener);
    		else if (listeningField == 2)
    			field2.removeAncestorListener(rfListener);
    		else if (listeningField == 3)
    			field3.removeAncestorListener(rfListener);
    		else
    			field4.removeAncestorListener(rfListener);
    		
    		// Check each text field for proper number format.
    		try {
    			int temp = Integer.parseInt(field1.getText());
    			if (temp > 255) {
    				field1.addAncestorListener(rfListener);
    				continue;
    			}
    			else 
    				address[0] = (byte)temp;
    		} catch (NumberFormatException e) {
    			System.out.println("e1");
    			// User entered a bad number in field 1.
    			field1.addAncestorListener(rfListener);
    			continue;
    		}
    		try {
    			int temp = Integer.parseInt(field2.getText());
    			if (temp > 255) {
    				field2.addAncestorListener(rfListener);
    				continue;
    			}
    			else 
    				address[1] = (byte)temp;
    		} catch (NumberFormatException e) {
    			System.out.println("e2");
    			// User entered a bad number in field 2.
    			field2.addAncestorListener(rfListener);
    			continue;
    		}
    		try {
    			int temp = Integer.parseInt(field3.getText());
    			if (temp > 255) {
    				field3.addAncestorListener(rfListener);
    				continue;
    			}
    			else 
    				address[2] = (byte)temp;
    		} catch (NumberFormatException e) {
    			System.out.println("e3");
    			// User entered a bad number in field 3.
    			field3.addAncestorListener(rfListener);
    			continue;
    		}
    		try {
    			int temp = Integer.parseInt(field4.getText());
    			if (temp > 255) {
    				field4.addAncestorListener(rfListener);
    				continue;
    			}
    			else 
    				address[3] = (byte)temp;
    		} catch (NumberFormatException e) {
    			System.out.println("e4");
    			// User entered a bad number in field 4.
    			field4.addAncestorListener(rfListener);
    			continue;
    		}
    		
    		// If we get here, input is good.
    		badInput = false;
    	}
    	
    	// Return the address numbers
		return address;
    }
    
	/*
    private static void checkHosts(String subnet) throws IOException {
    	InetAddress localHost = InetAddress.getLocalHost();
    	byte[] ip = localHost.getAddress();
    	System.out.println(localHost);
    	for (int i=108; i<=110; i++) {
    		ip[3] = (byte)i;
    		InetAddress address = InetAddress.getByAddress(ip);
    		System.out.println(address);
    		if (address.isReachable(1000)) {
    			// Machine is turned on and can be pinged.
    			System.out.println("0");
    		}
    		else if (!address.getHostAddress().equals(address.getHostName())) {
    			// Machine is known in a DNS lookup
    			System.out.println("1");
    		}
    		else {
    			// The host address and host name are equal, meaning the host name could not be resolved.
    			System.out.println("2");
    		}
    	}
    	

    }*/
    private static void setInitControlStates() throws FileNotFoundException {
        flashingLEDs = new ArrayList<JLabel>(); // Holds which leds are flashing
        
        // Collect previous state information.
		Scanner s = new Scanner(new BufferedReader(new FileReader("controls.txt")));
		
		// Get control states from text file
		String[] ledStates = s.nextLine().split(" ");
		String[] gaugeStates = s.nextLine().split(" ");
		String[] switchStates = s.nextLine().split(" ");
		String[] buttonStates = s.nextLine().split(" ");

		// Display states in GUI
		/*
		 * LEDs
		 */
		for (int i=0; i<3; i++) {
			for (int j=0; j<9; j++) {
				// Get the current state from controls.txt
				int curState = Integer.parseInt(ledStates[i*9 + j]);
				
				// Set LED to on, off, or flashing.
				turnLED(i, j, curState);
			}
		}
		
		// Start timer for flashing LEDs every second.
		Timer flashTimer = new Timer();
		flashTimer.scheduleAtFixedRate(new TimerTask() {
			private boolean on;
	    	@Override
		    public void run() {
	    		// Determine whether LEDs are flashing on or off.
	    		if (flashingLEDs.size() != 0) {
	    			if (flashingLEDs.get(0).getBackground() == Color.RED)
	    				on = true;
	    			else 
	    				on = false;
	    		}
				// Flash each LED in the array list.
				for (int i=0; i<flashingLEDs.size(); i++) {
				   if (on) {
					   // Turn off light
					   flashingLEDs.get(i).setBackground(new Color(157, 162, 149));
				   }
				   else {
					   // Turn on light
					   flashingLEDs.get(i).setBackground(Color.RED);
				   }	
				}
		   }
		}, 1000, 1000);


		/*
		 * Gauges, switches, and buttons
		 */
		for (int i=0; i<25; i++) {
			// Set gauge states.
			if (i != 24) {
				int perc = Integer.parseInt(gaugeStates[i]);
				movePointer(i, perc);
			}
			
			// Set switch states.
			// At this point, all are off.  Turn necessary ones on.
			int status = Integer.parseInt(switchStates[i]);
			if (status == 1) {
				moveLever(i, 18);
			}
		}
		
        /****************************************************************************************
         * Testing
         */
		/*
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
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
    	   int i=0;
    	   
    	   @Override
    	   public void run() {
    		   if (gP[3] != null)
    			   movePointer(2, k);
    		   
    		   if (k == 100)
    			   flag = 0;
    		   else if (k == 0)
    			   flag = 1;
    		   if (flag == 0)
    			   k--;
    		   else k++;
    		   
    		   if (sL[1] != null) {
    			   moveLever(sL[1], j);
    		   }
    		   if (j == 18)
    			   flag2 = 0;
    		   else if (j == 0)
    			   flag2 = 1;
    		   
    		   if (flag2 == 0)
    			   j--;
    		   else j++;
    		   
    		   if (i%10 == 0) {
    			   movePointer(0, i);
    		   }
    		   i++;
    		   if (i==110) {
    			   i=0;
    		   }
    	   }
       }, delay, 30);*/
    }
    
    /**
     * Turns an led's state to on, off, or flashing.
     * 
     * @param col: The column of the LED
     * @param row: The row of the LED
     * @param what: Which state to change it to (0 = off, 1 = on, 2 = flashing)
     */
    private static void turnLED(int row, int col, int what) {
    	// Store object's status value
    	ledArray[row][col].setStatus(what);
		
    	// Display the new state
    	if (what == 0) {
    		// LED off
    		ledArray[row][col].getLabel().setBackground(new Color(157, 162, 149));
    	}
    	else if (what == 1) {
			// LED on
			ledArray[row][col].getLabel().setBackground(Color.RED);
		}
		// If LED is flashing, store it in flashingLED array list.
		else if (what == 2) {
			flashingLEDs.add(ledArray[row][col].getLabel());
			return;
		}
    	
    	// If we get here, LED is no longer flashing. Find and remove it from list.
    	for (int i=0; i<flashingLEDs.size(); i++) {
    		if (flashingLEDs.get(i) == ledArray[row][col].getLabel()) {
    			flashingLEDs.remove(i);
    		}
    	}
    }
    
    /**
     * Moves a gauge's pointer to a specified location.
     * 
     * @param pointer: The label of the pointer to move
     * @param percentage: Used for location, 0% is min and 100% is max
     */
    private static void movePointer(int gauge, int percentage) {
    	// Store object's pointer location
    	gaugeArray[gauge].setPointerLocation(percentage);
    	
    	// Set the new bounds
    	gaugeArray[gauge].getPointerLabel().setBounds(3, 107 - percentage, 12, 6);    
    }
    
    /**
     * Moves a switch's lever to specified rotation
     * 
     * @param 
     */
    private static void moveLever(int num, int rotation) {
    	// Display appropriate icon.
    	switchArray[num].getLeverLabel().setIcon(lever[rotation]);
    	
    	// Change switch's LED color if necessary.
    	if (rotation == 0) {
    		// Switch has turned off.
    		switchArray[num].setStatus(0);
    	}
    	else if (rotation == 18) {
    		// Switch is on, green led.
    		switchArray[num].setStatus(1);
    	}
    }
    
    private class LeverMouseListener extends MouseInputAdapter {
    	boolean leverIsSelected, onFlag, changeFlag;
    	int switchNum;
    	
		@Override
		public void mousePressed(MouseEvent e) {
			leverIsSelected = false; // Default no lever selection.
			changeFlag = false; // Default no change
			
			// Get which switch was clicked.
			JLabel switchLabel = (JLabel)e.getSource();
			switchNum = leverHash.get(switchLabel);
			
			// Get lever points.  
			Point[] point = getPoints();
			
			// Check if user clicked the lever.
			leverIsSelected = checkLeverSelected(point, new Point(e.getX(), e.getY()));		
		}
		
		private boolean checkLeverSelected(Point[] point, Point userPoint) {
			// Determine if user clicked the lever
			int position = (point[1].x - point[0].x)*(userPoint.y - point[0].y) - (point[1].y - point[0].y)*(userPoint.x-point[0].x);
			if (position >= 0 && userPoint.y > 11) {
				position = (point[3].x - point[2].x)*(userPoint.y - point[2].y) - (point[3].y - point[2].y)*(userPoint.x-point[2].x);
				if (position <= 0) {
					// Lever was selected
					return true;
				}
				else {
					// Lever was not selected
					return false;
				}
			} 

			// Lever was not selected
			return false;
			
		}
		private Point[] getPoints() {
			if (switchArray[switchNum].getLeverLabel().getIcon().equals(lever[0])) { 
				// Switch is off, return points of lever 0.		
				return leverPoints[0];
			}
			else {
				// Switch is on, return points of lever 18.
				return leverPoints[18];
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (leverIsSelected) {
				changeFlag = true;
				
				// Update frame when necessary
				Point userPoint = new Point(e.getX(), e.getY());

				for (int i=0; i<19; i++) {
					if (checkLeverSelected(leverPoints[i], userPoint)) {
						// Change the switch's lever icon
						moveLever(switchNum, i);
						
						// Use onFlag for setting switch to either on or off
						if (i<10) onFlag = false;
						else onFlag = true;
						
						return; // Found appropriate icon, no need to continue.
					}
				}		
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			leverIsSelected = false;
			
			if (changeFlag) { // Something was changed, determine switch state
				if (onFlag)
					moveLever(switchNum, 18);
				else {
					moveLever(switchNum, 0);
				}
			}
		}
    }

}