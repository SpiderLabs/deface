/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.gui;

import java.io.IOException;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


import com.trustwave.deface.utils.ObjectDeserizer;
import com.trustwave.deface.utils.ObjectDumper;
import com.trustwave.deface.viewstate.*;

public class DefaceGUI {

	private static DefaceGUI gui;
	private Shell shell;
	private Text outputText;
	private Text attackText;
	private Text inputText;
//	private Button radios[]; 
	private Group inputGroup;
	private Group outputGroup;
	
	private static final int SIZE = 100;
	
	
	String elAttack = 
					"session - #{session}\n" + 
					"servletContext - #{servletContext}\n" +
					"request - #{request}\n" +
					"response - #{response}\n" +
					"param - #{param}\n" +
					"paramValues - #{paramValues}\n" +
					"header - #{header}\n" +
					"headerValues - #{headerValues}\n" +
					"cookie - #{cookie}\n" +
					"initParam - #{initParam}\n" +
					"pageScope - #{pageScope}\n" +
					"requestScope - #{requestScope}\n" +
					"sessionScope - #{sessionScope}\n" +
					"applicationScope - #{applicationScope}\n" +
				//	" - #{}\n" +
					"";
					
	
	public static void showWindow() {
		if (gui == null)
		{
			Display display = new Display();
			gui = new DefaceGUI();
			gui.open(display);
			display.dispose();
		}
	}

	public void open(Display display) {
		try{
			shell = new Shell(display);
			shell.setText("Deface Tool");
			shell.setLayout(new FillLayout());
	
			ScrolledComposite sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
			SashForm parent = new SashForm(sc, SWT.HORIZONTAL);
			sc.setContent(parent);
			parent.setLayout(new GridLayout(2, true));
	
			inputGroup = new Group(parent, SWT.NONE);
			inputGroup.setText("JSF Version");
			inputGroup.setLayout(new GridLayout(1, false));
			inputGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
			
			outputGroup = new Group(parent, SWT.NONE);
			outputGroup.setText("Results:");
			outputGroup.setLayout(new GridLayout(1, false));
			outputGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//			createJSFVersionRadios();
			createViewStatePaste();
			createCommandButtons();

	

			sc.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
	
			Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			Rectangle monitorArea = shell.getMonitor().getClientArea();
			shell.setSize(Math.min(size.x, monitorArea.width - 20), Math.min(
					size.y, monitorArea.height - 20));
			shell.open();
		} catch (Throwable t) {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			t.printStackTrace(System.out);
			System.exit(1);
		}

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	
	
	void createViewStatePaste() {

		// TextTransfer
		{
			Label l = new Label(inputGroup, SWT.NONE);
			l.setText("This tool is intended to work with Apache MyFaces 1.2.8.\n\nPaste view state here:");
			
			inputText = new Text(inputGroup, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL );
	
			GridData data = new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL);
			data.heightHint = data.widthHint = SIZE;
			inputText.setLayoutData(data);
		}
			
		{
			Label l = new Label(inputGroup, SWT.NONE);
			l.setText("EL attack code:");
			
			attackText = new Text(inputGroup, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL );
			attackText.setText(elAttack);
	
			GridData data = new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL);
			data.heightHint = data.widthHint = SIZE;
			attackText.setLayoutData(data);
		}
		
		Label l1 = new Label(outputGroup, SWT.NONE);
		l1.setText("Deface Status"); 
		outputText = new Text(outputGroup, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		outputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		

	}
	
	private void createCommandButtons()
	{
		{
			Button clearButton = new Button(inputGroup, SWT.PUSH);
			clearButton.setText("Clear view state");
			clearButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					inputText.setText("");
				}
			});
		}

		{
			Button serverDecodeButton = new Button(inputGroup, SWT.PUSH);
			serverDecodeButton.setText("Decode view state (server-side estimate)");
			serverDecodeButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try
					{
						ViewStateWrapper viewState = new ViewStateWrapper(inputText.getText());
						outputText.setText(viewState.generateServerSideTextTree());
					}
					catch (Exception e1)
					{
						DisplayText("Error", e1.toString());
					}
				}
			});
		}

		{
			Button stateDecodeButton = new Button(inputGroup, SWT.PUSH);
			stateDecodeButton.setText("Decode view state (raw view state only)");
			stateDecodeButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try
					{
						ViewStateWrapper viewState = new ViewStateWrapper(inputText.getText());
						outputText.setText(viewState.generateRawTextTree());
					}
					catch (Exception e1)
					{
						DisplayText("Error", e1.toString());
					}
				}
			});
		}

		{
			Button streamDecodeButton = new Button(inputGroup, SWT.PUSH);
			streamDecodeButton.setText("Decode Java Object Stream");
			streamDecodeButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try
					{
						Object o = ObjectDeserizer.deserialize(inputText.getText());
						outputText.setText(ObjectDumper.dumpObject(o, true));
					}
					catch (Exception e1)
					{
						DisplayText("Error", e1.toString());
					}
				}
			});
		}

		{
			Button xssButton = new Button(inputGroup, SWT.PUSH);
			xssButton.setText("Generate XSS attack");
			xssButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try
					{
						ViewStateWrapper viewState = new ViewStateWrapper(inputText.getText());
						viewState.insertXSSPoC();
						outputText.setText(viewState.serializeToString());
					}
					catch (Exception e1)
					{
						DisplayText("Error", e1.toString());
					}
				}
			});
		}

		{
			Button sessionButton = new Button(inputGroup, SWT.PUSH);
			sessionButton.setText("Generate session data attack");
			sessionButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try
					{
						ViewStateWrapper viewState = new ViewStateWrapper(inputText.getText());
						viewState.insertSessionVarsPoC(attackText.getText());
						outputText.setText(viewState.serializeToString());
					}
					catch (Exception e1)
					{
						DisplayText("Error", e1.toString());
					}
				}
			});
		}

	}
	
	public static void DisplayText(final String title, final String text)
	{
		gui.shell.getDisplay().syncExec(new Thread(){
			public void run()
			{
				MessageBox md = new MessageBox(gui.shell);
				md.setText(title);
				md.setMessage(text);
				md.open();
			}
		});
	}
	
//	private Version getJSFVersionSelected(){
//		for(Button b: radios)
//		{
//			if (b.getSelection()) return (Version) b.getData();
//		}
//		throw new RuntimeException("Problem with version radios");
//	}
//
//	void createJSFVersionRadios() {
//		radios = new Button[Version.values().length];
//		/* Create the modal style buttons */
//		int i = 0;
//		for (Version version: Version.values())
//		{
//			Button b = new Button(inputGroup, SWT.RADIO);
//			b.setText(version.getText());
//			b.setData(version);
//			if (i == 0) b.setSelection(true);
//			radios[i++] = b;
//		}
//	}
}