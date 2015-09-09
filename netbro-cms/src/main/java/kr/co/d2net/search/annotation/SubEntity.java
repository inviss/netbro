package kr.co.d2net.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( {java.lang.annotation.ElementType.FIELD })
public @interface SubEntity {
	public abstract String indexType();
}
