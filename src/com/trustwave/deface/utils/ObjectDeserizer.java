/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

public class ObjectDeserizer
{

	public static Object deserialize(String data) throws IOException, ClassNotFoundException
	{
		byte[] bytes = data.getBytes();
		if (isBase64(bytes))
		{
			bytes = Base64.decodeBase64(bytes);
		}

		InputStream is = new ByteArrayInputStream(bytes);
		if (isGziped(bytes))
		{
			is = new GZIPInputStream(is);
		}
		
		ObjectInputStream os = new ObjectInputStream(is);
		return os.readObject();
	}
	
	
	
	public static boolean isBase64(byte[] data)
	{
		return Base64.isArrayByteBase64(data);
	}
	
	private static boolean isGziped(byte[] data)
	{
		try
		{
			new GZIPInputStream(new ByteArrayInputStream(data));
			return true;
		} 
		catch (Exception ioe) 
		{
			// assume input stream is not GZIP compressed)
			return false;
		}
	}

}
