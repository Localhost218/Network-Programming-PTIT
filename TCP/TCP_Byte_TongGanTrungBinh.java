package TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCP_Byte_TongGanTrungBinh {
    static final String HOST = "36.50.135.242";
    static final int PORT = 2206;
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "D45EFA12";
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

            String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8).trim();
            System.out.println("Receive: " + response);

            String[] parts = response.split(",\\s*");
            int[] nums = new int[parts.length];
            double total = 0;
            for (int i = 0; i < parts.length; i++) {
                nums[i] = Integer.parseInt(parts[i].trim());
                total += nums[i];
            }
            double avg = total / nums.length;

            int bestA = nums[0], bestB = nums[1];
            double bestDiff = Math.abs((bestA + bestB) - avg);
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    double diff = Math.abs((nums[i] + nums[j]) - avg);
                    if (diff < bestDiff) {
                        bestDiff = diff;
                        bestA = nums[i];
                        bestB = nums[j];
                    }
                }
            }

            int first = Math.min(bestA, bestB);
            int second = Math.max(bestA, bestB);
            String result = first + "," + second;
            System.out.println("Send: " + result);
            out.write(result.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
