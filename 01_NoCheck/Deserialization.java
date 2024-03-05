import java.io.*;

public class Deserialization {

    public static void main(String[] args) {
        // Specify the file path where the serialized object is stored
        String filePath =  "SerializedFiles/01_NoCheck/";
        filePath += "lock.dat";
//        filePath += "calc.dat";

        try {
            // Deserialize the object from the provided file
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read the object from the stream (this can execute malicious code)
            Object obj = ois.readObject();

            // Close the streams
            ois.close();
            fis.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
