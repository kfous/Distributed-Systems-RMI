package zoo2;

//Fousekis Konstantinos
//icsd13196

//Κλάση γεγονότων που θα στέλνει αντικείμενα ανίχνευσης κίνησης
import java.io.Serializable;

public class Events implements Serializable {
//Μεταβλητές κατάλληλες για την καταγραφή ώρας και την αποστολή των bytes της εικόνας
    private int hour;
    private int minute;
    private int second;
    private String amORpm;
    private byte[] imageBytes;
    
    //Constructor 
    public Events(int hour, int minute, int second, String amORpm, byte[] imageBytes) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.amORpm = amORpm;
        this.imageBytes = imageBytes;
    }

    //getter για την ανάκτηση των bytes εικόνας όποτε ζητηθεί
    public byte[] getImageBytes() {
        return imageBytes;
    }

    //toString Μέθοδος για την εμφάνιση των μεταβλητών ενός αντικειμένου event
    @Override
    public String toString() {
        return "Movement event caught at : " + hour + ":" + minute + ":" + second + " " + amORpm ;
    }

}
