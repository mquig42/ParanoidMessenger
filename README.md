# ParanoidMessenger
Instant messaging program using RSA encryption algorithm

This is a peer to peer instant messaging program. All network communication is done through RMI. If you are on Linux, the startgui shell script will start the RMI registry, then run the program. When you exit, the RMI registry will be stopped.

RSA keys will be automatically generated when the program starts, based on a java.security.SecureRandom RNG seeded with the current time. Public keys are exchanged when a connection to another instance of the program is established. The encryption library supports messages up to 255 characters in length, which is 115 more than Twitter gives you.

Disclaimer: I made this for fun. Don't blame me if it doesn't hide you from the NSA.
