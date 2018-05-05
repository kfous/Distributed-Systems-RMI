package zoo2;

//Fousekis Konstantinos
//icsd13196

//Διεπαφή του Client
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
//Μέθοδος ενημέρωσης Client
    public void update(Events evs) throws RemoteException;

}
