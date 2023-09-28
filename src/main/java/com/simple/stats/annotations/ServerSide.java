package com.simple.stats.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Note that this code used is only Server Side . (Used only for documentation purposes only) */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(value = RetentionPolicy.SOURCE)
public @interface ServerSide {

}
