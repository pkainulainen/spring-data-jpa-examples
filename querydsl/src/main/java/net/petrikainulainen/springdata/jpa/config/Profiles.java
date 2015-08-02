package net.petrikainulainen.springdata.jpa.config;

/**
 * This class defines the Spring profiles used in the project. The idea behind this class
 * is that it helps us to avoid typos when we are using these profiles. At the moment
 * there are two profiles which are described in the following:
 * <ul>
 * <li>The APPLICATION profile is used when we run our example application.</li>
 * <li>The INTEGRATION_TEST profile is used when we run the integration tests of our example application.</li>
 * </ul>
 *
 * @author Petri Kainulainen
 */
public class Profiles {
    public static final String APPLICATION = "application";
    public static final String INTEGRATION_TEST = "integrationtest";
    /**
     * Prevent instantiation.
     */
    private Profiles() {}
}
