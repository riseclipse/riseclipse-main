/*
*************************************************************************
**  Copyright (c) 2019 CentraleSupélec & EDF.
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
**      http://wdi.supelec.fr/software/RiseClipse/
*************************************************************************
*/
package fr.centralesupelec.edf.riseclipse.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.IllegalValueException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;



public abstract class AbstractRiseClipseModelLoader {
    
    protected IRiseClipseResourceSet resourceSet;

    protected AbstractRiseClipseModelLoader() {
    }
    
    public void reset( IRiseClipseResourceSet resourceSet ) {
        this.resourceSet = resourceSet;
    }
    
   public IRiseClipseResourceSet getResourceSet() {
        return resourceSet;
    }
    
    public Resource load( String name, IRiseClipseConsole console ) {
        console.verbose( "Loading file " + name + " in RiseClipse" );
        
        int currentSize = resourceSet.getResources().size();
        
        // Construct the URI for the instance file.
        // The argument is treated as a file path only if it denotes an existing file.
        // Otherwise, it's directly treated as a URL.
        File file = new File( name );
        URI uri = file.isFile() ? URI.createFileURI( file.getAbsolutePath() ) : URI.createURI( name );
        
        ArrayList< URI > resourceURIs = new ArrayList< URI >();
        resourceURIs.add( uri );
        try {
            ZipInputStream in = new ZipInputStream( resourceSet.getURIConverter().createInputStream( resourceURIs.get( 0 )));
            ZipEntry entry = in.getNextEntry();
            if( entry != null ) {
                console.verbose( "Found a zip archived file" );
                String zipURI = resourceURIs.get( 0 ).toString();
                resourceURIs.clear();
                while( entry != null ) {
                    // Must use "archive:" and not "zip:" to be recognized by ArchiveURIHandlerImpl
                    resourceURIs.add( URI.createURI( "archive:" + zipURI + "!/" + entry.getName() ));
                    entry = in.getNextEntry();
                }
            }
            in.close();
        }
        catch( IOException e ) {
            // Will be handled later
        }
        
        for( URI resourceURI : resourceURIs ) {
            try {
                // Load the resource through the editing domain.
                //
                @SuppressWarnings("unused")
                Resource resource = resourceSet.getResource( resourceURI, true );
            }
            // This is done by AbstractRiseClipseModelLoader in the command line tool 
            catch( RuntimeException re ) {
                Throwable cause = re.getCause();
                if( cause instanceof IllegalValueException ) {
                    IllegalValueException e = ( IllegalValueException ) cause;
                    console.error( "value " + e.getValue() + " is not legal at line " + e.getLine() + " for feature "
                                   + e.getFeature().getName() + ", it should be " + e.getFeature().getEType().getInstanceTypeName() );
                }
                else if( cause instanceof FileNotFoundException ) {
                    console.error( "Problem loading " + resourceURI + " : file not found" );
                    // Resource has been created !
                    // We remove it to return null
                    if( resourceSet.getResources().size() > currentSize ) {
                        resourceSet.getResources().remove( currentSize );
                    }
                }
                else if( cause instanceof PackageNotFoundException ) {
                    // Unknown namespaces are not errors
                    // This is needed at least for SCL files using specific namespaces in Private elements
                    // TODO: move this to the specific model loader ?
                    PackageNotFoundException e = ( PackageNotFoundException ) cause;
                    console.info( "Elements in the XML namespace " + e.uri() + " are ignored " );
                }
                else if( re instanceof NullPointerException ) {
                	// To get more information and locate the problem
                    console.error( "Problem loading " + resourceURI + " : Null Pointer Exception (see log)");
                    re.printStackTrace();
                }
                else {
                    console.error( "Problem loading " + resourceURI + " : " + cause );
                }
            }
            catch( Exception e ) {
                console.error( "Problem loading " + resourceURI + " : " + e );
            }
            
        }
        
        // resourceSet.getResource() return the resource.
        // But if an exception occurs, we don't get it !
        // So, we expect that the newly created resource is the last one
        // in the resourceSet.
        if( resourceSet.getResources().size() > currentSize ) {
            return resourceSet.getResources().get( currentSize );
        }
        return null;
    }
    
    public void finalizeLoad( IRiseClipseConsole console ) {
        resourceSet.finalizeLoad( console );
    }

}
