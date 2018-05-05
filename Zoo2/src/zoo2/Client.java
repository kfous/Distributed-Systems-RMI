package zoo2;

//Fousekis Konstantinos
//icsd13196

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Client extends UnicastRemoteObject implements ClientInterface {

    private static JTextArea area;//static area έτσι ώστε κάθε φορά που θα αποστέλεται νέο timestamp να φαίνεται και το προηγούμενο
    private static JLabel label; //για τον ίδιο λόγο label που θα μπορεί να βλέπουν και οι συναρτήσεις και οι Main

    public Client() throws RemoteException {
        super();

    }

    //μέθοδος ενημέρωσης client για συμβάντα ως παράμετρους
    public void update(Events evs) throws RemoteException {
        try {
            // μετατροπή των bytes που περιέχει το event
            InputStream in = new ByteArrayInputStream(evs.getImageBytes());
            BufferedImage bImageFromConvert = ImageIO.read(in);
            
            //δημιουργία της νέας εικόνας με νέο όνομα που δίνεται από τα παραπάνω bytes
            ImageIO.write(bImageFromConvert, "PNG", new File(
                    "newTILE.png"));

            //εμφάνιση στο textarea των timestamps που έχει κάθε νέο event
            area.append(evs.toString() + " \n");

            //Ανάγνωση της νέα εικόνας που δημιουργήθηκε και τοποθέτησή της στο label που έχει δημιουργηθεί για την εμφάνισή της
            BufferedImage newImage = ImageIO.read(new File( "newTILE.png"));        //στην main
            ImageIcon icon = new ImageIcon(newImage); 
            label.setIcon(icon);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        try {
            //Δημιουργία αντικειμένου διεπαφής του server σε κατάλληλο lookup για την εκτέλεση του rmi
            ServerInterface look_op = (ServerInterface) Naming.lookup("//localhost/ZooRMI");
            
            
            Client client = new Client();

            //δημιουργία κουμπιών έναρξης και λήξης σύνδεσης
            JButton sub = new JButton("Subscribe");
            JButton unsub = new JButton("Unsubscribe");
            area = new JTextArea(); //όπως και area για την εμφάνιση timestamps
            area.setLineWrap(true);

            JScrollPane scroll = new JScrollPane(area); //το scrollbar του txtarea
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //που θα βρίσκεται μόνο κάθετα
            scroll.setPreferredSize(new Dimension(450, 110));//και οι διαστάσεις του

            FlowLayout layout1 = new FlowLayout();
            JPanel panel = new JPanel();             //δημιουργία μιας γραμμής

            ImageIcon icon = new ImageIcon();
            label = new JLabel(icon); //ενός label που θα δέχεται icons(για τα png files κατάλληλο)

            panel.setLayout(layout1); // δίνουμε το flowlayout που δηλώθηκε
            panel.add(sub);             //και προσθέτουμε στη γραμμή κουμπιά ,txtarea,scroll,και το label
            panel.add(unsub);
            panel.add(scroll, BorderLayout.CENTER);
            panel.add(label);

            JFrame frame = new JFrame("Event Presentation"); //δημιουργούμε το jframe για όλα τα παραπάνω
            frame.getContentPane().add(panel);  //και προσθέτουμε τη γραμμή με όλα τα περιεχόμενά της σε αυτό

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// επίσης δίνουμε διαστάσεις στο frame
            frame.setSize(500, 500);
            frame.setLocation(600, 200);
            frame.setVisible(true);

            //Action listener στις περιπτώσεις χρήσεις των κουμπιών
            sub.addActionListener((ActionEvent e) -> {
                try {
                    look_op.joinServer(client);//μέθοδος καταγραφής του client που συνδέεται στε λίστα
                    
                    look_op.sendMessage();// χρήση μεθόδου server για την έναρξη αποστολής events

                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            unsub.addActionListener((ActionEvent e) -> {
                try {
                    //απεγγραφή του client από τη λίστα συνδεδεμένων
                    look_op.leaveServer(client);
                    System.exit(1);// και κλείσιμο σύνδεσης
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (NotBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

}
