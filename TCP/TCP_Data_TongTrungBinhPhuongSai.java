package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCP_Data_TongTrungBinhPhuongSai {
    static final int PORT = 2207;
    static final String HOST = "36.50.135.242";
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "A1B2C3D4";
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
                int[] arr = new int[n];
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    arr[i] = in.readInt();
                    sum += arr[i];
                }

                float avg = (float) sum / n;
                double varSum = 0;
                for (int v : arr) {
                    double d = v - avg;
                    varSum += d * d;
                }
                float variance = (float) (varSum / n);

                out.writeInt(sum);
                out.writeFloat(avg);
                out.writeFloat(variance);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
