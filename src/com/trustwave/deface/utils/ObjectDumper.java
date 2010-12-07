/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jasper.el.JspValueExpression;

public class ObjectDumper
{
	private static List<String> bannedClasses;
	private static List<String> badHashClasses;
	static
	{
		bannedClasses = new ArrayList<String>();
		bannedClasses.add("java.util.logging.Logger");
		bannedClasses.add("sun.misc.URLClassPath");
		bannedClasses.add("org.apache.commons.logging.impl.Log4JLogger");

		badHashClasses = new ArrayList<String>();
		badHashClasses.add("javax.el.Expression");
	}

	private static boolean isBadHashClass(Class c)
	{
		for(String name: getAllSupers(c))
		{
			if (badHashClasses.contains(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public static List<String> getAllSupers(Class c)
	{
		List<String> names = new ArrayList<String>();
		Class sup = c;
		while(sup != Object.class && sup != void.class && sup != null)
		{
			names.add(sup.getCanonicalName());
			sup = sup.getSuperclass();
		}
		
		return names;
	}
	
	public static String dumpObject(Object o, boolean showNullMembers)
	{
		Set<Integer> objects = new HashSet<Integer>();
		return dumpObject(o, "", objects, showNullMembers); 
	}
	
	private static String dumpObject(Object o, String tabs, Set<Integer> objects, boolean showNullMembers)
	{
		if (o == null)
		{
			return ("null");
		}

		Class oClass = o.getClass();
		if (oClass.isPrimitive() || oClass.equals(Boolean.class)
				|| oClass.equals(Long.class) || oClass.equals(Integer.class)
				|| oClass.equals(Float.class) || oClass.equals(Byte.class)
				|| oClass.equals(Character.class) || oClass.equals(Short.class)
				|| oClass.equals(Double.class))
		{
			return (o.toString() + " (" + oClass.getSimpleName() + ")");
		}

		if (oClass.equals(String.class))
		{
			return ("'" + o.toString() + "' (String)");
		}

		StringBuffer buffer = new StringBuffer();
		int hash = o.hashCode();
		if (!isBadHashClass(oClass))
		{
			if (objects.contains(hash))
			{
				
				String type = oClass.isArray() ? "array" : "object";
				return "(" + type + " #" + hash + " already displayed)";
			}
			objects.add(hash);
		}
		else
		{
			System.out.println("Dum tomcat");
		}

		if (oClass.isArray())
		{
			int len = Array.getLength(o);
			String type = o.getClass().getComponentType().getName();
			buffer.append("ARRAY (" + type + "[" + len + "]) (#" + hash + "):");
			tabs += "\t";
			for (int i = 0; i < len; i++)
			{
				Object value = Array.get(o, i);
				if (value != null || showNullMembers)
				{
					buffer.append("\n" + tabs + "[" + i + "]: ");
					buffer.append(dumpObject(value, tabs, objects, showNullMembers));
				}
			}
		}
		else if (o instanceof Collection)
		{
			Collection c = (Collection) o;
			buffer.append("COLLECTION (" + c.size() + " members) (#" + hash + "):");
			tabs += "\t";
			int count = 1;
			for(Object member: c)
			{
				if (member != null || showNullMembers)
				{
					buffer.append("\n" + tabs + "collection member " + count++ + ": " + dumpObject(member, tabs, objects, showNullMembers));
				}
			}
		}
		else if (o instanceof Map)
		{
			Map m = (Map) o;
			buffer.append("MAP (" + m.size() + " members) (#" + hash + "):");
			tabs += "\t";
			int count = 1;
			for(Object key: m.keySet())
			{
				if (m.get(key) != null || showNullMembers)
				{
					buffer.append("\n" + tabs + "map member " + count++ + ":\n" +
								tabs + "\tkey: " + dumpObject(key, tabs + "\t", objects, showNullMembers) + "\n" +
								tabs + "\tvalue: " + dumpObject(m.get(key), tabs + "\t", objects, showNullMembers));
				}
			}
		}
		else
		// a normal object
		{
			buffer.append("(" + oClass.getCanonicalName() + ") (#" + hash + ")");
			if (bannedClasses.contains(oClass.getCanonicalName()))
			{
				buffer.append(" -- class not displayed");
			}
			else
			{
				tabs += "\t";
				Set<String> fieldNames = new HashSet<String>();
	
				Class superClass = oClass;
				while (superClass != Object.class && superClass != Class.class)
				{
					Field[] fields = superClass.getDeclaredFields();
					for (Field field : fields)
					{
						field.setAccessible(true);
						int modifiers = field.getModifiers();
						if (((modifiers & Modifier.FINAL) != 0)
								&& ((modifiers & Modifier.STATIC) != 0))
						{
							continue; // skip this for now
						}
						try
						{
							Object value = field.get(o);
							if (value != null || showNullMembers)
							{
								buffer.append("\n" + tabs + "field \"" + field.getName() + "\"");
								if (fieldNames.contains(field.getName()))
								{
									buffer.append(" (from super class " + superClass.getCanonicalName() + ")");
								}
								buffer.append(": " + dumpObject(value, tabs, objects, showNullMembers));
							}
						}
						catch (IllegalAccessException e)
						{
							System.err.println("What the what? " + e.getMessage());
							e.printStackTrace();
						}
					}
					superClass = superClass.getSuperclass();
				}
			}
		}
		return buffer.toString();
	}

}
