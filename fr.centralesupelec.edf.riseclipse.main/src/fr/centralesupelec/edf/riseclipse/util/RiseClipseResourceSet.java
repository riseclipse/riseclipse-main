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
           if( ! ( r instanceof IRiseClipseResource )) {
               throw new RiseClipseFatalException( "RiseClipseResourceSet.getRiseClipseResources(): not an IRiseClipseResource", null );
           }
           res.add(( IRiseClipseResource ) r );
       }
        
        return res;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI, boolean)
     */
    @Override
    public IRiseClipseResource getResource( URI uri, boolean loadOnDemand ) {
        Resource res = super.getResource( uri, loadOnDemand );
        if(( res != null ) && ( ! ( res instanceof IRiseClipseResource ))) {
            throw new RiseClipseFatalException( "RiseClipseResourceSet.getResource(): not an IRiseClipseResource", null );
        }
        return ( IRiseClipseResource ) res;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI)
     */
    @Override
    public IRiseClipseResource createResource( URI uri ) {
        Resource res = super.createResource( uri );
        if(( res != null ) && ( ! ( res instanceof IRiseClipseResource ))) {
            throw new RiseClipseFatalException( "RiseClipseResourceSet.createResource(): not an IRiseClipseResource", null );
        }
        return ( IRiseClipseResource ) res;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI, java.lang.String)
     */
    @Override
    public IRiseClipseResource createResource( URI uri, String contentType ) {
        Resource res = super.createResource( uri, contentType );
        if(( res != null ) && ( ! ( res instanceof IRiseClipseResource ))) {
            throw new RiseClipseFatalException( "RiseClipseResourceSet.getResource(): not an IRiseClipseResource", null );
        }
        return ( IRiseClipseResource ) res;
    }

}


