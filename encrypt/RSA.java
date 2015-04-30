package encrypt;

import java.security.SecureRandom;
import java.util.Date;
import java.math.BigInteger;

public class RSA
{
	private BigInteger n;
	private BigInteger e;
	private BigInteger d;
	private BigInteger otherN;
	
	/**Inits the object, but does not generate keys*/
	public RSA()
	{
		n=new BigInteger("0");
		e=new BigInteger("65537");
		d=new BigInteger("2");
		otherN=new BigInteger("0");
	}
		
	/**Generates key pair*/
	public void keyGen()
	{
		SecureRandom randGen=new SecureRandom();
		randGen.setSeed(new Date().getTime());
		
		BigInteger p = new BigInteger(1024,100,randGen);
		BigInteger q = new BigInteger(1024,100,randGen);	
		BigInteger totient = new BigInteger("0");
		
		n=p.multiply(q);		//Calculate n
		totient=((p.add(new BigInteger("-1"))).multiply(q.add(new BigInteger("-1"))));	//calculate totient
		d=e.modInverse(totient);	//Calculate d
	}
	
	/**Returns n. e is always 65537*/
	public BigInteger getPubKey()
	{
		return n;
	}
	
	/**Takes the other user's n and stores it.*/
	public void setPubKey(BigInteger newKey)
	{
		otherN=newKey;
	}
	
	/**Encrypts a string. Must have generated a keypair and set other user's public key
	So far, works on strings of up to 255 characters
	Encrypted data is stored as a BigInteger*/
	public BigInteger encrypt(String message)
	{
		BigInteger iMessage=new BigInteger(message.getBytes());	//Convert message to BigInteger
		BigInteger cipherText=iMessage.modPow(e,otherN);	//Encrypt message
		return cipherText;
	}
	
	/**Decrypts a string. Must have generated a keypair*/
	public String decrypt(BigInteger message)
	{
		BigInteger plainText=message.modPow(d,n);
		return new String(plainText.toByteArray());
	}	
}
