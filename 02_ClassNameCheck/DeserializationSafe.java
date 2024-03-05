import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class DeserializationSafe {

    private static final String[] ALLOWED_CLASSES = {"YourTrustedClass1", "YourTrustedClass2"};

    public static void main(String[] args) {
        // Specify the file path where the serialized object is stored
        String filePath =  "SerializedFiles/02_ClassNameCheck/";
//        filePath += "lock.dat";
        filePath += "calc.dat";

        try {
            // Deserialize the object from the provided file
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    // Check if the class is in the allowed classes whitelist
                    if (!isClassAllowed(desc.getName())) {
                        throw new SecurityException("Class not allowed: " + desc.getName());
                    }
                    // Use the default resolution if the class is allowed
                    return super.resolveClass(desc);
                }
            };

            // Read the object from the stream (this can execute malicious code)
            Object obj = ois.readObject();

            // Close the streams
            ois.close();
            fis.close();

            // Additional processing with the deserialized object
            // ...

        } catch (IOException | ClassNotFoundException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private static boolean isClassAllowed(String className) {
        // Implement your logic to check whether the class is in the allowed classes whitelist
        for (String allowedClass : ALLOWED_CLASSES) {
            if (allowedClass.equals(className)) {
                return true;
            }
        }
        return false;
    }
}
