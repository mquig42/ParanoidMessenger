import java.rmi.Remote;
import java.rmi.RemoteException;
import java.math.BigInteger;

public interface ChatInterface extends Remote
{
	void setKey(BigInteger key) throws RemoteException;
	void netMessage(BigInteger message) throws RemoteException;
	void remoteConnect(String addr) throws RemoteException;
}
