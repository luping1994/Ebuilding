package net.suntrans.guojj.test;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Looney on 2017/8/1.
 */

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface post {
    String value() default "";
}
