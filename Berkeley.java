import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;

public class Berkeley {
    float diff(int h, int m, int s, int nh, int nm, int ns) {
        int dh = h - nh;
        int dm = m - nm;
        int ds = s - ns;
        return (dh * 3600) + (dm * 60) + ds;
    }

    float average(float[] diff, int n) {
        float sum = 0;
        for (int i = 0; i < n; i++) {
            sum += diff[i];
        }
        float average = sum / (n + 1); // +1 for the time server
        System.out.println("The average of all time differences is: " + average + " seconds.");
        return average;
    }

    void sync(float[] diff, int n, int h, int m, int s, int[] nh, int[] nm, int[] ns, float average) {
        System.out.println("\n--- Synchronized Clocks ---");

        // Synchronize server time
        int totalServerSeconds = h * 3600 + m * 60 + s + (int) average;
        int newH = (totalServerSeconds / 3600) % 24;
        int newM = (totalServerSeconds % 3600) / 60;
        int newS = totalServerSeconds % 60;

        System.out.println("Time Server ---> " + newH + " : " + newM + " : " + newS);

        // Synchronize nodes
        for (int i = 0; i < n; i++) {
            int nodeSeconds = nh[i] * 3600 + nm[i] * 60 + ns[i] + (int)(average + diff[i]);
            nh[i] = (nodeSeconds / 3600) % 24;
            nm[i] = (nodeSeconds % 3600) / 60;
            ns[i] = nodeSeconds % 60;

            System.out.println("Node " + (i + 1) + " ---> " + nh[i] + " : " + nm[i] + " : " + ns[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        Berkeley b = new Berkeley();
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter number of nodes: ");
        int n = Integer.parseInt(obj.readLine());

        LocalTime time = LocalTime.now();
        int h = time.getHour();
        int m = time.getMinute();
        int s = time.getSecond();

        int[] nh = new int[n];
        int[] nm = new int[n];
        int[] ns = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter time for node " + (i + 1) + ":");
            System.out.print("Hours (0-23): ");
            nh[i] = Integer.parseInt(obj.readLine());
            System.out.print("Minutes (0-59): ");
            nm[i] = Integer.parseInt(obj.readLine());
            System.out.print("Seconds (0-59): ");
            ns[i] = Integer.parseInt(obj.readLine());
        }

        System.out.println("\n--- Time Sent by Server ---");
        for (int i = 0; i < n; i++) {
            System.out.println("Time Server sent time " + h + " : " + m + " : " + s + " to Node " + (i + 1));
        }

        float[] diff = new float[n];
        for (int i = 0; i < n; i++) {
            diff[i] = b.diff(h, m, s, nh[i], nm[i], ns[i]);
            System.out.println("Node " + (i + 1) + " sent time difference of " + (int)diff[i] + " seconds to Time Server.");
        }

        float average = b.average(diff, n);
        b.sync(diff, n, h, m, s, nh, nm, ns, average);
    }
}
