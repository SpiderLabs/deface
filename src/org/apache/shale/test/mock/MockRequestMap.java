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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Mock impementation of <code>Map</code> for the request scope
 * attributes managed by {@link MockExternalContext}.</p>
 *
 * $Id$
 */

class MockRequestMap implements Map {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new instance, exposing the attributes of the
     * specified request as a map.</p>
     *
     * @param request The HttpServletRequest to wrap
     */
    public MockRequestMap(HttpServletRequest request) {

        this.request = request;

    }


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The HttpServletRequest whose attributes we are exposing
     * as a Map.</p>
     */
    private HttpServletRequest request = null;


    // ------------------------------------------------------------- Map Methods


    /** {@inheritDoc} */
    public void clear() {

        Iterator keys = keySet().iterator();
        while (keys.hasNext()) {
            request.removeAttribute((String) keys.next());
        }

    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {

        return request.getAttribute(key(key)) != null;

    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {

        if (value == null) {
            return false;
        }
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = request.getAttribute((String) keys.nextElement());
            if (next == value) {
                return true;
            }
        }
        return false;

    }


    /** {@inheritDoc} */
    public Set entrySet() {

        Set set = new HashSet();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(request.getAttribute((String) keys.nextElement()));
        }
        return set;

    }


    /** {@inheritDoc} */
    public boolean equals(Object o) {

        return request.equals(o);

    }


    /** {@inheritDoc} */
    public Object get(Object key) {

        return request.getAttribute(key(key));

    }


    /** {@inheritDoc} */
    public int hashCode() {

        return request.hashCode();

    }


    /** {@inheritDoc} */
    public boolean isEmpty() {

        return size() < 1;

    }


    /** {@inheritDoc} */
    public Set keySet() {

        Set set = new HashSet();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return set;

    }


    /** {@inheritDoc} */
    public Object put(Object key, Object value) {

        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.setAttribute(skey, value);
        return previous;

    }


    /** {@inheritDoc} */
    public void putAll(Map map) {

        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            request.setAttribute(key, map.get(key));
        }

    }


    /** {@inheritDoc} */
    public Object remove(Object key) {

        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.removeAttribute(skey);
        return previous;

    }


    /** {@inheritDoc} */
    public int size() {

        int n = 0;
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;

    }


    /** {@inheritDoc} */
    public Collection values() {

        List list = new ArrayList();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(request.getAttribute((String) keys.nextElement()));
        }
        return list;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified key converted to a string.</p>
     *
     * @param key The key to convert
     */
    private String key(Object key) {

        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }

    }


}
