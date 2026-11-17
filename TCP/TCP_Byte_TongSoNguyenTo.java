package TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCP_Byte_TongSoNguyenTo {
    static final String HOST = "36.50.135.242";
    static final int PORT = 2206;
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "C89DAB45";
    static final int TIMEOUT_MS = 5000;

    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String request = STUDENT_CODE + ";" + QUESTION_CODE;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PORT), TIMEOUT_MS);
            socket.setSoTimeout(TIMEOUT_MS);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            out.write(request.getBytes(StandardCharsets.UTF_8));
            out.flush();

            byte[] buffer = new byte[4096];
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                throw new RuntimeException("Server closed connection.");
            }

            String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8).trim();
            System.out.println("Receive: " + response);

            long sum = 0;
            for (String part : response.split(",\\s*")) {
                if (!part.trim().isEmpty()) {
                    int val = Integer.parseInt(part.trim());
                    if (isPrime(val)) {
                        sum += val;
                    }
                }
            }

            String result = String.valueOf(sum);
            System.out.println("Send: " + result);
            out.write(result.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
