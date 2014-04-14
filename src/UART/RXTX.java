package UART;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import gnu.io.*;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import my.softpanel.SoftPanelUI;

public class RXTX implements SerialPortEventListener
{
    //passed from main GUI
    SoftPanelUI window = null;

    //for containing the ports that will be found
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";

    // Keeps count of the amount of received bytes when hard panel starts up
    private int byteCounter = 0;
    
    public RXTX(SoftPanelUI window)
    {
        this.window = window;
    }

    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public void searchForPorts()
    {
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                window.cboxPorts.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect()
    {
        String selectedPort = (String)window.cboxPorts.getSelectedItem();
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

        CommPort commPort = null;

        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("HardPanel", TIMEOUT);
            
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;

            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            //for controlling GUI elements
            setConnected(true);

            window.conStatus.setForeground(new Color(0,128,0));
            window.conStatus.setText("Connected to Hard Panel on " + selectedPort);

            //CODE ON SETTING BAUD RATE ETC OMITTED
            //XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

            //enables the controls on the GUI if a successful connection is made
            //window.keybindingController.toggleControls();
        }
        catch (PortInUseException e)
        {
            window.conStatus.setForeground(Color.RED);
            window.conStatus.setText(selectedPort + " is in use. (" + e.toString() + ")");
        }
        catch (Exception e)
        {
            window.conStatus.setText("Failed to open " + selectedPort + "(" + e.toString() + ")");
            window.conStatus.setForeground(Color.RED);
        }
        
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            writeData(0);
            
            successful = true;
            return successful;
        }
        catch (IOException e) {
            window.conStatus.setForeground(Color.red);
            window.conStatus.setText("I/O Streams failed to open. (" + e.toString() + ")");
            return successful;
        }
    }

    //starts the event listener that knows whenever data is available to be read
    //pre: an open serial port
    //post: an event listener for the serial port that knows when data is recieved
    public void initListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e)
        {
            window.conStatus.setForeground(Color.red);
            window.conStatus.setText("Too many listeners. (" + e.toString() + ")");
            System.out.println("Too many listeners. (" + e.toString() + ")");
        }
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public void disconnect()
    {
        //close the serial port
        try
        {
            writeData(0);

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setConnected(false);
           // window.keybindingController.toggleControls();

            window.conStatus.setForeground(Color.red);
            window.conStatus.setText("Not connected to Hard Panel");
        }
        catch (Exception e)
        {
            window.conStatus.setForeground(Color.red);
            window.conStatus.setText("Failed to close " + serialPort.getName() + "(" + e.toString() + ")");
        }
    }

    final public boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(boolean bConnected)
    {
        this.bConnected = bConnected;
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads
    
    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                byte singleData = (byte)input.read();
                
                if (singleData == 'R') {
                	System.out.println("here1");
                	// Hard panel just started up, request its input control states
                	writeData('S');      	
                }
                
                // Hard panel is sending control states 
                // byteCounter  = 0: button 1-8
                //				= 1: button 9-16
                //				= 2: button 17-24
                //				= 3: switch 1-8
                //				= 4: switch 9-16
                //				= 5: switch 17-24
                else {
                	if (byteCounter == 0)
                		System.out.println("Buttons:\n");
                	else if (byteCounter == 3)
                		System.out.println("Switches:\n");
                	
                	//window.handleData(byteCounter, singleData);
                	byteCounter++;
                	
                	if (byteCounter==6)
                		byteCounter=0;
                	
                	String s1 = String.format("%8s", Integer.toBinaryString(singleData & 0xFF)).replace(' ', '0');
                    System.out.println("M: " + s1);
                }
                
     
            }
            catch (Exception e)
            {
                System.out.println("Failed to read data. (" + e.toString() + ")");
            }
        }
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(int data)
    {
        try
        {
            output.write(data);
            output.flush();
            //this is a delimiter for the data
        //    output.write(DASH_ASCII);
      //      output.flush();
            
         //   output.write(rightThrottle);
          //  output.flush();
            //will be read as a byte so it is a space key
        //    output.write(SPACE_ASCII);
        //    output.flush();
        }
        catch (Exception e)
        {
            System.out.println("Failed to write data. (" + e.toString() + ")");
        }
    }
}
