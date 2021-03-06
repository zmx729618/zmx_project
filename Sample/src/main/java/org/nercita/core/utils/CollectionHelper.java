package org.nercita.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

@SuppressWarnings("unchecked")
public class CollectionHelper {
	
	private CollectionHelper(){}
	
	
	@SuppressWarnings("rawtypes")
	public static LinkedHashSet asLinkedHashSet(Collection c) {
		return (LinkedHashSet)asTargetTypeCollection(c,LinkedHashSet.class);
	}
	
	@SuppressWarnings("rawtypes")
	public static HashSet asHashSet(Collection c) {
		return (HashSet)asTargetTypeCollection(c,HashSet.class);
	}
	
	@SuppressWarnings("rawtypes")
	public static ArrayList asArrayList(Collection c) {
		return (ArrayList)asTargetTypeCollection(c,ArrayList.class);
	}
	
	@SuppressWarnings("rawtypes")
	public static Collection asTargetTypeCollection(Collection c,Class targetCollectionClass) {
		if(targetCollectionClass == null) 
			throw new IllegalArgumentException("'targetCollectionClass' must be not null");
		if(c == null)
			return null;
		if(targetCollectionClass.isInstance(c)) 
			return c;
		
		Collection result = null;
		
		try {
			result = (Collection)targetCollectionClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("targetCollectionClass="+targetCollectionClass.getName()+" is not correct!",e);
		}
		
		result.addAll(c);
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List selectProperty(Collection from,String propertyName) {
		if(propertyName == null) throw new IllegalArgumentException("'propertyName' must be not null");
		if(from == null) return null;
		
		List result = new ArrayList();
		for(Object o : from) {
			try {
				if(o == null) {
					result.add(null);
				}else {
					Object value = PropertyUtils.getSimpleProperty(o, propertyName);
					result.add(value);
				}
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("Cannot get propertyValue by propertyName:"+propertyName+" on object:"+o,e);
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("Cannot get propertyValue by propertyName:"+propertyName+" on object:"+o,e.getTargetException());
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Unknown property:"+propertyName,e);
			}
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object findSingleObject(Collection c) {
		if(c == null || c.isEmpty())
			return null;
		if(c.size() > 1)
			throw new IllegalStateException("found more than one object when single object requested");
		return c.iterator().next();
	}

	@SuppressWarnings("rawtypes")
	public static double avg(Collection objects,String propertyName) {
		List<Number> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return avg(propertyValues);
	}
	
	public static double avg(Collection<Number> values) {
		if(values == null) return 0;
		if(values.isEmpty()) return 0;
		return sum(values) / values.size();
	}
	
	@SuppressWarnings("rawtypes")
	public static double sum(Collection objects,String propertyName) {
		if(objects.isEmpty()) return 0;
		List<Number> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return sum(propertyValues);
	}

	public static double sum(Collection<Number> values) {
		if(values == null) return 0;
		if(values.isEmpty()) return 0;
		
		double sum = 0;
		for(Number num : values) {
			if(num == null) continue;
			sum += num.doubleValue();
		}
		return sum;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object max(Collection objects,String propertyName) {
		List<Comparable> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return Collections.max(propertyValues);
	}

	@SuppressWarnings("rawtypes")
	public static Object min(Collection objects,String propertyName) {
		List<Comparable> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return Collections.min(propertyValues);
	}

}
