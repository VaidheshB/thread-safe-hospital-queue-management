public class HospitalSimulation {
    private static final int SHIFT_DURATION_MS = 12000; // 12 seconds = 12 hours
    private static final int TOTAL_SHIFTS = 2; // Simulate 1 day (Day, Night) Queues might be ended with remaining Patients

    public static void main(String[] args) {
        System.out.println("--- Royal Manchester Hospital Management System Simulation Started ---");

        PatientQueue paediatricQueue = new PatientQueue("Paediatrics");
        PatientQueue surgeryQueue = new PatientQueue("Surgery");
        PatientQueue cardiologyQueue = new PatientQueue("Cardiology");

        PatientArrival patientArrival = new PatientArrival(paediatricQueue, surgeryQueue, cardiologyQueue);
        Thread arrivalThread = new Thread(patientArrival);
        arrivalThread.start();
        System.out.println("[SYSTEM] Patient Arrival Producer started.");
        ShiftManager shiftManager = new ShiftManager(paediatricQueue, surgeryQueue, cardiologyQueue);
        try {
            for (int i = 0; i < TOTAL_SHIFTS; i++) {
                String shiftType = (i % 2 == 0) ? "Day" : "Night";
                shiftManager.startShift(shiftType);
                try {
                    Thread.sleep(SHIFT_DURATION_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Simulation interrupted.");
                }
                shiftManager.endShift();
            }
        } finally {
            System.out.println("[SYSTEM] Stopping Patient Arrival Producer...");
            patientArrival.stop();
            arrivalThread.interrupt(); 
            try {
                arrivalThread.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while joining arrival thread.");
            }
            printSummary(paediatricQueue, surgeryQueue, cardiologyQueue);
            System.out.println("--- Hospital Management Simulation Ended ---");
        }
    }

    private static void printSummary(PatientQueue pQueue, PatientQueue sQueue, PatientQueue cQueue) {
        System.out.println("\n============ SIMULATION SUMMARY ============");
        System.out.println("Remaining Patients in Queues:");
        System.out.println(" - Paediatrics: " + pQueue.size());
        System.out.println(" - Surgery:     " + sQueue.size());
        System.out.println(" - Cardiology:  " + cQueue.size());
        
        int totalRemaining = pQueue.size() + sQueue.size() + cQueue.size();
        System.out.println("Total Waiting:  " + totalRemaining);
        
        System.out.println("--------------------------------------------");
        System.out.println("Performance Metrics (Target: Seen within 4h/4s):");
        int totalTreated = SimulationStats.getTotalTreated();
        int metTarget = SimulationStats.getMetTarget();
        int missedTarget = totalTreated - metTarget;
        double percentage = totalTreated > 0 ? ((double) metTarget / totalTreated) * 100 : 0.0;
        
        System.out.println(" - Total Patients Treated: " + totalTreated);
        System.out.println(" - Met Target (<= 4s):     " + metTarget);
        System.out.println(" - Missed Target (> 4s):   " + missedTarget);
        System.out.printf(" - Success Rate:           %.2f%%\n", percentage);
        System.out.println("============================================\n");
    }
}