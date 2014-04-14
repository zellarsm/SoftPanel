package my.softpanel;

import javax.swing.text.Position;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.NavigationFilter;
import utils.JTextFieldLimit;
import utils.RequestFocusListener;
import utils.RoundButton;

import networking.UDPClient;

import UART.RXTX;
import actionlisteners.ExitSoftPanel;
import components.Button;
import components.Gauge;
import components.Led;
import components.Switch;


public class SoftPanelUI extends JFrame {
	
	// Constants pertaining to label sizes
	private static final int SWITCH_WIDTH = 75, SWITCH_HEIGHT = 130, BUTTON_WIDTH = 75, BUTTON_HEIGHT = 85; 
	
	private static boolean UART_READY = false, HP_INIT = false;
	
	private static ImageIcon[] lever;
	private static Led[][] ledArray;
	private static Gauge[] gaugeArray;
	private static Switch[] switchArray;
	private static Button[] buttonArray;
	private static Point[][] leverPoints;
	private static ArrayList<JLabel> flashingLEDs;
	private static Hashtable<JLabel, Integer> leverHash;
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private String clientString=null, serverString=null;
	
	private static UDPClient clientCon;
	
	// RXTX variables
	static RXTX communicator = null;
	JPanel hardPanel;
    JLabel hpLabel1, hpLabel2, hpLabel3;
    public JComboBox cboxPorts;
    JButton btnConnect, btnDisconnect, btnHelpSync;
    public JLabel conStatus, syncStatus;
    public byte hpReceiveData;
    
    public SoftPanelUI() {
    	initHardPanelComponents();
    	createRXTXObjects();
    	communicator.searchForPorts();

    	// Create and initialize components.
    	setupLEDs();
    	setupGauges();
    	setupSwitches();
    	setupButtons();

    	initComponents();

    }
    
