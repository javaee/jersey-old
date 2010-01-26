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

package com.sun.jersey.impl.container.config;

import com.sun.jersey.spi.service.ServiceFinder;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class ServiceFinderTest extends AbstractConfigTester {
    
    public ServiceFinderTest(String testName) {
        super(testName);
    }
    
    public void testJarTopLevel() throws Exception {
        Map<String, String> m = new HashMap<String, String>();
        m.put("com/sun/jersey/impl/container/config/jaxrs-components",
                "META-INF/services/jaxrs-components");
        m.put("com/sun/jersey/impl/container/config/toplevel/PublicRootResourceClass.class",
                "com/sun/jersey/impl/container/config/toplevel/PublicRootResourceClass.class");

        ClassLoader cl = createClassLoader("target/test-classes/",
                m
                );
        
        ServiceFinder<?> rc = createServiceFinder(cl, "jaxrs-components");

        Set<Class<?>> s = new HashSet<Class<?>>();
        for (Class<?> c : rc.toClassArray()) 
            s.add(c);
                
        assertTrue(s.contains(
                cl.loadClass("com.sun.jersey.impl.container.config.toplevel.PublicRootResourceClass")));
        assertEquals(1, s.size());
    }
    
    
    private ServiceFinder<?> createServiceFinder(ClassLoader cl, String serviceName) throws IOException {
        ClassLoader ocl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        try {
            Class prc = cl.loadClass("com.sun.jersey.spi.service.ServiceFinder");
            Method m = prc.getMethod("find", String.class);

            return (ServiceFinder<?>)m.invoke(null, serviceName);
            // return new PackagesResourceConfig(packages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(ocl);
        }
    }
    
    private ClassLoader createClassLoader(String base, Map<String, String> entries) throws IOException {
        return createClassLoader(Suffix.jar, base, entries);
    }
    
    private ClassLoader createClassLoader(Suffix s, String base, Map<String, String> entries) throws IOException {
        URL[] us = new URL[1];
        us[0] = createJarFile(s, base, entries).toURI().toURL();
        return new PackageClassLoader(us);
    } 
    
    private static class PackageClassLoader extends URLClassLoader {
        PackageClassLoader(URL[] urls) {
            super(urls, null);
        }
        
        public Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                return super.findClass(name);
            } catch (ClassNotFoundException e) {
                return getSystemClassLoader().loadClass(name);
            }
        }        
    }
}
