import java.util.concurrent.LinkedBlockingQueue;

public class PatientQueue {
    private final LinkedBlockingQueue<Patient> queue;
    private final String specialityName;

    public PatientQueue(String specialityName) {
        this.queue = new LinkedBlockingQueue<>();
        this.specialityName = specialityName;
    }

    public void addPatient(Patient patient) {
        try {
            queue.put(patient);
            System.out.println("[QUEUE] " + patient + " added to " + specialityName + " Queue. Queue size: " + queue.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Failed to add patient: " + e.getMessage());
        }
    }

    public Patient getPatient() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }

}
