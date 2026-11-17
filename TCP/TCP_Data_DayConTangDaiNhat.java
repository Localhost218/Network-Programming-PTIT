package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TCP_Data_DayConTangDaiNhat {
    static final int PORT = 2207;
    static final String HOST = "36.50.135.242";
    static final String STUDENT_CODE = "B23DCCN465";
    static final String QUESTION_CODE = "B4C5D6E7";
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

                String response = in.readUTF().trim();
                System.out.println("Receive: " + response);

                String[] parts = response.split(",\\s*");
                int n = parts.length;
                int[] nums = new int[n];
                for (int i = 0; i < n; i++) {
                    nums[i] = Integer.parseInt(parts[i].trim());
                }

                int[] dp = new int[n];
                int[] parent = new int[n];
                Arrays.fill(dp, 1);
                Arrays.fill(parent, -1);

                int bestLen = 0, bestEnd = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < i; j++) {
                        if (nums[i] > nums[j] && dp[j] + 1 > dp[i]) {
                            dp[i] = dp[j] + 1;
                            parent[i] = j;
                        }
                    }
                    if (dp[i] > bestLen) {
                        bestLen = dp[i];
                        bestEnd = i;
                    }
                }

                List<String> lis = new ArrayList<>();
                for (int cur = bestEnd; cur != -1 && n > 0; cur = parent[cur]) {
                    lis.add(String.valueOf(nums[cur]));
                }
                Collections.reverse(lis);

                String seq = String.join(",", lis);
                System.out.println("Send: " + seq + " (" + bestLen + ")");
                out.writeUTF(seq);
                out.writeInt(bestLen);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
