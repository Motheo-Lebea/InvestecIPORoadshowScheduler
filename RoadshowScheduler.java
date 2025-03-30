import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Presentation {
    private final String companyName;
    private final String location;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Presentation(String companyName, String location, LocalDate date,
                        LocalTime startTime, LocalTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        this.companyName = companyName;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getCompanyName() { return companyName; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    @Override
    public String toString() {
        return String.format("%-15s | %-10s | %-10s | %s - %s",
                companyName, location, date, startTime, endTime);
    }
}

public class RoadshowScheduler {
    private final List<Presentation> presentations = new ArrayList<>();
    private Presentation newPresentation;

    public void addPresentation(Presentation newPresentation) {
        this.newPresentation = newPresentation;
        checkForConflicts(newPresentation);
        presentations.add(newPresentation);
    }

    public void removePresentation(Presentation presentation) {
        presentations.removeIf(p ->
                p.getCompanyName().equals(presentation.getCompanyName()) &&
                        p.getLocation().equals(presentation.getLocation()) &&
                        p.getDate().equals(presentation.getDate()) &&
                        p.getStartTime().equals(presentation.getStartTime()) &&
                        p.getEndTime().equals(presentation.getEndTime()));
    }

    private void checkForConflicts(Presentation newPresentation) {
        for (Presentation existing : presentations) {
            if (isSameLocationAndDate(existing, newPresentation) &&
                    hasTimeOverlap(existing, newPresentation)) {
                throw new IllegalArgumentException("Scheduling conflict detected with: " + existing);
            }
        }
    }

    private boolean isSameLocationAndDate(Presentation p1, Presentation p2) {
        return p1.getLocation().equals(p2.getLocation()) &&
                p1.getDate().equals(p2.getDate());
    }

    private boolean hasTimeOverlap(Presentation p1, Presentation p2) {
        return p1.getStartTime().isBefore(p2.getEndTime()) &&
                p1.getEndTime().isAfter(p2.getStartTime());
    }

    public List<Presentation> getSchedule() {
        presentations.sort(Comparator
                .comparing(Presentation::getDate)
                .thenComparing(Presentation::getStartTime));
        return Collections.unmodifiableList(presentations);
    }

    public List<Presentation> getPresentationsByLocation(String location) {
        return presentations.stream()
                .filter(p -> p.getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());
    }
}
