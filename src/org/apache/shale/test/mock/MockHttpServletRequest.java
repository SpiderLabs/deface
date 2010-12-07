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

import java.io.BufferedReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * <p>Mock implementation of <code>HttpServletContext</code>.</p>
 *
 * $Id$
 */

public class MockHttpServletRequest implements HttpServletRequest {


    // ------------------------------------------------------------ Constructors


    public MockHttpServletRequest() {

        super();

    }


    public MockHttpServletRequest(HttpSession session) {

        super();
        setHttpSession(session);

    }


    public MockHttpServletRequest(String contextPath, String servletPath,
                                  String pathInfo, String queryString) {

        super();
        setPathElements(contextPath, servletPath, pathInfo, queryString);

    }



    public MockHttpServletRequest(String contextPath, String servletPath,
                                  String pathInfo, String queryString,
                                  HttpSession session) {

        super();
        setPathElements(contextPath, servletPath, pathInfo, queryString);
        setHttpSession(session);

    }


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Add a new listener instance that should be notified about
     * attribute changes.</p>
     *
     * @param listener The new listener to register
     */
    public void addAttributeListener(ServletRequestAttributeListener listener) {
        attributeListeners.add(listener);
    }


    /**
     * <p>Add a date-valued header for this request.</p>
     *
     * @param name Header name
     * @param value Header value
     */
    public void addDateHeader(String name, long value) {

        headers.add(name + ": " + formatDate(value));

    }


    /**
     * <p>Add a String-valued header for this request.</p>
     *
     * @param name Header name
     * @param value Header value
     */
    public void addHeader(String name, String value) {

        headers.add(name + ": " + value);

    }


    /**
     * <p>Add an integer-valued header for this request.</p>
     *
     * @param name Header name
     * @param value Header value
     */
    public void addIntHeader(String name, int value) {

        headers.add(name + ": " + value);

    }


    /**
     * <p>Add a request parameter for this request.</p>
     *
     * @param name Parameter name
     * @param value Parameter value
     */
    public void addParameter(String name, String value) {

        String[] values = (String[]) parameters.get(name);
        if (values == null) {
            String[] results = new String[] { value };
            parameters.put(name, results);
            return;
        }
        String[] results = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        parameters.put(name, results);

    }


    /**
     * <p>Return the <code>ServletContext</code> associated with
     * this request.</p>
     */
    public ServletContext getServletContext() {

        return this.servletContext;

    }


    /**
     * <p>Set the <code>HttpSession</code> associated with this request.</p>
     *
     * @param session The new session
     */
    public void setHttpSession(HttpSession session) {

        this.session = session;

    }


    /**
     * <p>Set the <code>Locale</code> associated with this request.</p>
     *
     * @param locale The new locale
     */
    public void setLocale(Locale locale) {

        this.locale = locale;

    }


    /**
     * <p>Set the parsed path elements associated with this request.</p>
     *
     * @param contextPath The context path
     * @param servletPath The servlet path
     * @param pathInfo The extra path information
     * @param queryString The query string
     */
    public void setPathElements(String contextPath, String servletPath,
                                String pathInfo, String queryString) {

        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
        this.queryString = queryString;

    }


    /**
     * <p>Set the <code>ServletContext</code> associated with this request.</p>
     *
     * @param servletContext The new servlet context
     */
    public void setServletContext(ServletContext servletContext) {

        this.servletContext = servletContext;

    }


    /**
     * <p>Set the <code>Principal</code> associated with this request.</p>
     *
     * @param principal The new Principal
     */
    public void setUserPrincipal(Principal principal) {

        this.principal = principal;

    }


    // ------------------------------------------------------ Instance Variables


    private List attributeListeners = new ArrayList();
    private HashMap attributes = new HashMap();
    private String contextPath = null;
    private List headers = new ArrayList();
    private Locale locale = null;
    private HashMap parameters = new HashMap();
    private String pathInfo = null;
    private Principal principal = null;
    private String queryString = null;
    private ServletContext servletContext = null;
    private String servletPath = null;
    private HttpSession session = null;


