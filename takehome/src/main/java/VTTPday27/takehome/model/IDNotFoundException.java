package VTTPday27.takehome.model;

public class IDNotFoundException extends RuntimeException {
    
    public IDNotFoundException(){
        super(); //calls constructor of runtimeexpcetion (super/parent class)
    }

    //custom message is input in repository
    //the RunTimeException accepts a String message, and stores it as the exception's message
    public IDNotFoundException(String message){
        super(message);   
    }

    public IDNotFoundException(String message, Throwable cause){
        super(message, cause);
    }



}
