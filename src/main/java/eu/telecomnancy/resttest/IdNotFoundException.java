package eu.telecomnancy.resttest;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(Long id) {
        super("Could not find item " + id);
    }
}
