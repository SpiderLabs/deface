/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.viewstate_old;
//package com.trustwave.deface.viewstate;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.util.zip.GZIPOutputStream;
//
//import javax.faces.application.StateManager;
//import javax.faces.application.ViewHandler;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.myfaces.shared_impl.util.StateUtils;
//
//public class MyFaces_1_2_8 extends AbstractViewState
//{
//
//	public MyFaces_1_2_8(String viewState)
//	{
//		super(viewState);
//		Object o = StateUtils.reconstruct(viewState, externalContext);
//	}
//
//	static String encodeObject(Object o) throws IOException
//	{
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		ObjectOutputStream oStream = new ObjectOutputStream(bytes);
//		oStream.writeObject(o);
//		return new String(encode(compress(bytes.toByteArray())));
//	}
//
//    public static final byte[] encode(byte[] bytes)
//    {
//    	  return new Base64().encode(bytes);
//    }
//
//
//    public static final byte[] compress(byte[] bytes) throws IOException
//    {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        GZIPOutputStream gzip = new GZIPOutputStream(baos);
//        gzip.write(bytes, 0, bytes.length);
//        gzip.finish();
//        byte[] fewerBytes = baos.toByteArray();
//        gzip.close();
//        baos.close();
//        gzip = null;
//        baos = null;
//        return fewerBytes;
//    }
//
//	@Override
//	protected String[] getRequiredClasses()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected StateManager getStateManager()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected ViewHandler getViewHandler()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected String getViewStateParamName()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected void initializeFactories()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void insertSessionVarsPoC()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void insertXSSPoC()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public String serializeToString()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