    // ---------------------------------------------- HttpServletRequest Methods


    /** {@inheritDoc} */
    public String getAuthType() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getContextPath() {

        return contextPath;

    }


    /** {@inheritDoc} */
    public Cookie[] getCookies() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public long getDateHeader(String name) {

        String match = name + ":";
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            if (header.startsWith(match)) {
                return parseDate(header.substring(match.length() + 1).trim());
            }
        }
        return (long) -1;

    }


    /** {@inheritDoc} */
    public String getHeader(String name) {

        String match = name + ":";
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            if (header.startsWith(match)) {
                return header.substring(match.length() + 1).trim();
            }
        }
        return null;

    }


    /** {@inheritDoc} */
    public Enumeration getHeaderNames() {

        Vector values = new Vector();
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            int colon = header.indexOf(':');
            if (colon >= 0) {
                String name = header.substring(0, colon).trim();
                if (!values.contains(name)) {
                    values.add(name);
                }
            }
        }
        return values.elements();

    }


    /** {@inheritDoc} */
    public Enumeration getHeaders(String name) {

        String match = name + ":";
        Vector values = new Vector();
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            if (header.startsWith(match)) {
                values.add(header.substring(match.length() + 1).trim());
            }
        }
        return values.elements();

    }


    /** {@inheritDoc} */
    public int getIntHeader(String name) {

        String match = name + ":";
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            if (header.startsWith(match)) {
                return Integer.parseInt(header.substring(match.length() + 1).trim());
            }
        }
        return -1;

    }


    /** {@inheritDoc} */
    public String getMethod() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getPathInfo() {

        return pathInfo;

    }


    /** {@inheritDoc} */
    public String getPathTranslated() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getQueryString() {

        return queryString;

    }


    /** {@inheritDoc} */
    public String getRemoteUser() {

        if (principal != null) {
            return principal.getName();
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public String getRequestedSessionId() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getRequestURI() {

        StringBuffer sb = new StringBuffer();
        if (contextPath != null) {
            sb.append(contextPath);
        }
        if (servletPath != null) {
            sb.append(servletPath);
        }
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public StringBuffer getRequestURL() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getServletPath() {

        return (servletPath);

    }


    /** {@inheritDoc} */
    public HttpSession getSession() {

        return getSession(true);

    }


    /** {@inheritDoc} */
    public HttpSession getSession(boolean create) {

        if (create && (session == null)) {
            throw new UnsupportedOperationException();
        }
        return session;

    }


    /** {@inheritDoc} */
    public Principal getUserPrincipal() {

        return principal;

    }


    /** {@inheritDoc} */
    public boolean isRequestedSessionIdFromCookie() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isRequestedSessionIdFromUrl() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isRequestedSessionIdFromURL() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isRequestedSessionIdValid() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {

        throw new UnsupportedOperationException();

    }


    // ------------------------------------------------- ServletRequest Methods


    /** {@inheritDoc} */
    public Object getAttribute(String name) {

        return attributes.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames() {

        return new MockEnumeration(attributes.keySet().iterator());

    }


    /** {@inheritDoc} */
    public String getCharacterEncoding() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public int getContentLength() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getContentType() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public ServletInputStream getInputStream() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Locale getLocale() {

        return locale;

    }


    /** {@inheritDoc} */
    public Enumeration getLocales() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getLocalAddr() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getLocalName() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public int getLocalPort() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getParameter(String name) {

        String[] values = (String[]) parameters.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public Map getParameterMap() {

        return parameters;

    }


    /** {@inheritDoc} */
    public Enumeration getParameterNames() {

        return new MockEnumeration(parameters.keySet().iterator());

    }


    /** {@inheritDoc} */
    public String[] getParameterValues(String name) {

        return (String[]) parameters.get(name);

    }


    /** {@inheritDoc} */
    public String getProtocol() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public BufferedReader getReader() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getRealPath(String path) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getRemoteAddr() {

        // i figure testing never assumes a specific remote - so anything works
        return "1.2.3.4";

    }


    /** {@inheritDoc} */
    public String getRemoteHost() {

        // i figure testing never assumes a specific remote - so anything works
        return "ShaleServer";

    }


    /** {@inheritDoc} */
    public int getRemotePort() {

        // i figure testing never assumes a specific remote - so anything works
        return 46123;

    }


    /** {@inheritDoc} */
    public RequestDispatcher getRequestDispatcher(String path) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getScheme() {

        return ("http");

    }


    /** {@inheritDoc} */
    public String getServerName() {

        return ("localhost");

    }


    /** {@inheritDoc} */
    public int getServerPort() {

        return (8080);

    }


    /** {@inheritDoc} */
    public boolean isSecure() {

        return false;

    }


    /** {@inheritDoc} */
    public void removeAttribute(String name) {

        if (attributes.containsKey(name)) {
            Object value = attributes.remove(name);
            fireAttributeRemoved(name, value);
        }

    }


    /** {@inheritDoc} */
    public void setAttribute(String name, Object value) {

        if (name == null) {
            throw new IllegalArgumentException("Attribute name cannot be null");
        }
        if (value == null) {
            removeAttribute(name);
            return;
        }
        if (attributes.containsKey(name)) {
            Object oldValue = attributes.get(name);
            attributes.put(name, value);
            fireAttributeReplaced(name, oldValue);
        } else {
            attributes.put(name, value);
            fireAttributeAdded(name, value);
        }

    }


    /** {@inheritDoc} */
    public void setCharacterEncoding(String name) {

        throw new UnsupportedOperationException();

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Fire an attribute added event to interested listeners.</p>
     *
     * @param key Attribute key whose value was added
     * @param value The new attribute value
     */
    private void fireAttributeAdded(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        ServletRequestAttributeEvent event =
                new ServletRequestAttributeEvent(getServletContext(), this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            ServletRequestAttributeListener listener =
                    (ServletRequestAttributeListener) listeners.next();
            listener.attributeAdded(event);
        }
    }


    /**
     * <p>Fire an attribute removed event to interested listeners.</p>
     *
     * @param key Attribute key whose value was removed
     * @param value Attribute value that was removed
     */
    private void fireAttributeRemoved(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        ServletRequestAttributeEvent event =
                new ServletRequestAttributeEvent(getServletContext(), this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            ServletRequestAttributeListener listener =
                    (ServletRequestAttributeListener) listeners.next();
            listener.attributeRemoved(event);
        }
    }


    /**
     * <p>Fire an attribute replaced event to interested listeners.</p>
     *
     * @param key Attribute key whose value was replaced
     * @param value The original value
     */
    private void fireAttributeReplaced(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        ServletRequestAttributeEvent event =
                new ServletRequestAttributeEvent(getServletContext(), this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            ServletRequestAttributeListener listener =
                    (ServletRequestAttributeListener) listeners.next();
            listener.attributeReplaced(event);
        }
    }


    /**
     * <p>The date formatting helper we will use in <code>httpTimestamp()</code>.
     * Note that usage of this helper must be synchronized.</p>
     */
    private static SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    /**
     * <p>Return a properly formatted String version of the specified
     * date/time, formatted as required by the HTTP specification.</p>
     *
     * @param date Date/time, expressed as milliseconds since the epoch
     */
    private String formatDate(long date) {
        return format.format(new Date(date));
    }


    /**
     * <p>Return a date/time value, parsed from the specified String.</p>
     *
     * @param date Date/time, expressed as a String
     */
    private long parseDate(String date) {
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException(date);
        }
    }


}
