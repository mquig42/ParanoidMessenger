import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import encrypt.RSA;
import java.math.BigInteger;

public class Backend implements ChatInterface
{
	private RSA encryptor;
	//private chatUI ui;
	private ChatInterface remote;
	private Registry registry;
	
	public Backend()
	{
		encryptor = new RSA();
		encryptor.keyGen();
		
		//ui=userInterface;
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
	
	/**Send public key to this instance of the program*/
	public void setKey(BigInteger key)
	{
		encryptor.setPubKey(key);
	}
	
	/**Call this from remote backend to send a message*/
	public void netMessage(BigInteger message)
	{
		String plaintext = encryptor.decrypt(message);
		System.out.println("> " + plaintext);
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
			System.out.println("Message failed");
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
			System.out.println("Could not connect");
		}
	}
}
