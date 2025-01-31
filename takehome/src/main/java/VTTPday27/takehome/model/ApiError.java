package VTTPday27.takehome.model;

import java.util.Date;

public class ApiError {
    private int status;
    private String message;
    private Date timeStamp;

    public int getStatus() {return status;}
    public void setStatus(int status) {this.status = status;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public Date getTimeStamp() {return timeStamp;}
    public void setTimeStamp(Date timeStamp) {this.timeStamp = timeStamp;}
    
    public ApiError(int status, String message, Date timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

}