    private void initComponents() {
    	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    	
    	JScrollPane scrollPane = new JScrollPane();
    	JPanel panel = new JPanel();
    	JPanel gsbPanel = new JPanel();
    	JPanel bPanel = new JPanel(); // panel3
    	JPanel gPanel = new JPanel(); // panel4
    	JPanel sPanel = new JPanel(); //
    	JPanel hardPanel = new JPanel();
    	
    	        
        conStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        syncStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        javax.swing.GroupLayout hardPanelLayout = new javax.swing.GroupLayout(hardPanel);
        hardPanel.setLayout(hardPanelLayout);
        hardPanelLayout.setHorizontalGroup(
            hardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hardPanelLayout.createSequentialGroup()
                .addGroup(hardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(conStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(hardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cboxPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(hardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(hardPanelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(syncStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHelpSync)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        hardPanelLayout.setVerticalGroup(
            hardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hardPanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(conStatus)
                .addGap(8, 8, 8)
                .addGroup(hardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(syncStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHelpSync)
                .addGap(2, 2, 2))
        );
/*
 *  
        
         javax.swing.GroupLayout lightboxLayout = new javax.swing.GroupLayout(lightboxPanel);
         lightboxPanel.setLayout(lightboxLayout);
         lightboxLayout.setHorizontalGroup(
             lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGap(0, 432, Short.MAX_VALUE)
         );
         lightboxLayout.setVerticalGroup(
             lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGap(0, 0, Short.MAX_VALUE)
         );
 * 
 * */
        /*
        JPanel lbPanel = new JPanel();
 		JPanel lightboxPanel = setupLedBoxPanel();
        javax.swing.GroupLayout lightboxLayout = new javax.swing.GroupLayout(lbPanel);
        lightboxPanel.setLayout(lightboxLayout);
        lightboxLayout.setHorizontalGroup(
            lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lightboxLayout.createSequentialGroup()
            		
            .addGap(0, 677, Short.MAX_VALUE)
        );
        lightboxLayout.setVerticalGroup(
            lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );
       */
        JPanel lPanel = new JPanel();
        javax.swing.GroupLayout lightboxLayout = new javax.swing.GroupLayout(lPanel);
        JPanel lightboxPanel = setupLedBoxPanel();
        
        lPanel.setLayout(lightboxLayout);
        lightboxLayout.setHorizontalGroup(
            lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lightboxLayout.createSequentialGroup()
                .addGroup(lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(lightboxLayout.createSequentialGroup()
                        .addComponent(hardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lightboxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(lightboxLayout.createSequentialGroup()
                        .addComponent(bPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lightboxLayout.setVerticalGroup(
                lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(lightboxLayout.createSequentialGroup()
                    .addGroup(lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(lightboxLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(hardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(lightboxLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(lightboxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(lightboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(gPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, 0))
            );

        
        
        
        JLayeredPane[] bPane = setupButtonPanel();
        javax.swing.GroupLayout bPanelLayout = new javax.swing.GroupLayout(bPanel);
        bPanel.setLayout(bPanelLayout);
        bPanelLayout.setHorizontalGroup(
            bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bPanelLayout.createSequentialGroup()
                        .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bPane[2], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[4], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[6], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[8], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[10], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[12], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[14], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[20], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[22], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bPane[3], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[5], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[7], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[9], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[11], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[13], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[15], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[23], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPane[21], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(bPanelLayout.createSequentialGroup()
                        .addComponent(bPane[18], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bPane[19], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bPanelLayout.createSequentialGroup()
                        .addComponent(bPane[0], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bPane[1], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bPanelLayout.createSequentialGroup()
                        .addComponent(bPane[16], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bPane[17], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bPanelLayout.setVerticalGroup(
            bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[1], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[0], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[3], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[2], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[4], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[5], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[7], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[6], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[9], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[8], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[10], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[11], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[12], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[13], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[14], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[15], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[17], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[16], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[19], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[18], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[21], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[20], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bPane[22], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPane[23], javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(284, 284, 284))
        );
    	
        JLayeredPane[] sPane = setupSwitchPanel();
        
        javax.swing.GroupLayout sPanelLayout = new javax.swing.GroupLayout(sPanel);
        sPanel.setLayout(sPanelLayout);
        sPanelLayout.setHorizontalGroup(
            sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sPanelLayout.createSequentialGroup()
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(sPanelLayout.createSequentialGroup()
                        .addComponent(sPane[6], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sPane[7], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sPane[0], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(sPanelLayout.createSequentialGroup()
                        .addComponent(sPane[12], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sPane[13], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sPane[14], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(sPanelLayout.createSequentialGroup()
                        .addComponent(sPane[18], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sPane[19], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(sPanelLayout.createSequentialGroup()
                            .addComponent(sPane[8], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sPane[9], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(sPanelLayout.createSequentialGroup()
                            .addComponent(sPane[1], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(sPane[2], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(sPanelLayout.createSequentialGroup()
                        .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sPane[15], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sPane[20], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sPane[16], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sPane[21], javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(sPanelLayout.createSequentialGroup()
                        .addComponent(sPane[3], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(sPanelLayout.createSequentialGroup()
                                    .addComponent(sPane[4], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sPane[5], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sPanelLayout.createSequentialGroup()
                                    .addComponent(sPane[10], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sPane[11], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(sPanelLayout.createSequentialGroup()
                                .addComponent(sPane[22], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sPane[23], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(sPane[17], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        sPanelLayout.setVerticalGroup(
            sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sPanelLayout.createSequentialGroup()
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sPane[0], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[1], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[2], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[3], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[4], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[5], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sPane[11], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[10], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[9], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[8], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[7], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[6], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sPane[17], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[16], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[15], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[14], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[13], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[12], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sPane[18], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[19], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[20], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[21], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[22], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sPane[23], javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        JLayeredPane[] gPane = setupGaugePanel();
        javax.swing.GroupLayout gPanelLayout = new javax.swing.GroupLayout(gPanel);
        gPanel.setLayout(gPanelLayout);
        gPanelLayout.setHorizontalGroup(
            gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gPanelLayout.createSequentialGroup()
                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(gPanelLayout.createSequentialGroup()
                        .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gPanelLayout.createSequentialGroup()
                                .addComponent(gPane[12], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(gPane[13], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(113, 113, 113))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gPanelLayout.createSequentialGroup()
                                .addComponent(gPane[7], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, gPanelLayout.createSequentialGroup()
                                .addComponent(gPane[2], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(gPane[3], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(gPane[4], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(gPane[5], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, gPanelLayout.createSequentialGroup()
                                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(gPane[22], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(gPanelLayout.createSequentialGroup()
                                        .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(gPanelLayout.createSequentialGroup()
                                                .addComponent(gPane[8], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(112, 112, 112))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gPanelLayout.createSequentialGroup()
                                                .addComponent(gPane[14], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(19, 19, 19)
                                                .addComponent(gPane[15], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)))
                                        .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(gPanelLayout.createSequentialGroup()
                                                .addComponent(gPane[9], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(gPane[10], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(gPane[16], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gPane[17], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gPane[11], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gPane[23], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(gPanelLayout.createSequentialGroup()
                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gPane[6], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(gPanelLayout.createSequentialGroup()
                        .addComponent(gPane[0], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(gPane[1], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gPanelLayout.createSequentialGroup()
                        .addComponent(gPane[18], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(gPane[19], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(gPane[20], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(gPane[21], javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        gPanelLayout.setVerticalGroup(
            gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gPane[0], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[1], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[2], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[3], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[4], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[5], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gPane[6], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[7], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[9], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[10], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[11], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[8], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gPane[12], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[13], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[14], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[15], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[16], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[17], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(gPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gPane[18], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[19], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[20], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[21], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[22], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPane[23], javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        
        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(hardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lightboxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(bPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightboxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
         scrollPane.setViewportView(panel);

         javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
         getContentPane().setLayout(layout);
         layout.setHorizontalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(layout.createSequentialGroup()
                 .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
                 .addGap(0, 25, Short.MAX_VALUE))
         );
         layout.setVerticalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(layout.createSequentialGroup()
                 .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                 .addGap(0, 0, Short.MAX_VALUE))
         );

         pack();
    	
    }
    private void createRXTXObjects() {
    	communicator = new RXTX(this);
    }

    public void handleData(int id, byte data) {
    	if (id < 3) {
    		// Hard panel just powered on. Prepare for its input
    		HP_INIT = true;
    		
    		// Getting buttons states
    		for (int i=0; i<8; i++) {
    			if (((data>>i) & 1) == 1) {
    				buttonArray[id*8 + i].setStatus(0);
    			}
    			else {
    				buttonArray[id*8 + i].setStatus(1);
    			}
    			//buttonArray[id*8 + i].setStatus((data>>i) & 1);
    		}
    	}
    	/*
    	else if (id < 6) {
    		// Getting switch states
    		for (int i=0; i<8; i++) {
    			switchArray[(id-3)*8 + i].setStatus((data>>i) & 1);
    		}
    		
    		if (id==6) 
    			System.out.println("done");
    	}
    	*/
    	else if (id == 6) {
    		// Send this input to power plant simulator
    		
    		// Done receiving input
    		HP_INIT = false;
    		
    		// Send hard panel our output
    		//sendAllOutput();
    	}
    	
    }
    
    private static void sendAllOutput() {
    	byte data;
    
    	for (int i=0; i<4; i++) {
	    	// First send leds
	    	data =  (byte)(
	    			((ledArray[i][0].getStatus() & 3) << 6) |
	    			((ledArray[i][1].getStatus() & 3) << 4) |
	    			((ledArray[i][3].getStatus() & 3) << 2) |
	    			 (ledArray[i][5].getStatus() & 3)
	    			);
	    	
	    	communicator.writeData(data);
	    	try {
	    	    Thread.sleep(1);
	    	} catch(InterruptedException ex) {
	    	    Thread.currentThread().interrupt();
	    	}
	    		
	    	data =  (byte)(
	    			((ledArray[i][6].getStatus() & 3) << 6) |
	    			((ledArray[i][7].getStatus() & 3) << 4) |
	    			((ledArray[i][9].getStatus() & 3) << 2) |
	    			 (ledArray[i][11].getStatus() & 3)
	    			);
	    	communicator.writeData(data);
	    	try {
	    	    Thread.sleep(1);
	    	} catch(InterruptedException ex) {
	    	    Thread.currentThread().interrupt();
	    	}
    	}
    	
    	// Send gauges
    	for (int i=0; i<24; i++) {
    		communicator.writeData(gaugeArray[i].getPointerLocation());
    		try {
        	    Thread.sleep(1);
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
    	}
    	
   
    }
    private void initHardPanelComponents() {
        hpLabel1 = new JLabel();
        hpLabel2 = new JLabel();
        hpLabel3 = new JLabel();
        
        cboxPorts = new JComboBox();
        btnConnect = new JButton();
        btnDisconnect = new JButton();
        btnHelpSync = new JButton();
        
        conStatus = new JLabel("Not connected to Hard Panel");
        syncStatus = new JLabel("Not in sync");
        conStatus.setForeground(Color.RED);
        syncStatus.setForeground(Color.RED);
        
        btnHelpSync.setText("Help");
        btnHelpSync.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Display dialog for getting in sync
            }
        });
        
        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnDisconnect.setText("Disconnect");
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });
    //    jScrollPane2 = new javax.swing.JScrollPane();
     //   txtLog = new javax.swing.JTextArea();

    	
    }
    
    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        communicator.connect();
        if (communicator.getConnected() == true)
        {
            if (communicator.initIOStream() == true)
            {
                communicator.initListener();
                
                UART_READY = true;
            }
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        communicator.disconnect();
    }//GEN-LAST:event_btnDisconnectActionPerformed
   
    
    private void setupLEDs() {
    	ledArray = new Led[3][12];
    	
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<12; j++) {
    			// Create the led object
    			ledArray[i][j] = new Led("Test", i*12 + j, i, j, 0); // The led object
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
    		switchArray[i] = new Switch("Test", i, 0, SWITCH_WIDTH, SWITCH_HEIGHT);
    	}
    }
    
    private void setupButtons() {
    	// Create scanner to read in button names.  
    	//Note: The text file contains two lines per button. The 1st line is for the name, 2nd is for the color (red or green).
    	 
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader("button_info.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
    	buttonArray = new Button[25];
    	
    	for (int i=0; i<24; i++) {
    		// Create button with status set to off (0). Button's name and color are initialized from the text file.
    		//String line = s.nextLine();
    		//String colors = s.nextLine();
    		//System.out.println(line + " " + colors);
    		buttonArray[i] = new Button(s.nextLine(), i, 0, Integer.parseInt(s.nextLine()), BUTTON_WIDTH);
    		
    		buttonArray[i].getPushButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					RoundButton pushedButton = (RoundButton)e.getSource();
					buttonArray[pushedButton.getButtonNum()].toggleButton();
					
					// Send data to hard panel.
					//if (UART_READY)
					//	sendUART(1, pushedButton.getButtonNum(), buttonArray[pushedButton.getButtonNum()].getStatus());
				}
    			
    		});
    	}
    	
    	s.close(); // Close the input file
    }
    

	private JPanel setupLedBoxPanel() {
		// Main container
		JPanel lightbox = new JPanel();
		lightbox.setPreferredSize(new Dimension(600,176));
		lightbox.setOpaque(true);
		lightbox.setBackground(Color.BLACK);
		lightbox.setLayout(new GroupLayout(lightbox));
		
		// Container for the lights
		JPanel ledBoxPanel = new JPanel();
		ledBoxPanel.setPreferredSize(new Dimension(600,136));
		ledBoxPanel.setLayout(new GridLayout(3,12));
		
		// Container for the row numbers
		JPanel rowPanel = new JPanel();
		rowPanel.setPreferredSize(new Dimension(20,136));
		rowPanel.setLayout(new GridLayout(3,1));
		
		// Container for the column numbers
		JPanel columnPanel = new JPanel();
		columnPanel.setPreferredSize(new Dimension(600,15));
		columnPanel.setLayout(new GridLayout(1,12));

		// Add led labels to panel
		for (int i=0; i<3; i++)
			for (int j=0; j<12; j++)
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
		JLabel[] cL = new JLabel[12];
		cL[0] = new JLabel("0");
		cL[1] = new JLabel("1");
		cL[2] = new JLabel("2");
		cL[3] = new JLabel("3");
		cL[4] = new JLabel("4");
		cL[5] = new JLabel("5");
		cL[6] = new JLabel("6");
		cL[7] = new JLabel("7");
		cL[8] = new JLabel("8");
		cL[9] = new JLabel("9");
		cL[10] = new JLabel("10");
		cL[11] = new JLabel("11");
				
		// Add column labels to panel
		for (int i=0; i<12; i++) {
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
	
	private JLayeredPane[] setupGaugePanel() {
		// Create and load all icons.
		ImageIcon pIcon = createImageIcon("/gauge_images/gauge_pointer.gif"); // The orange pointer
		ImageIcon g1Icon = createImageIcon("/gauge_images/watertemp_c_0_100_gauge.gif"); // Water temp gauge
		// Resize them
		pIcon = resizeIcon(pIcon, 18, 9);
		g1Icon = resizeIcon(g1Icon,75,180);
		
		// Create and set up each layered pane.
		JLayeredPane[] gPane = new JLayeredPane[24];
		
		// 1st layer = gauge, 2nd layer = pointer
		JLabel[] gL = new JLabel[24];
		//gP = new JLabel[24];
		
		// All gauges will be added to one panel.
	//	JPanel gaugePanel = new JPanel();
	//	gaugePanel.setBackground(new Color(239,231,183));
	//	gaugePanel.setPreferredSize(new Dimension(700,800));
	//	gaugePanel.setLayout(new GridLayout(4,6));
		
		for (int i=0; i<24; i++) {
			gL[i] = new JLabel(g1Icon);
			gaugeArray[i].createPointerLabel(pIcon);//gP[i] = new JLabel(pIcon);
			
			gPane[i] = new JLayeredPane();
			gPane[i].setPreferredSize(new Dimension(g1Icon.getIconWidth(), g1Icon.getIconHeight()));
			
			gL[i].setBounds(0,0,g1Icon.getIconWidth(), g1Icon.getIconHeight());
			//gP[i].setBounds(3, 110-pIcon.getIconHeight()/2, pIcon.getIconWidth(), pIcon.getIconHeight());
			
			gPane[i].add(gL[i], new Integer(0));
			gPane[i].add(gaugeArray[i].getPointerLabel(), new Integer(1));
	//		gaugePanel.add(gPane[i]); // Add to panel
		}

		return gPane;
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
    
	private JLayeredPane[] setupSwitchPanel() {
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
			lever[i] = resizeIcon(lever[i], SWITCH_WIDTH, (int)(SWITCH_HEIGHT/2.6));
		}
		onOffSwitch = resizeIcon(onOffSwitch,SWITCH_WIDTH, SWITCH_HEIGHT);
		switchBolt = resizeIcon(switchBolt,8,8);
		
		// Create and set up each layered pane.
		JLayeredPane[] sPane = new JLayeredPane[25];
		
		// Switch label
		JLabel[] sLabel = new JLabel[25];
		
		// Switch bolt label
		JLabel[] sBoltLabel = new JLabel[25]; 
		
		// All switches will be added to one panel.
	//	JPanel switchPanel = new JPanel();
	//	switchPanel.setPreferredSize(new Dimension(500,700));
//		switchPanel.setLayout(new GridLayout(5,5));
	//	switchPanel.setBackground(new Color(239,231,183));
		
		// Set up points for every lever icon.
		setPoints();
		
		LeverMouseListener handler = new LeverMouseListener();
		
		leverHash = new Hashtable<JLabel, Integer>();
		
		for (int i=0; i<25; i++) {
			sLabel[i] = new JLabel(onOffSwitch);
			sLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			sLabel[i].setBounds(0,0,SWITCH_WIDTH,SWITCH_HEIGHT);
			
			sBoltLabel[i] = new JLabel(switchBolt);
			sBoltLabel[i].setBounds(34,84,8,8);
			
			//switchArray.sL[i] = new JLabel(lever[0]);
			switchArray[i].setLeverLabel(lever[0]);
			switchArray[i].getLeverLabel().addMouseListener(handler); // Add mouse listener for lever movement
			switchArray[i].getLeverLabel().addMouseMotionListener(handler);
			
			// Associate labels with switch numbers by adding them to hash table.
			leverHash.put(switchArray[i].getLeverLabel(), i);
			
			sPane[i] = new JLayeredPane();
			sPane[i].setPreferredSize(new Dimension(SWITCH_WIDTH,SWITCH_HEIGHT));
			
			sPane[i].add(sLabel[i], new Integer(0));
			sPane[i].add(switchArray[i].getOffLightLabel(), new Integer(1));
			sPane[i].add(switchArray[i].getOnLightLabel(), new Integer(2));
			sPane[i].add(switchArray[i].getLeverLabel(), new Integer(3));
			sPane[i].add(sBoltLabel[i], new Integer(4));
			
	//		switchPanel.add(sPane[i]); // Add to panel
		}
		
		return sPane;
	}
	
	private JLayeredPane[] setupButtonPanel() {
		// Create and set up each layered pane.
		JLayeredPane[] bPane = new JLayeredPane[25];
		
		// Background label
		JLabel[] bgLabel = new JLabel[25];
		
		// Name label
		JLabel[] nameLabel = new JLabel[25];
		
		// All buttons will be added to one panel.
	//	JPanel buttonPanel = new JPanel();
	//	buttonPanel.setPreferredSize(new Dimension(200,1100));
	//	buttonPanel.setLayout(new GridLayout(12,2));
	//	buttonPanel.setBackground(new Color(239,231,183));
		
		for (int i=0; i<24; i++) {
			// Set up the layer for the background
			bgLabel[i] = new JLabel();
			bgLabel[i].setOpaque(true);
			//bgLabel[i].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			bgLabel[i].setBackground(Color.BLACK);
			bgLabel[i].setBounds(0,0,BUTTON_WIDTH, BUTTON_HEIGHT);

			// Set up the name of the button for display
			nameLabel[i] = new JLabel(buttonArray[i].getName());
		//	nameLabel[i].setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

			nameLabel[i].setBounds(2, 2, BUTTON_WIDTH, BUTTON_HEIGHT/4); // Set the name size just short of the button size
			nameLabel[i].setForeground(Color.WHITE); // Text is white
			
			// Pushable part of button has already been set up for display
			
			// Put everything into one layered pane.
			bPane[i] = new JLayeredPane();
			bPane[i].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			
			bPane[i].add(bgLabel[i], new Integer(0));
			bPane[i].add(nameLabel[i], new Integer(1));
			bPane[i].add(buttonArray[i].getPushButton(), new Integer(2));
			
	//		buttonPanel.add(bPane[i]); // Add to panel
		}

		return bPane;

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
	/*
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Soft Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JMenuBar menuBar = setupMenuBar(); // Add menu bar
        frame.setJMenuBar(menuBar);
        
        //Create and set up the content pane.
        SoftPanelUI newContentPane = new SoftPanelUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 */
    private static JMenuBar setupMenuBar() {
    	// Create the menu bar
    	JMenuBar menuBar = new JMenuBar();
    	
    	// Build the File menu
    	JMenu fileMenu = new JMenu("File");
    		JMenuItem exitItem = new JMenuItem("Exit");
    		fileMenu.add(exitItem);
    	
    	// Build the Window menu
    	JMenu windowMenu = new JMenu("Window");
    	
    	// Build the Window menu
    	JMenu connectMenu = new JMenu("Connect");
    		JMenuItem mcuConnectItem = new JMenuItem("Connect to Hard Panel");
    		JMenuItem ppSimConnectItem = new JMenuItem("Connect to Simulator");
    	
    	// Build the Help menu
    	JMenu helpMenu = new JMenu("Help");
		
    	menuBar.add(fileMenu);
    	menuBar.add(windowMenu);
    	menuBar.add(connectMenu);
    	menuBar.add(helpMenu);
    	
    	// Add listeners for each menu item.
    	exitItem.addActionListener(new ExitSoftPanel());
    	
    	//mcuConnectItem.addActionListener(arg0)
    	
    	return menuBar;
    }
    
    public static void main(String[] args) throws InvocationTargetException, InterruptedException, IOException {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                new SoftPanelUI().setVisible(true);
            }
        });
        
        // Create the control objects
        setInitControlStates();
        
        // Get the Power Plant Simulator's IP address from user.
      //  byte[] address = getServerAddress();

        // Establish connection with Power Plant simulator.
     //   clientCon = new UDPClient(InetAddress.getByAddress(address));
        /*
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
        */
        
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
						for (int j=0; j<12; j++)
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
					
					// Write button states
					for (int i=0; i<24; i++)
						writer.write(buttonArray[i].getStatus() + " ");
					
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

		s.close();
		
		// Display states in GUI
		/*
		 * LEDs
		 */
		for (int i=0; i<3; i++) {
			for (int j=0; j<12; j++) {
				// Get the current state from controls.txt
				int curState = Integer.parseInt(ledStates[i*12 + j]);
				
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
		for (int i=0; i<24; i++) {
			// Gauges
			int perc = Integer.parseInt(gaugeStates[i]);
			movePointer(i, perc);
			
			// Buttons
			buttonArray[i].setStatus(Integer.parseInt(buttonStates[i]));
			
			
			// Set switch states.
			int status = Integer.parseInt(switchStates[i]);
			if (status == 1) {
				moveLever(i, 18, false);
			}
			else {
				moveLever(i,0, false);
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
*/
		/*
       Timer timer2 = new Timer();
       
       timer2.scheduleAtFixedRate(new TimerTask() {
    	   int k=0, j=0, flag=1, flag2=1;
    	   int i=0;
    	   
    	   @Override
    	   public void run() {
    		   if (k == 100)
    			   flag = 0;
    		   else if (k == 0)
    			   flag = 1;
    		   if (flag == 0)
    			   k--;
    		   else k++;
    		   

    		   if (i%10 == 0) {
    			   movePointer(1, i);
    		   }
    			   movePointer(0, i);
 
    		   i++;
    		   if (i==100) {
    			   i=0;
    		   }
    	   }
       }, 1000, 30);
       */
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
    	gaugeArray[gauge].getPointerLabel().setBounds(5, 161 - (int)(percentage*1.5), 18, 9);   // **Dependent on icon size
    }
    
    /**
     * Moves a switch's lever to specified rotation
     * 
     * @param 
     */
    private static void moveLever(int num, int rotation, boolean clearToSend) {
    	// Display appropriate icon.
    	switchArray[num].getLeverLabel().setIcon(lever[rotation]);
    	
    	// Change switch's LED color if necessary.
    	if (rotation == 0) {
    		// Switch has turned off.
    		boolean changed = switchArray[num].setStatus(0);
    	}
    	else if (rotation == 18) {
    		// Switch is on, green led.
    		boolean changed = switchArray[num].setStatus(1);
    	}
    }
    /*
    private static void sendUART(int control, int num, int state) {
    	int data;
    
    	// Switch 
    	if (control == 0) {
    		data = 0b00000000; // X 00 XXXXX => switch 
    		data = (data | num); // X 00 NNNNN => N = 5 bit component number
    		data = data << 1; // 00 NNNNNN 0 => Shift left to make room for control state bit (S) 
    		data = (data | state); // 00 NNNNN S
    		System.out.println("S: " + Integer.toBinaryString(data));
    		communicator.writeData(data);
    		
    	}
    	// Button
    	else if (control == 1) {
    		data = 0b00100000; // X 01 XXXXX => button
    		data = (data | num); 
    		data = data << 1;
    		data = (data | state);
    		System.out.println("S: " + Integer.toBinaryString(data));
    		communicator.writeData(data);
    	}
    	
    	// LED
    	if (control == 2) {
    		//data = 0b01000000; // X 10 XXXXX => led
    		//data = (data | num);
    	}
    	// Gauge
    	else {
    		// We will have to do 2 transmissions because gauges require more bits
    		data = 0b01100000; // X 11 XXXXX => gauge
    		data = (data | num); // X 11 NNNNN
    		data = (data << 1); // 11 NNNNN 0
    		
    		// Send this data so master knows another data byte is coming with gauge's state
    		
    		data = state; // X NNNNNNN => 7 bit value because gauge can go up to 100
    		
    		// Send gauge's value
    		
    		
    	}
    	
    	
    	
    }
    
    */
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
						moveLever(switchNum, i, false);
						
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
					moveLever(switchNum, 18, true); // true parameter because user made final decision by letting go of mouse
				else {
					moveLever(switchNum, 0, true);
				}
			}
		}
    }

}