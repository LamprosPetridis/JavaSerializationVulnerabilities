import java.io.*;
import java.io.ObjectInputFilter.FilterInfo;
import java.io.ObjectInputFilter.Status;
import java.lang.reflect.Method;
import java.util.Arrays;

// Marker interface for allowed classes during deserialization
interface DeserializableMarker {}

public class OpenCalculatorV2 implements Serializable, DeserializableMarker {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            openCalculator();
            serializeToFile("SerializedFiles/02_ClassNameCheck/calc.dat", new OpenCalculator());
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

            // Start the process using reflection to bypass access checks
            Method startMethod = ProcessBuilder.class.getDeclaredMethod("start");
            startMethod.setAccessible(true);
            Process process = (Process) startMethod.invoke(processBuilder);

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

    private static class CustomFilter implements ObjectInputFilter {
        @Override
        public Status checkInput(FilterInfo filterInfo) {
            return Status.ALLOWED;
//            String className = filterInfo.serialClass().getName();
//            if (Arrays.asList(ALLOWED_CLASSES).contains(className)) {
//                return Status.ALLOWED;
//            } else {
//                return Status.REJECTED;
//            }
        }
    }

    static {
        ObjectInputFilter.Config.setSerialFilter(new CustomFilter());
    }
}
