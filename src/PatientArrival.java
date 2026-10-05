import java.util.Random;

public class PatientArrival implements Runnable {
    private final PatientQueue paediatricQueue;
    private final PatientQueue surgeryQueue;
    private final PatientQueue cardiologyQueue;
    private volatile boolean running = true;
    private final Random random = new Random();
    private int patientIdCounter = 1;

    public PatientArrival(PatientQueue paediatricQueue, PatientQueue surgeryQueue, PatientQueue cardiologyQueue) {
        this.paediatricQueue = paediatricQueue;
        this.surgeryQueue = surgeryQueue;
        this.cardiologyQueue = cardiologyQueue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(500 + random.nextInt(1000));
                String speciality = getRandomSpeciality();
                Patient newPatient = new Patient(patientIdCounter++, speciality);
                switch (speciality) {
                    case "Paediatrics":
                        paediatricQueue.addPatient(newPatient);
                        break;
                    case "Surgery":
                        surgeryQueue.addPatient(newPatient);
                        break;
                    case "Cardiology":
                        cardiologyQueue.addPatient(newPatient);
                        break;
                }

            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("PatientArrival thread stopped.");
    }

    public void stop() {
        running = false;
    }

    private String getRandomSpeciality() {
        int choice = random.nextInt(3);
        switch (choice) {
            case 0: return "Paediatrics";
            case 1: return "Surgery";
            default: return "Cardiology";
        }
    }
}
