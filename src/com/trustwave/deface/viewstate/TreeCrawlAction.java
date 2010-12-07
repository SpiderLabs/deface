/*******************************************************************************
 * Copyright (c) 2010 Trustwave Holdings, Inc.
 *******************************************************************************/

package com.trustwave.deface.viewstate;

import javax.faces.component.UIComponentBase;

public interface TreeCrawlAction
{
	public void handleNode(UIComponentBase child);
}
