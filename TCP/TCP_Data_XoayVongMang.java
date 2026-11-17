package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCP_Data_XoayVongMang {
    static final int PORT = 2207;
    static final String HOST = "36.50.135.242";
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "C6D7E8F9";
    static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) {
        String request = STUDENT_CODE + ";" + QUESTION_CODE;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PORT), TIMEOUT_MS);
            socket.setSoTimeout(TIMEOUT_MS);
            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                out.writeUTF(request);
                out.flush();

                int k = in.readInt();
                String response = in.readUTF().trim();
                System.out.println("Receive: k=" + k + ", arr=" + response);

                String[] parts = response.split(",\\s*");
                int n = parts.length;
                int shift = n == 0 ? 0 : ((k % n) + n) % n;
                String[] rotated = new String[n];
                for (int i = 0; i < n; i++) {
                    rotated[(i + shift) % n] = parts[i].trim();
                }

                String result = String.join(",", rotated);
                System.out.println("Send: " + result);
                out.writeUTF(result);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
