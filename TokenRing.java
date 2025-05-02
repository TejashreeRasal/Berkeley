import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TokenRing {
    private List<Process> processes;
    private int currentTokenHolderIndex;
    private final Lock lock = new ReentrantLock();

    public TokenRing(int numProcesses) {
        processes = new ArrayList<>();
        currentTokenHolderIndex = 0; // First process holds the token initially
        for (int i = 0; i < numProcesses; i++) {
            // Pass the tokenRing instance to each Process when created
            processes.add(new Process(i, this));
        }
    }

    public void startRing() {
        // Start the processes
        for (Process process : processes) {
            new Thread(process).start();
        }
    }

    public void passToken(int fromIndex, int toIndex) {
        lock.lock();
        try {
            System.out.println("Process " + processes.get(fromIndex).getId() + " passes the token to Process " + processes.get(toIndex).getId());
            currentTokenHolderIndex = toIndex;
        } finally {
            lock.unlock();
        }
    }

    public Process getCurrentProcessWithToken() {
        return processes.get(currentTokenHolderIndex);
    }

    public int getNextProcessIndex(int currentIndex) {
        return (currentIndex + 1) % processes.size();
    }
}
