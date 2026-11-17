package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCP_Data_XacSuatXucXac {
    static final int PORT = 2207;
    static final String HOST = "36.50.135.242";
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "0D135D6A";
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

                int n = in.readInt();
                int[] counts = new int[7];
                for (int i = 0; i < n; i++) {
                    int face = in.readInt();
                    if (face >= 1 && face <= 6) {
                        counts[face]++;
                    }
                }

                for (int face = 1; face <= 6; face++) {
                    out.writeFloat((float) counts[face] / n);
                }
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
