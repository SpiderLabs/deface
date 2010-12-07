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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>Mock implementation of <code>ExternalContext</code>.</p>
 *
 * $Id$
 */

public class MockExternalContext extends ExternalContext {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a wrapper instance.</p>
     *
     * @param context <code>ServletContext</code> for this application
     * @param request <code>HttpServetRequest</code> for this request
     * @param response <code>HttpServletResponse</code> for this request
     */
    public MockExternalContext(ServletContext context,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        this.context = context;
        this.request = request;
        this.response = response;
        applicationMap = new MockApplicationMap(context);
        requestMap = new MockRequestMap(request);

    }


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    private Map applicationMap = null;
    private ServletContext context = null;
    protected HttpServletRequest request = null;
    private Map requestMap = null;
    protected HttpServletResponse response = null;
    private Map sessionMap = null;
    private Map requestCookieMap = new HashMap();
    private Map requestParameterMap = new HashMap();


    // ------------------------------------------------- setters for the mock object


    /**
     * <p>Add a new cookie for this request.</p>
     *
     * @param cookie The new cookie
     */
    public void addRequestCookieMap(Cookie cookie) {
        requestParameterMap.put(cookie.getName(), cookie);
    }


    /**
     * <p>Set the request cookie map for this request.</p>
     *
     * @param map The new request cookie map
     */
    public void setRequestCookieMap(Map map) {
        requestParameterMap = map;
    }


    /**
     * <p>Add the specified request parameter for this request.</p>
     *
     * @param key Parameter name
     * @param value Parameter value
     */
    public void addRequestParameterMap(String key, String value) {
        requestParameterMap.put(key, value);
    }


    /**
     * <p>Set the request parameter map for this request.</p>
     *
     * @param map The new request parameter map
     */
    public void setRequestParameterMap(Map map) {
        requestParameterMap = map;
    }


    // ------------------------------------------------- ExternalContext Methods


    /** {@inheritDoc} */
    public void dispatch(String requestURI)
      throws IOException, FacesException {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String encodeActionURL(String sb) {

        return sb;

    }


    /** {@inheritDoc} */
    public String encodeNamespace(String aValue) {

        return aValue;

    }


    /** {@inheritDoc} */
    public String encodeResourceURL(String sb) {

        return sb;

    }


    /** {@inheritDoc} */
    public Map getApplicationMap() {

        return this.applicationMap;

    }


    /** {@inheritDoc} */
    public String getAuthType() {

        return request.getAuthType();

    }


    /** {@inheritDoc} */
    public Object getContext() {

        return context;

    }


    /** {@inheritDoc} */
    public String getInitParameter(String name) {

        return context.getInitParameter(name);

    }


    /** {@inheritDoc} */
    public Map getInitParameterMap() {

        Map parameterMap = new HashMap();
        Enumeration names = context.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            parameterMap.put(name, context.getInitParameter(name));
        }
        return Collections.unmodifiableMap(parameterMap);

    }


    /** {@inheritDoc} */
    public String getRemoteUser() {

        return request.getRemoteUser();

    }


    /** {@inheritDoc} */
    public Object getRequest() {

        return request;

    }


    /** {@inheritDoc} */
    public String getRequestContextPath() {

        return request.getContextPath();

    }


    /** {@inheritDoc} */
    public Map getRequestCookieMap() {

        return requestCookieMap;

    }


    /** {@inheritDoc} */
    public Map getRequestHeaderMap() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Map getRequestHeaderValuesMap() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Locale getRequestLocale() {

        return request.getLocale();

    }


    /** {@inheritDoc} */
    public Iterator getRequestLocales() {

        return new LocalesIterator(request.getLocales());

    }


    /** {@inheritDoc} */
    public Map getRequestMap() {

        return requestMap;

    }


    /** {@inheritDoc} */
    public Map getRequestParameterMap() {

        return requestParameterMap;

    }


    /** {@inheritDoc} */
    public Iterator getRequestParameterNames() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Map getRequestParameterValuesMap() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getRequestPathInfo() {

        return request.getPathInfo();

    }


    /** {@inheritDoc} */
    public String getRequestServletPath() {

        return request.getServletPath();

    }


    /** {@inheritDoc} */
    public URL getResource(String path) throws MalformedURLException {

        return context.getResource(path);

    }


    /** {@inheritDoc} */
    public InputStream getResourceAsStream(String path) {

        return context.getResourceAsStream(path);

    }


    /** {@inheritDoc} */
    public Set getResourcePaths(String path) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Object getResponse() {

        return response;

    }


    /** {@inheritDoc} */
    public Object getSession(boolean create) {

        return request.getSession(create);

    }


    /** {@inheritDoc} */
    public Map getSessionMap() {

        if (sessionMap == null) {
            HttpSession session = request.getSession(true);
            sessionMap = new MockSessionMap(session);
        }
        return sessionMap;

    }


    /** {@inheritDoc} */
    public java.security.Principal getUserPrincipal() {

        return request.getUserPrincipal();

    }


    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {

        return request.isUserInRole(role);

    }


    /** {@inheritDoc} */
    public void log(String message) {

        context.log(message);

    }


    /** {@inheritDoc} */
    public void log(String message, Throwable throwable) {

        context.log(message, throwable);

    }


    /** {@inheritDoc} */
    public void redirect(String requestURI)
      throws IOException {

        throw new UnsupportedOperationException();

    }


    /**
     * <p>Iterator implementation that wraps an enumeration
     * of Locales for the current request.</p>
     */
    private class LocalesIterator implements Iterator {

        /**
         * <p>Construct an iterator wrapping the specified
         * enumeration.</p>
         *
         * @param locales Locales enumeration to wrap
         */
        public LocalesIterator(Enumeration locales) {
            this.locales = locales;
        }

        /**
         * <p>The enumeration to be wrapped.</p>
         */
        private Enumeration locales;

        /** {@inheritDoc} */
        public boolean hasNext() { return locales.hasMoreElements(); }

        /** {@inheritDoc} */
        public Object next() { return locales.nextElement(); }

        /** {@inheritDoc} */
        public void remove() { throw new UnsupportedOperationException(); }

    }


}
