package net.petrikainulainen.springdata.jpa.common;

/**
 * This class provides static utility methods that are used to ensure that a constructor or a method was invoked properly.
 * These methods throw an exception if the specified precondition is violated.
 *
 * This class selects the thrown exception by using the guideline given in Effective Java by Joshua Bloch (Item 60).
 *
 * @author Petri Kainulainen
 */
public final class PreCondition {

    private PreCondition() {}

    /**
     * Ensures that the expression given as a method parameter is true.
     * @param expression The inspected expression.
     * @param errorMessageTemplate The template that is used to construct the message of the exception thrown if the
     * inspected exception is false. The template must use the syntax that is supported
     * by the {@link java.lang.String#format(String, Object...)} method.
     * @param errorMessageArguments The arguments that are used when the message of the thrown exception is constructed.
     * @throws java.lang.IllegalArgumentException if the inspected exception is false.
     */
    public static void isTrue(boolean expression, String errorMessageTemplate, Object... errorMessageArguments) {
        isTrue(expression, String.format(errorMessageTemplate, errorMessageArguments));
    }
    /**
     * Ensures that the expression given as a method parameter is true.
     * @param expression The inspected expression.
     * @param errorMessage The error message that is passed forward to the exception that is thrown
     * if the expression is false.
     * @throws java.lang.IllegalArgumentException if the inspected expression is false.
     */
    public static void isTrue(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    /**
     * Ensures that the string given as a method parameter is not empty.
     * @param string The inspected string.
     * @param errorMessage The error message that is passed forward to the exception that is thrown if
     * the string is empty.
     * @throws java.lang.IllegalArgumentException if the inspected string is empty.
     */
    public static void notEmpty(String string, String errorMessage) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    /**
     * Ensures that the object given as a method parameter is not null.
     * @param reference A reference to the inspected object.
     * @param errorMessage The error message that is passed forward to the exception that is thrown if
     * the object given as a method parameter is null.
     * @throws java.lang.NullPointerException If the object given as a method parameter is null.
     */
    public static void notNull(Object reference, String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        }
    }
}
