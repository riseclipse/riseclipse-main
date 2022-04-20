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
import org.eclipse.jdt.annotation.NonNull;



public abstract class AbstractRiseClipseModelLoader {
    
    private static final String PROBLEM_LOADING_ERROR = "Problem loading ";

    private static final String MODEL_LOADER_CATEGORY = "RiseClipse/ModelLoader";

    protected @NonNull IRiseClipseResourceSet resourceSet;

    // used by private method
    private IRiseClipseConsole console;
    private int currentResourceSetSize;

    protected AbstractRiseClipseModelLoader( @NonNull IRiseClipseResourceSet resourceSet ) {
        this.resourceSet = resourceSet;
    }
    
    public void reset( @NonNull IRiseClipseResourceSet resourceSet ) {
        this.resourceSet = resourceSet;
    }
    
   public @NonNull IRiseClipseResourceSet getResourceSet() {
        return resourceSet;
    }
    
    public Resource load( @NonNull String name, @NonNull IRiseClipseConsole console ) {
        this.console = console;
        console.debug( MODEL_LOADER_CATEGORY, 0, "Loading file " + name + " in RiseClipse" );
        
        currentResourceSetSize = resourceSet.getResources().size();
        
        // Construct the URI for the instance file.
        // The argument is treated as a file path only if it denotes an existing file.
        // Otherwise, it's directly treated as a URL.
        File file = new File( name );
        URI uri = file.isFile() ? URI.createFileURI( file.getAbsolutePath() ) : URI.createURI( name );
        
        ArrayList< URI > resourceURIs = new ArrayList<>();
        resourceURIs.add( uri );
        try(  ZipInputStream in = new ZipInputStream( resourceSet.getURIConverter().createInputStream( resourceURIs.get( 0 )))) {
            ZipEntry entry = in.getNextEntry();
            if( entry != null ) {
                console.info( MODEL_LOADER_CATEGORY, 0, "Found a zip archived file" );
                String zipURI = resourceURIs.get( 0 ).toString();
                resourceURIs.clear();
                while( entry != null ) {
                    // Must use "archive:" and not "zip:" to be recognized by ArchiveURIHandlerImpl
                    resourceURIs.add( URI.createURI( "archive:" + zipURI + "!/" + entry.getName() ));
                    entry = in.getNextEntry();
                }
            }
        }
        catch( IOException e ) {
            // Will be handled later
        }
        
        for( URI resourceURI : resourceURIs ) {
            String resourceName = resourceURI.lastSegment();
            try {
                // Load the resource through the editing domain.
                //
                @SuppressWarnings( "unused" )
                Resource resource = resourceSet.getResource( resourceURI, true );
            }
            catch( RuntimeException re ) {
                // auxiliary method to lower the cognitive complexity (Sonar)
                handleRuntimeException( resourceName, re );
            }
            catch( Exception e ) {
                console.error( MODEL_LOADER_CATEGORY, 0, PROBLEM_LOADING_ERROR, resourceName, ": got exception ", e );
            }
            
        }
        
        // resourceSet.getResource() return the resource.
        // But if an exception occurs, we don't get it !
        // So, we expect that the newly created resource is the last one
        // in the resourceSet.
        if( resourceSet.getResources().size() > currentResourceSetSize ) {
            return resourceSet.getResources().get( currentResourceSetSize );
        }
        return null;
    }

    private void handleRuntimeException( String resourceName, RuntimeException re ) {
        Throwable cause = re.getCause();
        if( cause instanceof IllegalValueException ) {
            IllegalValueException e = ( IllegalValueException ) cause;
            console.error( MODEL_LOADER_CATEGORY, resourceName, e.getLine(),
                    "value ", e.getValue(), " is not legal for feature ",
                    e.getFeature().getName(), ", it should be a value of ", e.getFeature().getEType().getName() );
        }
        else if( cause instanceof FileNotFoundException ) {
            console.error( MODEL_LOADER_CATEGORY, 0, PROBLEM_LOADING_ERROR, resourceName, ": file not found" );
            // Resource has been created !
            // We remove it to return null
            if( resourceSet.getResources().size() > currentResourceSetSize ) {
                resourceSet.getResources().remove( currentResourceSetSize );
            }
        }
        else if( cause instanceof PackageNotFoundException ) {
            // Unknown namespaces are not errors
            // This is needed at least for SCL files using specific namespaces in Private elements
            // TODO: move this to the specific model loader ?
            PackageNotFoundException e = ( PackageNotFoundException ) cause;
            console.notice( MODEL_LOADER_CATEGORY, 0, "Elements in the XML namespace ", e.uri(), " are ignored " );
        }
        else if( re instanceof NullPointerException ) {
        	// To get more information and locate the problem
            console.error( MODEL_LOADER_CATEGORY, 0, PROBLEM_LOADING_ERROR, resourceName, " : Null Pointer Exception (see log)" );
            re.printStackTrace();
        }
        else {
            console.error( MODEL_LOADER_CATEGORY, 0, PROBLEM_LOADING_ERROR, resourceName, ": got exception ", cause );
        }
    }
    
    public void finalizeLoad( @NonNull IRiseClipseConsole console ) {
        resourceSet.finalizeLoad( console );
    }

}
