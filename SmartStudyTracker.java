import java.io.*;
import java.util.*;

class StudySession {
    String subject;
    int hours;
    String date;

    StudySession(String subject, int hours, String date) {
        this.subject = subject;
        this.hours = hours;
        this.date = date;
    }

    @Override
    public String toString() {
        return subject + "," + hours + "," + date;
    }
}

public class SmartStudyTracker {
    static final String FILE_NAME = "study_data.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Smart Study Tracker ---");
            System.out.println("1. Add Study Session");
            System.out.println("2. View All Sessions");
            System.out.println("3. View Summary");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addSession();
                case 2 -> viewSessions();
                case 3 -> viewSummary();
                case 4 -> { System.out.println("Exiting..."); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void addSession() {
        System.out.print("Enter Subject: ");
        String subject = sc.nextLine();
        System.out.print("Enter Hours Studied: ");
        int hours = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        StudySession session = new StudySession(subject, hours, date);

        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(session.toString());
            bw.newLine();
            System.out.println("Study session added successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    static void viewSessions() {
        System.out.println("\n--- All Study Sessions ---");
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            if ((line = br.readLine()) == null) {
                System.out.println("No sessions found.");
                return;
            } else {
                System.out.println("Subject | Hours | Date");
                System.out.println("-------------------------");
                do {
                    String[] parts = line.split(",");
                    System.out.println(parts[0] + " | " + parts[1] + " | " + parts[2]);
                } while ((line = br.readLine()) != null);
            }
        } catch (IOException e) {
            System.out.println("Error reading data.");
        }
    }

    static void viewSummary() {
        HashMap<String, Integer> summary = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String subject = parts[0];
                int hours = Integer.parseInt(parts[1]);
                summary.put(subject, summary.getOrDefault(subject, 0) + hours);
            }
        } catch (IOException e) {
            System.out.println("Error reading data.");
            return;
        }

        if (summary.isEmpty()) {
            System.out.println("No study data found.");
            return;
        }

        System.out.println("\n--- Study Summary ---");
        int totalHours = 0;
        String topSubject = "";
        int maxHours = 0;
        for (String subject : summary.keySet()) {
            int hours = summary.get(subject);
            totalHours += hours;
            System.out.println(subject + ": " + hours + " hours");
            if (hours > maxHours) {
                maxHours = hours;
                topSubject = subject;
            }
        }
        System.out.println("Total Hours Studied: " + totalHours);
        System.out.println("Most Studied Subject: " + topSubject);
    }
}
