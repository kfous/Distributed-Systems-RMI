package zoo2;

//Fousekis Konstantinos
//icsd13196

import java.rmi.Naming;
import java.rmi.registry.Registry;


public class MainServer {
public static void main(String[] args) {
        try {
            
            Server server = new Server();
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);//1099 is the port for netbeans

            //καταχωρεί στο registry το απομακρυσμένο αντικείμενο με μία ονομασία σε
            //μορφή URL, έτσι ώστε να είναι δυνατή η αναζήτησή της από τον client
            Naming.rebind("//localhost/ZooRMI", server);
            System.out.println("Server up and running....");

           
        
        } catch (Exception e) {
            System.out.println("ZooServer error: " + e);
            System.exit(1);
        }

    }
}
