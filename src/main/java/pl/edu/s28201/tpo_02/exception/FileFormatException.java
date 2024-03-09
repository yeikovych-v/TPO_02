package pl.edu.s28201.tpo_02.exception;

public class FileFormatException extends RuntimeException{
    public FileFormatException(String message) {
        super(message);
    }
}
