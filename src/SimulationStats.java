import java.util.concurrent.atomic.AtomicInteger;

public class SimulationStats {
    private static final AtomicInteger totalTreated = new AtomicInteger(0);
    private static final AtomicInteger metTarget = new AtomicInteger(0);
    public static final long waitTarget = 4000;

    public static void recordPatientTreated(long waitTimeMs) {
        totalTreated.incrementAndGet();
        if (waitTimeMs <= waitTarget) {
            metTarget.incrementAndGet();
        }
    }

    public static int getTotalTreated() {
        return totalTreated.get();
    }

    public static int getMetTarget() {
        return metTarget.get();
    }
}
