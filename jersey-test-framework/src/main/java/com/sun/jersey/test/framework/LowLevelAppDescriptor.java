package com.sun.jersey.test.framework;

import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.test.framework.spi.container.grizzly.GrizzlyTestContainerFactory;
import com.sun.jersey.test.framework.spi.container.http.HTTPContainerFactory;
import com.sun.jersey.test.framework.spi.container.inmemory.InMemoryTestContainerFactory;

/**
 * A low-level application descriptor.
 * <p>
 * An instance of this class is created by creating an instance of
 * {@link Builder}, invoking methods to add/modify state, and finally invoking
 * the {@link Builder#build() } method.
 * <p>
 * This application descriptor is compatible with low-level test containers
 * that do not support Servlet. The following low-level test container
 * factories are provided:
 * <ul>
 *  <li>{@link GrizzlyTestContainerFactory} for testing with the low-level
 *      Grizzly HTTP container.</li>
 *  <li>{@link HTTPContainerFactory} for testing with the Light Weight HTTP
 *      server distributed with Java SE 6.</li>
 *  <li>{@link InMemoryTestContainerFactory} for testing in memory without
 *      using underlying HTTP client and server side functionality
 *      to send requests and receive responses.</li>
 * </ul>
 *
 * @author Paul.Sandoz@Sun.COM
 */
public class LowLevelAppDescriptor extends AppDescriptor {

    /**
     * The builder for building a low-level application descriptor.
     * <p>
     * If properties of the builder are not modified default values be utilized.
     * The default value for the context path is an empty string.
     * <p>
     * After the {@link #build() } has been invoked the state of the builder
     * will be reset to the default values.
     */
    public static class Builder
            extends AppDescriptorBuilder<Builder, LowLevelAppDescriptor> {

        protected final ResourceConfig rc;

        protected String contextPath = "";

        /**
         * Create a builder with one or more package names where
         * root resource and provider classes reside.
         * <p>
         * An instance of {@link PackagesResourceConfig} will be created and
         * set as the resource configuration.
         *
         * @param packages one or more package names where
         *        root resource and provider classes reside.
         * @throws IllegalArgumentException if <code>packages</code> is null.
         */
        public Builder(String... packages) throws IllegalArgumentException {
            if (packages == null)
                throw new IllegalArgumentException("The packages must not be null");
            
            this.rc = new PackagesResourceConfig(packages);
        }

        /**
         * Create a builder with one or more root resource and provider classes.
         * <p>
         * An instance of {@link ClassNamesResourceConfig} will be created and
         * set as the resource configuration.
         *
         * @param classes one or more root resource and provider classes.
         * @throws IllegalArgumentException if <code>classes</code> is null.
         */
        public Builder(Class... classes) throws IllegalArgumentException {
            if (classes == null)
                throw new IllegalArgumentException("The classes must not be null");

            this.rc = new ClassNamesResourceConfig(classes);
        }

        /**
         * Create a builder with a resource configuration.
         *
         * @param rc the resource configuration.
         * @throws IllegalArgumentException if <code>rc</code> is null.
         */
        public Builder(ResourceConfig rc) {
            if (rc == null)
                throw new IllegalArgumentException("The resource configuration must not be null");
            
            this.rc = rc;
        }

        /**
         * Set the context path.
         *
         * @param contextPath the context path to the application.
         * @return this builder.
         * @throws IllegalArgumentException if <code>contextPath</code> is null.
         */
        public Builder contextPath(String contextPath) {
            if (contextPath == null)
                throw new IllegalArgumentException("The context path must not be null");
            
            this.contextPath = contextPath;
            return this;
        }

        /**
         * Build the low-level application descriptor.
         * .
         * @return the low-level application descriptor.
         */
        public LowLevelAppDescriptor build() {
            LowLevelAppDescriptor lld = new LowLevelAppDescriptor(this);

            reset();
            
            return lld;
        }

        @Override
        protected void reset() {
            super.reset();

            this.contextPath = "";
        }
    }

    private final ResourceConfig rc;

    private final String contextPath;

    /**
     * The private constructor.
     * @param A {@link Builder} instance.
     */
    private LowLevelAppDescriptor(Builder b) {
        super(b);

        this.contextPath = b.contextPath;
        this.rc = b.rc;
    }

    /**
     * Get the resource configuration.
     *
     * @return the resource configuration.
     */
    public ResourceConfig getResourceConfig() {
        return rc;
    }

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Transform a Web-based application descriptor into a low-level
     * application descriptor.
     * 
     * @param wad the Web-based application descriptor.
     * @return the low-level application descriptor.
     */
    public static LowLevelAppDescriptor transform(WebAppDescriptor wad) {
        // TODO need to check contraints on wad
        String packages = wad.getInitParams().get(PackagesResourceConfig.PROPERTY_PACKAGES);
        return new LowLevelAppDescriptor.Builder(packages).build();
    }
}
