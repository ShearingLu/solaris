package com.smart.pay.common.tools;



import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.web.client.RestOperations;


public abstract class AbstractUriUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractUriUtil.class);


    abstract public ServiceInstanceChooser getLoadBalancer();


    abstract public RestOperations getRestOperations();


    /** TODO: Complement this with a simpler version without fallback-url! */
    public String getServiceUrl(String serviceId, String fallbackUri, String pathUri) {
        return getServiceUrl(serviceId, fallbackUri).toString().concat(pathUri);
    }


    /** TODO: Complement this with a simpler version without fallback-url! */
    public URI getServiceUrl(String serviceId, String fallbackUri) {
        URI uri = null;
        try {
            ServiceInstance instance = getLoadBalancer().choose(serviceId);
            uri = instance.getUri();
            LOG.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);

        } catch (RuntimeException e) {
            // e.printStackTrace();
            uri = URI.create(fallbackUri);
            LOG.warn(String.format("Failed to resolve serviceId '%s'. Fallback to URL '%s'.", serviceId, uri), e);
        }

        return uri;
    }
}
