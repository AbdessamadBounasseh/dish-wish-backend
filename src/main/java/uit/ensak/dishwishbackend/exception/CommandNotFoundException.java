package uit.ensak.dishwishbackend.exception;


public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(String message){
        super(message);
    }
}

