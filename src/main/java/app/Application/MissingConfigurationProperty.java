package app.application;

public class MissingConfigurationProperty extends RuntimeException {
    public MissingConfigurationProperty(String s) {
        super(s);
    }
}
