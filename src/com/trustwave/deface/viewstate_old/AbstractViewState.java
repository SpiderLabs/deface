/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.viewstate_old;
//package com.trustwave.deface.viewstate;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.util.zip.GZIPInputStream;
//
//import javax.faces.FactoryFinder;
//import javax.faces.application.Application;
//import javax.faces.application.StateManager;
//import javax.faces.application.ViewHandler;
//import javax.faces.component.UIViewRoot;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.faces.context.ResponseWriter;
//import javax.faces.lifecycle.Lifecycle;
//import javax.faces.render.RenderKit;
//import javax.faces.render.RenderKitFactory;
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.codec.binary.Base64InputStream;
//import org.apache.shale.test.mock.MockApplication;
//import org.apache.shale.test.mock.MockExternalContext;
//import org.apache.shale.test.mock.MockFacesContext;
//import org.apache.shale.test.mock.MockHttpServletRequest;
//import org.apache.shale.test.mock.MockHttpServletResponse;
//import org.apache.shale.test.mock.MockLifecycle;
//import org.apache.shale.test.mock.MockServletContext;
//
//import com.trustwave.deface.utils.ObjectDumper;
//import com.trustwave.deface.utils.WriteBehindStateWriter;
//
//public abstract class AbstractViewState
//{
////	protected final Version version;
//	protected boolean compressViewState;
//	protected final FacesContext facesContext;
//	protected final ExternalContext externalContext;
//	protected final ServletContext servletContext;
//	protected final HttpServletRequest request;
//	protected final HttpServletResponse response;
//	protected final Lifecycle lifecycle;
//	protected final UIViewRoot viewRoot;
//	protected final Application application;
////	protected final URLClassLoader classLoader;
//	protected final String rawViewState;
//	protected final WriteBehindStateWriter stateWriter;
//	
////	protected AbstractViewState(Version version, String viewState)
//	protected AbstractViewState(String viewState)
//	{
//		initializeFactories();
////		this.version = version;
//		this.rawViewState = viewState;
//
////		classLoader = initializeClassLoader();
//		
//		servletContext = createMockServletContext();
//		request = createMockHttpServletRequest();
//		response = createMockHttpServletResponse();
//		application = createMockApplication();
//		externalContext = createMockExternalContext();
//		facesContext = createMockFacesContext();
//		stateWriter = createWriteBehindStateWriter();
//		lifecycle = createMockLifecycle();
//		viewRoot = getViewHandler().restoreView(facesContext, getViewStateParamName());
//		facesContext.setViewRoot(viewRoot);
//	}
//
//	protected abstract ViewHandler getViewHandler();
//	protected abstract void initializeFactories();
////	protected abstract String[] getRequiredClasses();
//	protected abstract StateManager getStateManager();
//	protected abstract String getViewStateParamName();
//
//	public abstract String serializeToString(); 
//	public abstract void insertXSSPoC();
//	public abstract void insertSessionVarsPoC();
//
//	
////	private URLClassLoader initializeClassLoader()
////	{
////		URLClassLoader loader = null;
////		try
////		{
////			loader = (URLClassLoader)ClassLoader.getSystemClassLoader();
////			Class sysclass = URLClassLoader.class;
////			try 
////			{
////				Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
////				method.setAccessible(true);
////				method.invoke(loader, new Object[]{ getJarURL() });
////			} 
////			catch (Exception e) 
////			{
////				System.err.println("Failed to set class path: " + e.getLocalizedMessage());
////				e.printStackTrace();
////				System.exit(1);
////			}
////			
////			
//////			loader = new URLClassLoader(getJarURLs());
////			for (String className: getRequiredClasses())
////			{
////				loader.loadClass(className);
////			}
////		}
//////		catch (MalformedURLException e)
//////		{
//////			System.err.println("Failed to load JavaServer Faces jar: " + e.getLocalizedMessage());
//////			e.printStackTrace();
//////			System.exit(1);
//////		}
////		catch (ClassNotFoundException e)
////		{
////			System.err.println("Failed to load JavaServer Faces jar: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		return loader;
////	}
//	
//	
//	protected MockLifecycle createMockLifecycle()
//	{
//		return new MockLifecycle();
//	}
//	
//	protected MockFacesContext createMockFacesContext()
//	{
//		MockFacesContext mfc = new MockFacesContext(externalContext);
//		((MockFacesContext) mfc).setApplication(application);
////		Field defaultFacesContext;
////		try
////		{
////			defaultFacesContext = FacesContext.class.getDeclaredField("defaultFacesContext");
////			defaultFacesContext.setAccessible(true);
////			defaultFacesContext.set(mfc, mfc);
////		}
////		catch (SecurityException e)
////		{
////			System.err.println("Problem creating FacesContext: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		catch (NoSuchFieldException e)
////		{
////			System.err.println("Problem creating FacesContext: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		catch (IllegalArgumentException e)
////		{
////			System.err.println("Problem creating FacesContext: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		catch (IllegalAccessException e)
////		{
////			System.err.println("Problem creating FacesContext: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
//
//		mfc.getApplication().setViewHandler(getViewHandler());
//		mfc.getApplication().setStateManager(getStateManager());
//		return mfc;
//	}
//	
//	protected WriteBehindStateWriter createWriteBehindStateWriter()
//	{
//		RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
//		RenderKit renderKit = renderFactory.getRenderKit(facesContext, RenderKitFactory.HTML_BASIC_RENDER_KIT);
//		WriteBehindStateWriter wbsw = new WriteBehindStateWriter(facesContext, 100000);
//		ResponseWriter newWriter = renderKit.createResponseWriter(wbsw, "text/html", null);
//		facesContext.setResponseWriter(newWriter);
//		return wbsw;
//	}
//	
//	@SuppressWarnings("unchecked")
//	protected MockExternalContext createMockExternalContext()
//	{
//		MockExternalContext ec = new MockExternalContext(servletContext, request, response);
//		ec.getRequestParameterMap().put(getViewStateParamName(), rawViewState);;
//		return ec;
//	}
//	
//	protected MockApplication createMockApplication()
//	{
//		return new MockApplication();
//	}
//	
//	protected MockHttpServletResponse createMockHttpServletResponse()
//	{
//		return new MockHttpServletResponse();
//	}
//	
//	protected MockHttpServletRequest createMockHttpServletRequest()
//	{
//		MockHttpServletRequest r =  new MockHttpServletRequest();
//		((MockHttpServletRequest) r).addParameter(getViewStateParamName(), rawViewState);
//		return r;
//	}
//	
//	protected MockServletContext createMockServletContext()
//	{
//		MockServletContext msc = new MockServletContext();
//		((MockServletContext) msc).addInitParameter(StateManager.STATE_SAVING_METHOD_PARAM_NAME, 
//				StateManager.STATE_SAVING_METHOD_CLIENT);
//		return msc;
//	}
//	
////	@SuppressWarnings("deprecation")
////	protected URL getJarURL() throws MalformedURLException
////	{
////		String facesLib = Deface.facesLibDir + version.getDirectory();
////		return  new File(facesLib).toURL();
////	}
////	
////	protected Object instantiate(String className)
////	{
////		Object o = null;
////		try
////		{
////			Class c = classLoader.loadClass(className);
////			o = c.newInstance();
////		}
////		catch (ClassNotFoundException e)
////		{
////			System.err.println(className + " not found: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		catch (InstantiationException e)
////		{
////			System.err.println(className + " not found: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		catch (IllegalAccessException e)
////		{
////			System.err.println(className + " not found: " + e.getLocalizedMessage());
////			e.printStackTrace();
////			System.exit(1);
////		}
////		
////		return o;
////	}
//
//	
//	public String generateServerSideTextTree()
//	{
//		return ObjectDumper.dumpObject(viewRoot, false);
//	}
//	
//	public String generateRawTextTree()
//	{
//		StringBuffer buffer = new StringBuffer();
//		ObjectInputStream ois;
//		try
//		{
//			ois = initInputStream(this.rawViewState);
//			try 
//			{
//				long stateTime = ois.readLong();
//				buffer.append("State time stamp: " + stateTime + "\n\n");
//			} 
//			catch (IOException ioe) 
//			{
//				// no state time
//			}
//			
//			buffer.append("Structure object: \n" + ObjectDumper.dumpObject(ois.readObject(), false) + "\n\n");
//			buffer.append("State object: \n" + ObjectDumper.dumpObject(ois.readObject(), false));
//		}
//		catch (IOException e)
//		{
//			buffer.append("Problem reading view state: " + e.getLocalizedMessage());
//		}
//		catch (ClassNotFoundException e)
//		{
//			buffer.append("Class not found in view state: " + e.getLocalizedMessage());
//		}
//		
//		return buffer.toString();
//	}
//	
//	private ObjectInputStream initInputStream(String stateString) throws IOException
//	{
//		InputStream bis = null;
//		ObjectInputStream os = null;
//		bis = new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(stateString.getBytes())));
//		os = new ObjectInputStream(bis);
//		try
//		{
//			Object structure = os.readObject();
//		} 
//		catch (Exception ioe) 
//		{
//			// assume input stream is not GZIP compressed)
//			bis = new Base64InputStream(new ByteArrayInputStream(stateString.getBytes()));
//			os = new ObjectInputStream(bis);
//		}
//		return os;
//	}
//	
////	static void xssTheView(UIComponentBase component) {
////	for (UIComponentBase child : component.getChildren().toArray(
////			new UIComponentBase[0])) {
////		xssTheView(child);
////		if (child instanceof HtmlForm) {
////			((HtmlForm) child).setOnmouseover("alert('hi')");
////		}
////
////		else if (child instanceof HtmlCommandLink) {
////			((HtmlCommandLink) child).setOnmouseover("alert('hi')");
////		}
////
////		else if (child instanceof HtmlGraphicImage) {
////			((HtmlGraphicImage) child).setOnmouseover("alert('hi')");
////		}
////
////		else if (child instanceof HtmlPanelGrid) {
////			((HtmlPanelGrid) child).setOnmouseover("alert('hi')");
////		}
////
////		else {
////			System.err.println("Unknown type");
////		}
////	}
////}
//}
