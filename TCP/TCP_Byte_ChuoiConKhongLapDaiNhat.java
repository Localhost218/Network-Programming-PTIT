package TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TCP_Byte_ChuoiConKhongLapDaiNhat {
    static final String HOST = "36.50.135.242";
    static final int PORT = 2206;
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "10048F28";
    static final int TIMEOUT_MS = 5000;

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

            String s = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8).trim();
            System.out.println("Receive: " + s);

            Map<Character, Integer> lastSeen = new HashMap<>();
            int left = 0, bestStart = 0, bestLen = 0;
            for (int right = 0; right < s.length(); right++) {
                char c = s.charAt(right);
                if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                    left = lastSeen.get(c) + 1;
                }
                lastSeen.put(c, right);
                if (right - left + 1 > bestLen) {
                    bestLen = right - left + 1;
                    bestStart = left;
                }
            }

            String longest = s.substring(bestStart, bestStart + bestLen);
            String result = longest + ";" + bestLen;
            System.out.println("Send: " + result);
            out.write(result.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
