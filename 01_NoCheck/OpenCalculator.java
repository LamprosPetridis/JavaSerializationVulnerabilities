import java.io.*;

public class OpenCalculator implements Serializable {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            serializeToFile("SerializedFiles/01_NoCheck/calc.dat", new OpenCalculator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openCalculator() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                // Windows
                processBuilder = new ProcessBuilder("cmd", "/c", "start", "calc");
            } else if (os.contains("mac")) {
                // macOS
                processBuilder = new ProcessBuilder("open", "-a", "Calculator");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                // Linux
                processBuilder = new ProcessBuilder("gnome-calculator"); // You might need to adjust this based on the desktop environment
            } else {
                throw new UnsupportedOperationException("Unsupported operating system: " + os);
            }

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to finish (optional)
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serializeToFile(String filename, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
            System.out.println("Object serialized and saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Default deserialization
        ois.defaultReadObject();

        // Code to open the calculator
        openCalculator();
    }


}
