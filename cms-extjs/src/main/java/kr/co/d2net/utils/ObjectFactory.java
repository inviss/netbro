package kr.co.d2net.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ObjectFactory<E> {

	private Class<?> claxx;

	public ObjectFactory(Class<E> clazz) {
		claxx = clazz;
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance() throws InstantiationException, IllegalAccessException {
		T obj = (T) this.claxx.newInstance();
		return obj;
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<E> argsClasses[], Object constructorArgs[])
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
			{
		Constructor<?> cons = claxx.getConstructor(argsClasses);
		return (T)cons.newInstance(constructorArgs);
	}

	public Object newInstanceAndBindProperties(Map<String, Object> propertys)
			throws InstantiationException, IllegalAccessException {
		Object obj = claxx.newInstance();
		ObjectUtils.setProperties(obj, propertys);
		return obj;
	}

	public Object newInstanceAndBindProperties(Class<E> argsClasses[], Object constructorArgs[], Map<String, Object> propertys)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
			{
		Constructor<?> cons = claxx.getConstructor(argsClasses);
		Object obj = cons.newInstance(constructorArgs);
		ObjectUtils.setProperties(obj, propertys);
		return obj;
	}

	public Constructor<?> getConstructor(Class<E> argsClasses[])
			throws SecurityException, NoSuchMethodException {
		return claxx.getConstructor(argsClasses);
	}

	public Constructor<?>[] getConstructor()
			throws SecurityException, NoSuchMethodException {
		return claxx.getConstructors();
	}

	public void setProperties(Object t, Map<String, Object> propertys) {
		ObjectUtils.setProperties(t, propertys);
	}

	public void setProperty(Object t, String name, Object val) {
		ObjectUtils.setProperty(t, name, val);
	}

}
