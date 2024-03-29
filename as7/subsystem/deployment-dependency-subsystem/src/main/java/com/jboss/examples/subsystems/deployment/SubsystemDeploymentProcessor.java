package com.jboss.examples.subsystems.deployment;

import java.io.IOException;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.vfs.VirtualFile;

/**
 * An example deployment unit processor that does nothing. To add more deployment
 * processors copy this class, and add to the {@link AbstractDeploymentChainStep}
 * {@link SubsystemAdd#performBoottime(org.jboss.as.controller.OperationContext, org.jboss.dmr.ModelNode, org.jboss.dmr.ModelNode, org.jboss.as.controller.ServiceVerificationHandler, java.util.List)}
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemDeploymentProcessor implements DeploymentUnitProcessor {

    Logger log = Logger.getLogger(SubsystemDeploymentProcessor.class);

    /**
     * See {@link Phase} for a description of the different phases
     */
    public static final Phase PHASE = Phase.DEPENDENCIES;

    /**
     * The relative order of this processor within the {@link #PHASE}.
     * The current number is large enough for it to happen after all
     * the standard deployment unit processors that come with JBoss AS.
     */
    public static final int PRIORITY = 0x4000;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        log.info("Deploy");
        String name = phaseContext.getDeploymentUnit().getName();
       
        // Look for MANIFEST.MF
        ResourceRoot root = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT);
        VirtualFile manifestFile = root.getRoot().getChild("META-INF/MANIFEST.MF2");
        
        if(manifestFile.exists())
        {
	        try
	        {
	        	Manifest manifest = new Manifest(manifestFile.openStream());
	        	Attributes attrs = manifest.getMainAttributes();
	        	for(Object o : attrs.keySet())
	        	{
	        		log.info("Manifest.MainAttributes: " + o);
	        	}
	        	
	        	Map<String, Attributes> entries = manifest.getEntries();
	        	for(String key : entries.keySet())
	        	{
	        		log.info("Manifest.Entry: " + key);
	        		attrs = entries.get(key);
	        		for(Object o : attrs.keySet())
	            	{
	            		log.info("Manifest.Entry.Atrributes: " + o);
	            	}	
	        	}
	        	
	        	attrs = manifest.getAttributes("Deployment-Dependencies");
	        	for(Object o : attrs.keySet())
	        	{
	        		log.info("Manifest.Deployment-Dependencies: " + o);
	        	}
	        }
	        catch(IOException ioe)
	        {
	        	ioe.printStackTrace();
	        }	       
        }
        
        // this is from the acme-subsystem.zip from https://docs.jboss.org/author/display/AS71/Example+subsystem
        // So this is asks the MSC for the TracerService which then adds the deployment to the TrackerService
//        TrackerService service = getTrackerService(phaseContext.getServiceRegistry(), name);
        
//        if (service != null) {
//            ResourceRoot root = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT);
//            VirtualFile cool = root.getRoot().getChild("META-INF/cool.txt");
//            service.addDeployment(name);
//            if (cool.exists()) {
//                service.addCoolDeployment(name);
//            }
//        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    	log.info("Undeploy");
//    	String name = context.getName();
//        TrackerService service = getTrackerService(context.getServiceRegistry(), name);
//        if (service != null) {
//            service.removeDeployment(name);
//        }
    }
    
    // this is from the acme-subsystem.zip from https://docs.jboss.org/author/display/AS71/Example+subsystem
    // So this is calling Service controller to get the service
//    private TrackerService getTrackerService(ServiceRegistry registry, String name) {
//        int last = name.lastIndexOf(".");
//        String suffix = name.substring(last + 1);
//        ServiceController<?> container = registry.getService(TrackerService.createServiceName(suffix));
//        if (container != null) {
//            TrackerService service = (TrackerService)container.getValue();
//            return service;
//        }
//        return null;
//    }

}
