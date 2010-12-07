/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shale.test.mock;

import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

/**
 * <p>Mock implementation of <code>Lifecycle</code>.</p>
 *
 * $Id$
 */

public class MockLifecycle extends Lifecycle {


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>List of event listeners for this instance.</p>
     */
    private List listeners = new ArrayList();


    // ------------------------------------------------------- Lifecycle Methods


    /** {@inheritDoc} */
    public void addPhaseListener(PhaseListener listener) {

        listeners.add(listener);

    }


    /** {@inheritDoc} */
    public void execute(FacesContext context) throws FacesException {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public PhaseListener[] getPhaseListeners() {

        return (PhaseListener[]) listeners.toArray(new PhaseListener[listeners.size()]);

    }


    /** {@inheritDoc} */
    public void removePhaseListener(PhaseListener listener) {

        listeners.remove(listener);

    }


    /** {@inheritDoc} */
    public void render(FacesContext context) throws FacesException {

        throw new UnsupportedOperationException();

    }


}
