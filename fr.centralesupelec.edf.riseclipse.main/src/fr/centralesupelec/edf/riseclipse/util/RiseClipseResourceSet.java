/**
 *  Copyright (c) 2018 CentraleSupélec & EDF.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  This file is part of the RiseClipse tool
 *  
 *  Contributors:
 *      Computer Science Department, CentraleSupélec
 *      EDF R&D
 *  Contacts:
 *      dominique.marcadet@centralesupelec.fr
 *      aurelie.dehouck-neveu@edf.fr
 *  Web site:
 *      http://wdi.supelec.fr/software/RiseClipse/
 */
package fr.centralesupelec.edf.riseclipse.util;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Minimal implementation of ResourceSet for RiseClipse conforming metamodels.
 * It checks that only IRiseClipseResource are used, and give access to
 * contained resources as IRiseClipseResource
 *  
 * @author Dominique Marcadet
 *
 */
public class RiseClipseResourceSet extends ResourceSetImpl implements IRiseClipseResourceSet {

    protected IRiseClipseConsole console;
    // If true, only IRiseClipseResource can be added
    protected boolean strictContent;
    
    // When the resourceSet will load several resources at once, it is
    // useless to call finalizeLoad() after each one. Indeed, such a call
    // can lead to wrong error messages.
    // As the resourceSet do not know when the last resource is loaded,
    // it is the client that must call finalizeLoad() in this case.
    //
    // When a resource is added to an existing resourceSet, the client
    // may not have an easy way to call finalizeLoad() after (the "load
    // resource" command in RiseClipse editor use directly the editing
    // domain). If set, this boolean will then call finalizeLoad() after
    // a getResource().
    private boolean callFinalizeLoadAfterGetResource;

    public RiseClipseResourceSet( boolean strictContent, IRiseClipseConsole console ) {
        this.console = console;
        this.strictContent = strictContent;
        this.callFinalizeLoadAfterGetResource = false;
    }
    
    // TODO: is unset needed ?
    public void setCallFinalizeLoadAfterGetResource() {
        callFinalizeLoadAfterGetResource = true;
    }

    @Override
    public void printStatistics( IRiseClipseConsole console ) {
        for( IRiseClipseResource r : getRiseClipseResources() ) {
            r.printStatistics( console );
        }
    }

    @Override
    public void finalizeLoad( IRiseClipseConsole console ) {
        for( IRiseClipseResource r : getRiseClipseResources() ) {
            r.finalizeLoad( console );
        }
    }

    public EList< IRiseClipseResource > getRiseClipseResources() {
        // The list is copied, but it is expected that the number of resources is low,
        // so this should not be a problem.
        EList< IRiseClipseResource > res = new BasicEList<>();
        for( Resource r : super.getResources() ) {
           if( r instanceof IRiseClipseResource ) {
               res.add(( IRiseClipseResource ) r );
           }
       }
        
        return res;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI, boolean)
     */
    @Override
    public Resource getResource( URI uri, boolean loadOnDemand ) {
        Resource res = super.getResource( uri, loadOnDemand );
        // createResource has been called before, so check is useless
//        if(( res != null ) && strictContent && ( ! ( res instanceof IRiseClipseResource ))) {
//            throw new RiseClipseFatalException( "RiseClipseResourceSet.getResource(): not an IRiseClipseResource", null );
//        }
        
        if( callFinalizeLoadAfterGetResource && ( res instanceof IRiseClipseResource )) {
            finalizeLoad( console );
        }
        return res;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI, java.lang.String)
     */
    @Override
    public Resource createResource( URI uri, String contentType ) {
        Resource res = super.createResource( uri, contentType );
        if(( res != null ) && strictContent && ( ! ( res instanceof IRiseClipseResource ))) {
            throw new RiseClipseFatalException( "RiseClipseResourceSet.getResource(): not an IRiseClipseResource", null );
        }
        return res;
    }

}


