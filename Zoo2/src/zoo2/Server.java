package zoo2;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class Server extends UnicastRemoteObject implements ServerInterface {

    private Random randomMove;
    private boolean move;
    private static int count;

    private ArrayList<ClientInterface> clients;

    public Server() throws RemoteException {//constructor server
        super();
        randomMove = new Random();

        move = randomMove.nextBoolean(); //τυχαία τιμή κίνησης η οχι
        move = true;//προς διευκολυνση
        
        clients = new ArrayList<ClientInterface>(); //αρχικοποίηση λίστας με τους συνδεδεμένους χρήστες

    }

    //συνάρτηση ελέγχου event που θα ενημερώνει του client για τυχόν συμβάντα
    public void sendMessage() throws RemoteException {

        count = 1;
        if (move == true) {
            for (ClientInterface client : clients) {   // Ενημέρωση όλων των συνδεδεμένων πελατών
                Calendar calendar = new GregorianCalendar();
                String amORpm;
                int hour = calendar.get(Calendar.HOUR);         //Δημιουργία ενός Calendar για την αποστολή της 
                int min = calendar.get(Calendar.MINUTE);        //ώρας που σημειώθηκε κίνηση
                int sec = calendar.get(Calendar.SECOND);
                if (calendar.get(Calendar.AM_PM) == 0) {
                    amORpm = "AM";
                } else {
                    amORpm = "PM";
                }
                //τυχαία τιμή δευτερολέπτων που θα αποστέλεται ανίχνευση κίνησης
                Random r = new Random();
                int secs = 2 + r.nextInt(3);
                System.out.println("Event starting every " + secs + " seconds");

                Timer t = new Timer();
                t.schedule(new TimerTask() { //αποστολη timestamp*εικόνας ανά τα παραπάνω δευτερόλεπτα
                    @Override
                    public void run() {

                        try {

                            byte[] imageInByte;
                            BufferedImage originalImage = null;

                            originalImage = ImageIO.read(new File( //Ορίζεται ποια εικόνα θα επιλεγεί για να 
                                    String.valueOf(count) + ".png"));     //μετατραπεί σε bytes, με κατάλληλο counter
                            //που ξεκινάει από 1 και αυξάνεται αφού τα ονόματα\
                            //των εικόνων είναι 1,2,3,4 

                            // Μετατροπή της εικόνας σε bytes      
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(originalImage, "PNG", baos);

                            imageInByte = baos.toByteArray();//μεταβλητή που περιέχει τα Bytes
                            baos.close();

                            //αποστολή στον client μέσω μεθόδου του,update ενός αντικειμένου event 
                            //το οποίο περιέχει το timestamp και τα bytes της εικόνας
                            client.update(new Events(hour, min, sec, amORpm, imageInByte));
                            count++;        //αυξάνεται ο coutner για εναλλαγή των εικόνων
                            if (count == 4) {
                                count = 1;    // και αν φτάσει στη τεταρτη εικόνα επαναφέρεται στην πρώτη
                            }
                        } catch (RemoteException ex) {
                            clients.remove(client);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                },
                        0, secs
                        * 1000);//τα δευτερόλεπτα που θα αποστέλονται στιγμιότυπα στο client

            }
        } else {
            System.out.println("false");

        }
    }
//Μέθοδοι για την εγγραφή απεγγραφή Clients από τη λίστα
    public void joinServer(ClientInterface client) throws RemoteException {
        System.out.println("Client Connected....");
        clients.add(client);
    }

    public void leaveServer(ClientInterface client) throws RemoteException {
        System.out.println("Client Disconnected....");
        clients.remove(client);
    }

}
