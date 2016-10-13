package app.Application;

public class MissingConfigurationProperty extends RuntimeException {
    public MissingConfigurationProperty(String s) {
        super(s);
    }
}
