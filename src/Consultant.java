import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consultant implements Runnable {
    private final String name;
    private final String speciality;
    private final PatientQueue patientQueue;
    private final Random random = new Random();
    private volatile boolean running = true;
    private final AtomicBoolean isTreating = new AtomicBoolean(false);
    private Thread currentThread;

    public Consultant(String name, String speciality, PatientQueue patientQueue) {
        this.name = name;
        this.speciality = speciality;
        this.patientQueue = patientQueue;
    }

    @Override
    public void run() {
        this.currentThread = Thread.currentThread();
        System.out.println("[CONSULTANT] " + name + " started working.");
        
        try {
            while (running) {
                try {
                    isTreating.set(false);

                    Patient patient = patientQueue.getPatient();

                    isTreating.set(true);

                    long waitTime = System.currentTimeMillis() - patient.getArrivalTime();
                    SimulationStats.recordPatientTreated(waitTime);

                    System.out.println("[TREATING] Consultant " + name + " started treating " + patient+ " Wait time: " + waitTime + "ms");

                    Thread.sleep(1000 + random.nextInt(2000));
                    
                    System.out.println("[DONE] Consultant " + name + " finished treating " + patient);

                } catch (InterruptedException e) {
                    if (isTreating.get()) {
                        System.out.println("[ALERT] Consultant " + name + " was interrupted but finished patient.");
                    }
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            System.out.println("[CONSULTANT] " + name + " shift ended.");
        }
    }
    

    public void stop() {
        running = false;
        if (!isTreating.get() && currentThread != null) {
            currentThread.interrupt();
        }
    }
}
