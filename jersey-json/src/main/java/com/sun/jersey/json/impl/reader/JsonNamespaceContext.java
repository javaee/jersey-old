/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.json.impl.reader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author japod
 */
public class JsonNamespaceContext implements NamespaceContext {
    
    static final String PrefixPREFIX = "ns";

    private final Map<String, String> ns2pMap = new HashMap<String, String>();
    private final Map<String, String> p2nsMap = new HashMap<String, String>();
    private int nsCount = 0;

    protected int getNamespaceCount() {
        return nsCount;
    }

    public String getNamespaceURI(String prefix) {
        if (p2nsMap.containsKey(prefix)) {
            return p2nsMap.get(prefix);
        }
        return null;
    }

    public String getPrefix(String namespaceURI) {
        if (ns2pMap.containsKey(namespaceURI)) {
            return ns2pMap.get(namespaceURI);
        } else {
            final String newPrefix = PrefixPREFIX + ++nsCount;
            ns2pMap.put(namespaceURI, newPrefix);
            p2nsMap.put(newPrefix, namespaceURI);
            return newPrefix;
        }
    }

    public Iterator getPrefixes(String namespaceURI) {
        return p2nsMap.keySet().iterator();
    }

}
