
package co.unicauca.usermanagement;

/**
 *
 * @author Sam
 */
public class AvailableSlot {

    private String day;
    private String startTime;
    private String endTime;

    public AvailableSlot(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}