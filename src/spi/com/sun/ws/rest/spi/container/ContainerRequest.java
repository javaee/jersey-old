/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.ws.rest.spi.container;

import com.sun.ws.rest.api.core.HttpRequestContext;
import java.net.URI;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

/**
 * The HTTP request context supplied by the container.
 * <p>
 * This interface is a place for holder for any container specific HTTP request 
 * functionality that may be required in the future.
 *
 * @author Paul.Sandoz@Sun.Com
 */
public interface ContainerRequest extends HttpRequestContext {
    
    /**
     * Add templates values to the HTTP request.
     * <p>
     * The template names and values must be in encoded form.
     *
     * @param values the map of template values.
     */
    public void addTemplateValues(Map<String, String> values);
    
    /**
     * TODO
     * The following methods are temporary until such methods are
     * available on UriInfo, and thus can be removed later on.
     */
    public URI getComplete();
    
    public UriBuilder getCompleteBuilder();
}