package zoo2;

//Fousekis Konstantinos
//icsd13196

//Server Interface  με τη δηλωση των καταλληλων μεθοδων που θα χρησιμοποιηθούν από τον Client
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    //Μέθοδος που θα αποστέλει αντικείμενα με το χρόνο καταγραφής κίνησης και την εικόνα
    public void sendMessage() throws RemoteException;

   

    
    // Μέθοδοι με παράμετρο ένα πελάτη και θα τον προσθέτει στην λίστα – register στον
    // εξυπηρετητή ή θα τον διαγράφει από την λίστα αντίστοιχα
    public void joinServer(ClientInterface client) throws RemoteException;

    public void leaveServer(ClientInterface client) throws RemoteException;
}
