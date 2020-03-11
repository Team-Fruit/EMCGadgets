package net.teamfruit.emcgadgets;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * a method invoked by ASM
 *
 * @author Kamesuta
 */
@Retention(SOURCE)
@Target({TYPE, CONSTRUCTOR, METHOD, FIELD})
public @interface CoreInvoke {

}
