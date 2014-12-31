package com.archipov.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class CopyUtils {
	
	
	public static <T>T deepCopy(T obj){
		
		T finObj = null;
		Class rezClass = obj.getClass();
		rezClass.cast(finObj);
		try {
			Constructor<T> constructor = getCompliteConstructor(rezClass);
			//getParamsObjForConstructor(rezClass);
			//System.out.println("|\\-/| " + rezClass.getDeclaredField("second"));
			Object[] args = {5, "five", false};
			
			finObj = (T) constructor.newInstance(getParamsObjForConstructor(rezClass));
			copyFields(rezClass, obj, finObj);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return finObj;
	}
	
	private static Constructor getCompliteConstructor(Class ourClass) throws NoSuchMethodException, SecurityException{
		Constructor constructor = null;
		Class[] params = new Class[ourClass.getConstructors()[0].getParameterCount()];
		for(int i = 0; i < ourClass.getConstructors()[0].getParameterCount(); i++){
			params[i] = ourClass.getConstructors()[0].getParameterTypes()[i];
			//System.out.println(i + " -> " + params[i]);
		}
		constructor = ourClass.getConstructor(params);
		constructor.setAccessible(true);
		return constructor;		
	}
	
	private static Object[] getParamsObjForConstructor(Class ourClass) throws NoSuchMethodException, SecurityException{
		Constructor constuctor = null;
		constuctor = ourClass.getConstructors()[0];
		constuctor.setAccessible(true);
		//constuctor = ourClass.getDeclaredConstructor(null);
		Object[] objParams = new Object[constuctor.getParameterCount()];
		for(int i = 0; i < constuctor.getParameterCount(); i++){
			System.out.println("------>>>>>>>>> " + ourClass.getConstructors()[0].getParameters()[i].getType());
			if(constuctor.getParameters()[i].getType().toString().equals("int") ||
					constuctor.getParameters()[i].getType().toString().equals("double") ||
					constuctor.getParameters()[i].getType().toString().equals("float") ||
					constuctor.getParameters()[i].getType().toString().equals("byte") ||
					constuctor.getParameters()[i].getType().toString().equals("char") || 
					constuctor.getParameters()[i].getType().toString().equals("long")){
				objParams[i] = 0;
			}else if(constuctor.getParameters()[i].getType().toString().equals("boolean")){
				objParams[i] = false;
			}else{
				objParams[i] = null;
			}
		}
		return objParams;
	}
	
	private static <T> void copyFields(Class ourClass, T srcObj, T finObj) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = ourClass.getDeclaredFields();
		for(int i = 0; i < fields.length; i++){
			fields[i].setAccessible(true);
			fields[i].set(finObj, fields[i].get(srcObj));
		}
	}
	
	
	//����� �� Stackoverflow http://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
	static void setFinalStaticField(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}
}
