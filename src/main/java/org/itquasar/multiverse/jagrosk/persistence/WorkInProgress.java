package org.itquasar.multiverse.jagrosk.persistence;

import java.lang.annotation.*;

/**
 * Added to a class or method to denote that it's not ready to use.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface WorkInProgress {
}
