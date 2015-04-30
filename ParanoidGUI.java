import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import encrypt.RSA;
import java.math.BigInteger;

public class ParanoidGUI extends JFrame implements ChatInterface
{
	RSA encryptor;
	ChatInterface remote;
	Registry registry;
	JTextArea outputArea;
	JTextField inputArea;
	JButton cmdSend;
	JButton cmdConnect;
	JOptionPane popup;
	
	private class ConnectListener implements ActionListener
	{
		ConnectListener(){}
		
		public void actionPerformed(ActionEvent e)
		{
			Connect(inputArea.getText());
			inputArea.setText("");
		}
	}
	
	private class SendListener implements ActionListener
	{
		SendListener(){}
		
		public void actionPerformed(ActionEvent e)
		{
			sendMessage(inputArea.getText());
			outputArea.append("> " + inputArea.getText() + "\n");
			outputArea.setCaretPosition(outputArea.getText().length());
			inputArea.setText("");
		}
	}
	
	/**Connects to a hostname*/
	public void remoteConnect(String addr)
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry(addr);
			remote = (ChatInterface) registry.lookup("ParanoidMSG");
			remote.setKey(encryptor.getPubKey());
			outputArea.append("Connected to " + addr + "\n");
			outputArea.setCaretPosition(outputArea.getText().length());
		}
		catch(Exception e){}
	}
	
	/**Call from UI to set up connection*/
	public void Connect(String addr)
	{
		try
		{
			remoteConnect(addr);
			remote.remoteConnect(InetAddress.getLocalHost().getHostName());
		}
		catch(Exception e)
		{
			popup.showMessageDialog(null,"Could not connect");
		}
	}
	
	/**Call this from remote backend to send a message*/
	public void netMessage(BigInteger message)
	{
		String plaintext = encryptor.decrypt(message);
		outputArea.append(plaintext + "\n");
		outputArea.setCaretPosition(outputArea.getText().length());
	}
	
	/**Call this from local UI to send a message*/
	public void sendMessage(String message)
	{
		try
		{
			remote.netMessage(encryptor.encrypt(message));
		}
		catch(Exception e)
		{
			popup.showMessageDialog(null,"Message failed");
		}
	}
	
	/**Send public key to this instance of the program*/
	public void setKey(BigInteger key)
	{
		encryptor.setPubKey(key);
	}
	
	public void bind()
	{
		try
		{
			ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(this, 0);

			// Bind the remote object's stub in the registry
			registry = LocateRegistry.getRegistry();
			registry.bind("ParanoidMSG", stub);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: an error has occurred");
			System.exit(0);
		}
	}
	
	public ParanoidGUI()
	{
		super();
		
		popup=new JOptionPane();
		
		encryptor=new RSA();
		encryptor.keyGen();
		
		setLayout(new BorderLayout());
		setSize(320,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Paranoid Messenger");
		
		JPanel cmdPanel = new JPanel(new BorderLayout());
		cmdPanel.setVisible(true);
		add(cmdPanel,BorderLayout.SOUTH);
		
		outputArea=new JTextArea();
		outputArea.setLineWrap(true);
		
		JScrollPane scroller = new JScrollPane(outputArea);
		scroller.setAutoscrolls(true);
		add(scroller,BorderLayout.CENTER);
		
		inputArea=new JTextField();
		inputArea.addActionListener(new SendListener());
		cmdPanel.add(inputArea,BorderLayout.CENTER);
		
		cmdSend=new JButton("Send");
		cmdSend.addActionListener(new SendListener());
		cmdPanel.add(cmdSend,BorderLayout.EAST);
		
		cmdConnect=new JButton("Connect");
		cmdConnect.addActionListener(new ConnectListener());
		cmdPanel.add(cmdConnect,BorderLayout.WEST);
	}
	
	public static void main(String args[])
	{
		ParanoidGUI mainWindow = new ParanoidGUI();
		mainWindow.bind();
		mainWindow.setVisible(true);
	}	
}
