import encrypt.RSA;
import java.math.BigInteger;

public class EncryptTest
{
	public static void main(String args[])
	{
		RSA encryptor=new RSA();
		
		encryptor.keyGen();
		encryptor.setPubKey(encryptor.getPubKey());
		
		String message = new String("My hovercraft is full of eels");
		BigInteger cipher = encryptor.encrypt(message);
		
		System.out.println(message);
		System.out.println(cipher);
		System.out.println(encryptor.decrypt(cipher));
	}
}
