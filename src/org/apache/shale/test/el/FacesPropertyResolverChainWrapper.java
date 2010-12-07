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

package org.apache.shale.test.el;

import java.util.Iterator;
import java.util.List;

import javax.el.ELContext;
import javax.el.ELException;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyResolver;

/**
 * <p><code>ELResolver</code> implementation that wraps the legacy (JSF 1.1)
 * <code>PropertyResolver</code> chain.  See the JSF 1.2 Specification, section
 * 5.6.1.6, for requirements implemented by this class.</p>
 *
 * @since 1.0.4
 */
public class FacesPropertyResolverChainWrapper extends AbstractELResolver {
    

    /**
     * <p>Return the most general type this resolver accepts for the
     * <code>property</code> argument.</p>
     */
    public Class getCommonPropertyType(ELContext context, Object base) {

        if (base != null) {
            return null;
        } else {
            return Object.class;
        }

    }


    /**
     * <p>Return an <code>Iterator</code> over the attributes that this
     * resolver knows how to deal with.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     */
    public Iterator getFeatureDescriptors(ELContext context, Object base) {

        return null;

    }



    /**
     * <p>Evaluate with the legacy property resolver chain and return
     * the value.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     */
    public Class getType(ELContext context, Object base, Object property) {

        if ((base == null) || (property == null)) {
            return null;
        }

        context.setPropertyResolved(true);
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ELContext elContext = fcontext.getELContext();
        PropertyResolver pr = fcontext.getApplication().getPropertyResolver();

        if ((base instanceof List) || base.getClass().isArray()) {
            Integer index = (Integer) fcontext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            try {
                return pr.getType(base, index.intValue());
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        } else {
            try {
                return pr.getType(base, property);
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        }

    }


    /**
     * <p>Evaluate with the legacy property resolver chain and return
     * the value.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     */
    public Object getValue(ELContext context, Object base, Object property) {

        if ((base == null) || (property == null)) {
            return null;
        }

        context.setPropertyResolved(true);
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ELContext elContext = fcontext.getELContext();
        PropertyResolver pr = fcontext.getApplication().getPropertyResolver();

        if ((base instanceof List) || base.getClass().isArray()) {
            Integer index = (Integer) fcontext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            try {
                return pr.getValue(base, index.intValue());
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        } else {
            try {
                return pr.getValue(base, property);
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        }

    }


    /**
     * <p>Return <code>true</code> if the specified property is read only.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     */
    public boolean isReadOnly(ELContext context, Object base, Object property) {

        if ((base == null) || (property == null)) {
            return false;
        }

        context.setPropertyResolved(true);
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ELContext elContext = fcontext.getELContext();
        PropertyResolver pr = fcontext.getApplication().getPropertyResolver();

        if ((base instanceof List) || base.getClass().isArray()) {
            Integer index = (Integer) fcontext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            try {
                return pr.isReadOnly(base, index.intValue());
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        } else {
            try {
                return pr.isReadOnly(base, property);
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        }

    }



    /**
     * <p>Set the value of a property for the specified name.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     * @param value New value to be set
     */
    public void setValue(ELContext context, Object base, Object property, Object value) {

        if ((base == null) || (property == null)) {
            return;
        }

        context.setPropertyResolved(true);
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ELContext elContext = fcontext.getELContext();
        PropertyResolver pr = fcontext.getApplication().getPropertyResolver();

        if ((base instanceof List) || base.getClass().isArray()) {
            Integer index = (Integer) fcontext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            try {
                pr.setValue(base, index.intValue(), value);
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        } else {
            try {
                pr.setValue(base, property, value);
            } catch (EvaluationException e) {
                context.setPropertyResolved(false);
                throw new ELException(e);
            }
        }

    }


}
