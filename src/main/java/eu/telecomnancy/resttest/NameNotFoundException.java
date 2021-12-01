package eu.telecomnancy.resttest;

public class NameNotFoundException extends RuntimeException {
    public NameNotFoundException(String name) {
        super("Could not find item named " + name);
    }
}
