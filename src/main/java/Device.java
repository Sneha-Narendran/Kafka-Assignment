import java.sql.Timestamp;
import java.util.Date;


public class Device {

    Date date = new Date();
    long time = date.getTime();
    Timestamp timestamp = new Timestamp(time);
    private String deviceId;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    private String status;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"device_id\":" + "\"" + deviceId + "\"" + ",\n" +
                "    \"status\":" + "\"" + status + "\"" + ",\n" +

                "    \"timestamp\": " + "\"" + timestamp + "\"" + "\n}";


    }
}

