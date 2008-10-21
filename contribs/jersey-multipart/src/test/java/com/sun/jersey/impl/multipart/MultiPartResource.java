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

package com.sun.jersey.impl.multipart;

import com.sun.jersey.api.multipart.BodyPart;
import com.sun.jersey.api.multipart.MultiPart;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Providers;

/**
 * <p>Resource file for {@link MultiPartReaderWriterTest}.</p>
 */
@Path("/multipart")
public class MultiPartResource {

    @Context
    private Providers providers = null;

    @Path("zero")
    @GET
    @Produces("text/plain")
    public String zero() {
        return "Hello, world\r\n";
    }

    @Path("one")
    @GET
    @Produces("multipart/mixed")
    public Response one() {
        MultiPart entity = new MultiPart();
        // Exercise manually adding part(s) to the bodyParts property
        BodyPart part = new BodyPart("This is the only segment", new MediaType("text", "plain"));
        entity.getBodyParts().add(part);
        return Response.ok(entity).type("multipart/mixed").build();
    }

    @Path("two")
    @GET
    @Produces("multipart/mixed")
    public Response two() {
        // Exercise builder pattern with default content type
        return Response.ok(new MultiPart().
                             bodyPart("This is the first segment", new MediaType("text", "plain")).
                             bodyPart("<outer><inner>value</inner></outer>", new MediaType("text", "xml"))).build();
    }

    @Path("three")
    @GET
    @Produces("multipart/mixed")
    public Response three() {
        // Exercise builder pattern with explicit content type
        MultiPartBean bean = new MultiPartBean("myname", "myvalue");
        return Response.ok(new MultiPart().
                             type(new MediaType("multipart", "mixed")).
                             bodyPart("This is the first segment", new MediaType("text", "plain")).
                             bodyPart(bean, new MediaType("x-application", "x-format"))).build();
    }

    @Path("four")
    @PUT
    @Produces("text/plain")
    public Response four(MultiPart multiPart) {
        if (!(multiPart.getBodyParts().size() == 2)) {
            return Response.ok("FAILED:  Number of body parts is " + multiPart.getBodyParts().size() + " instead of 2").build();
        }
        BodyPart part0 = multiPart.getBodyParts().get(0);
        if (!(part0.getMediaType().equals(new MediaType("text", "plain")))) {
            return Response.ok("FAILED:  First media type is " + part0.getMediaType()).build();
        }
        BodyPart part1 = multiPart.getBodyParts().get(1);
        if (!(part1.getMediaType().equals(new MediaType("x-application", "x-format")))) {
            return Response.ok("FAILED:  Second media type is " + part1.getMediaType()).build();
        }
        MultiPartBean bean = part1.getEntityAs(MultiPartBean.class);
        if (!bean.getName().equals("myname")) {
            return Response.ok("FAILED:  Second part name = " + bean.getName()).build();
        }
        if (!bean.getValue().equals("myvalue")) {
            return Response.ok("FAILED:  Second part value = " + bean.getValue()).build();
        }
        multiPart.cleanup();
        return Response.ok("SUCCESS:  All tests passed").build();
    }

}