package net.petrikainulainen.springdata.jpa.common;

/**
 * This class is used to capture the Throwable object thrown by the tested
 * method.
 *
 * Note: I borrowed this idea from a blog post titled:
 * <a href="http://www.codeaffine.com/2014/07/28/clean-junit-throwable-tests-with-java-8-lambdas/">Clean JUnit Throwable-Tests with Java 8 Lambdas</a>
 * @author Petri Kainulainen
 */
public final class ThrowableCaptor {

    @FunctionalInterface
    public interface Actor {
        void act() throws Throwable;
    }

    /**
     * Prevents instantiation.
     */
    private ThrowableCaptor() {}

    /**
     * Captures the thrown Throwable object.
     * @param actor
     * @return The captured Throwable object of null if none is thrown.
     */
    public static Throwable thrown(Actor actor) {
        Throwable thrown = null;
        try {
            actor.act();
        }
        catch (Throwable captured) {
            thrown = captured;
        }
        return thrown;
    }
}
