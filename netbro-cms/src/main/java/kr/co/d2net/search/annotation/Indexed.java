package kr.co.d2net.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface Indexed {
	public abstract String name() default "";
	public abstract String type() default "";
	public abstract boolean store() default true;
	public abstract String format() default "";
	public abstract String index() default "no";
	public abstract boolean termVector() default false;
	public abstract String analyzer() default "";
	public abstract boolean includeInAll() default false;
}