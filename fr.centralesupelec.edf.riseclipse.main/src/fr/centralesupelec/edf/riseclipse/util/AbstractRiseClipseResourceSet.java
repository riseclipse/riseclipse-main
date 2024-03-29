/*
*************************************************************************
**  Copyright (c) 2016-2022 CentraleSupélec & EDF.
**  All rights reserved. This program and the accompanying materials
**  are made available under the terms of the Eclipse Public License v2.0
**  which accompanies this distribution, and is available at
**  https://www.eclipse.org/legal/epl-v20.html
** 
**  This file is part of the RiseClipse tool
**  
**  Contributors:
**      Computer Science Department, CentraleSupélec
**      EDF R&D
**  Contacts:
**      dominique.marcadet@centralesupelec.fr
**      aurelie.dehouck-neveu@edf.fr
**  Web site:
**      https://riseclipse.github.io
*************************************************************************
*/
package fr.centralesupelec.edf.riseclipse.util;

import java.util.Optional;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.annotation.NonNull;

/**
 * Minimal implementation of ResourceSet for RiseClipse conforming metamodels.
 * It checks that only IRiseClipseResource are used, and give access to
 * contained resources as IRiseClipseResource
 *  
 * @author Dominique Marcadet
 *
 */
public abstract class AbstractRiseClipseResourceSet extends ResourceSetImpl implements IRiseClipseResourceSet {

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

    protected AbstractRiseClipseResourceSet( boolean strictContent ) {
        this.strictContent = strictContent;
        this.callFinalizeLoadAfterGetResource = false;
    }
    
    public void setCallFinalizeLoadAfterGetResource() {
        callFinalizeLoadAfterGetResource = true;
    }

    @Override
    public void printStatistics( @NonNull IRiseClipseConsole console ) {
        getRiseClipseResources().stream().forEach( r -> r.printStatistics( console ) );
    }

    @Override
    public void finalizeLoad( @NonNull IRiseClipseConsole console ) {
        getRiseClipseResources().stream().forEach( r -> r.finalizeLoad( console ) );
    }

    public @NonNull EList< IRiseClipseResource > getRiseClipseResources() {
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
    public @NonNull Resource getResource( @NonNull URI uri, boolean loadOnDemand ) {
        Resource res = super.getResource( uri, loadOnDemand );
        
        if( callFinalizeLoadAfterGetResource && ( res instanceof IRiseClipseResource )) {
            finalizeLoad( AbstractRiseClipseConsole.getConsole() );
        }
        return res;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI, java.lang.String)
     */
    @Override
    public Resource createResource( @NonNull URI uri, @NonNull String contentType ) {
        Resource res = null;
        Optional< String > metamodelName = RiseClipseMetamodel.findMetamodelFor( uri );
        if( metamodelName.isPresent() ) {
            res = createRiseClipseResource( uri, contentType );
        }
        if(( res == null ) && ! strictContent ) {
            res = super.createResource( uri, contentType );
        }
        if( res != null ) {
            getResources().add( res );
        }
        return res;
    }

    protected abstract @NonNull IRiseClipseResource createRiseClipseResource( @NonNull URI uri, @NonNull String contentType );

}


