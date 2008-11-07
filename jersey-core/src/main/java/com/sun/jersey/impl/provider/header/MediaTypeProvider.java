/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.jersey.impl.provider.header;

import com.sun.jersey.core.header.reader.HttpHeaderReader;
import com.sun.jersey.impl.provider.header.WriterUtil;
import com.sun.jersey.spi.HeaderDelegateProvider;
import java.text.ParseException;
import java.util.Map;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Marc.Hadley@Sun.Com
 */
public class MediaTypeProvider implements HeaderDelegateProvider<MediaType> {
    
    public boolean supports(Class<?> type) {
        return MediaType.class.isAssignableFrom(type);
    }

    public String toString(MediaType header) {
        StringBuilder b = new StringBuilder();
        b.append(header.getType()).
            append('/').
            append(header.getSubtype());
        for (Map.Entry<String, String> e : header.getParameters().entrySet()) {
            b.append(';').
                append(e.getKey()).
                append('=');
            WriterUtil.appendQuotedMediaType(b, e.getValue());
        }
        return b.toString();
    }

    public MediaType fromString(String header) {
        if (header == null)
            throw new IllegalArgumentException("Media type is null");
        
        try {
            HttpHeaderReader reader = HttpHeaderReader.newInstance(header);
            // Skip any white space
            reader.hasNext();

            // Get the type
            String type = reader.nextToken();
            reader.nextSeparator('/');
            // Get the subtype
            String subType = reader.nextToken();

            Map<String, String> params = null;

            if (reader.hasNext())
                params = HttpHeaderReader.readParameters(reader);

            return new MediaType(type, subType, params);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(
                    "Error parsing media type '" + header + "'", ex);
        }
    }
}