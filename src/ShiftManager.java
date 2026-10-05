import java.util.ArrayList;
import java.util.List;

public class ShiftManager {
    private final PatientQueue paediatricQueue;
    private final PatientQueue surgeryQueue;
    private final PatientQueue cardiologyQueue;
    private final List<Consultant> activeConsultantObjects = new ArrayList<>();
    private final List<Thread> activeThreads = new ArrayList<>();

    public ShiftManager(PatientQueue paediatricQueue, PatientQueue surgeryQueue, PatientQueue cardiologyQueue) {
        this.paediatricQueue = paediatricQueue;
        this.surgeryQueue = surgeryQueue;
        this.cardiologyQueue = cardiologyQueue;
    }

    public void startShift(String shiftType) {
        System.out.println("========================================");
        System.out.println("STARTING " + shiftType.toUpperCase() + " SHIFT");
        System.out.println("========================================");
        
        activeConsultantObjects.clear();
        activeThreads.clear();

        Consultant c1 = new Consultant("Dr. Paeds (" + shiftType + ")", "Paediatrics", paediatricQueue);
        Consultant c2 = new Consultant("Mr. Surgeon (" + shiftType + ")", "Surgery", surgeryQueue);
        Consultant c3 = new Consultant("Dr. Cardio (" + shiftType + ")", "Cardiology", cardiologyQueue);

        activeConsultantObjects.add(c1);
        activeConsultantObjects.add(c2);
        activeConsultantObjects.add(c3);

        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        Thread t3 = new Thread(c3);
        
        activeThreads.add(t1);
        activeThreads.add(t2);
        activeThreads.add(t3);

        t1.start();
        t2.start();
        t3.start();
    }

    public void endShift() {
        System.out.println("\n[SHIFT] Ending current shift...");
        for (Consultant c : activeConsultantObjects) {
            c.stop();
        }
        for (Thread t : activeThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("ShiftManager interrupted while waiting for consultants to stop.");
            }
        }
        
        System.out.println("[SHIFT] All consultants have ended their shift.\n");
        
        activeConsultantObjects.clear();
        activeThreads.clear();
    }
}
