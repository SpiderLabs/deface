/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.viewstate_old;
//package com.trustwave.deface.viewstate;
//
//import java.io.IOException;
//
//import javax.faces.FactoryFinder;
//import javax.faces.application.StateManager;
//import javax.faces.application.ViewHandler;
//import javax.faces.render.ResponseStateManager;
//
//
//public class Mojarra_1_2_14 extends AbstractViewState
//{
//	private ViewHandler viewHandler;
//	private StateManager stateManager;
//	private final static String viewHandlerClassName = "com.sun.faces.application.ViewHandlerImpl";
//	private final static String stateManagerClassName = "com.sun.faces.application.StateManagerImpl";
//	private final static String responseStateManagerClassName = "com.sun.faces.renderkit.ResponseStateManagerImpl";
//	private static final String VIEW_STATE_PARAM = "javax.faces.ViewState";
//	
//	public Mojarra_1_2_14(String viewState)
//	{
//		super(Version.Mojarra_1_2_14, viewState);
//
//	}
//	
//	@Override
//	protected void initializeFactories()
//	{
//		FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
//			"com.sun.faces.renderkit.RenderKitFactoryImpl");
//	}
//
//
//	@Override
//	protected String[] getRequiredClasses()
//	{
//		return new String[] {viewHandlerClassName, stateManagerClassName, responseStateManagerClassName};
//	}
//
//	
//	@Override
//	protected StateManager getStateManager()
//	{
//		if (stateManager == null)
//		{
//			stateManager = (StateManager) instantiate(stateManagerClassName);
//		}
//		return stateManager;
//	}
//
//	
//	@Override
//	protected ViewHandler getViewHandler()
//	{
//		if (viewHandler == null)
//		{
//			viewHandler = (ViewHandler) instantiate(viewHandlerClassName);
//		}
//		return viewHandler;
//	}
//
//
//	@Override
//	public String serializeToString()
//	{
//		String text;
//		Object savedView = stateManager.saveView(facesContext);
//		ResponseStateManager r = (ResponseStateManager) instantiate(responseStateManagerClassName);
//		try
//		{
//			r.writeState(facesContext, savedView);
//			text = stateWriter.toString();
//		}
//		catch (IOException e)
//		{
//			text = "Error serializing view state: " + e.getLocalizedMessage();
//		}
//		return text;
//	}
//
//	@Override
//	protected String getViewStateParamName()
//	{
//		return VIEW_STATE_PARAM;
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
//
//
//}
