package kr.co.d2net.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.TYPE })
public @interface Settings {
	public abstract int shards() default 5;
	public abstract int replicas() default 1;
	public abstract int refresh() default -1;
	public abstract String termIndex() default "1";
	public abstract boolean analysis() default false;
	public abstract String[] analyzer() default "";
	public abstract String analyzerType() default "";
	public abstract String[] analyzerToken() default "";
	public abstract String tokenizer() default "";
	public abstract String tokenizerType() default "";
	public abstract int compundLength() default 10;
	public abstract String[] filters() default {};
	public abstract String synonym() default "";
	public abstract String synonymPath() default "";
	public abstract boolean routing() default false;
	public abstract String routPath() default "";
}
