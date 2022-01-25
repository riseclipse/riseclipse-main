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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ViewerFilter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class RiseClipseMetamodel {
    
    private static final HashMap< String, RiseClipseMetamodel > knownMetamodels = new HashMap<>();
    private static final String XMLNS_ATTRIBUTE_NAME = "xmlns";
    private static       SAXParser saxParser;
    private static final URIConverter uriConverter = new ExtensibleURIConverterImpl();
    
    private static final String Category = "RiseClipse/Metamodel";
    
    static {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        }
        catch( ParserConfigurationException e ) {
            throw new RiseClipseFatalException( "RiseClipseMetamodel.static", e );
        }
        catch( SAXException e ) {
            throw new RiseClipseFatalException( "RiseClipseMetamodel.static", e );
        }
    }

    public static void loadKnownMetamodels( @NonNull IRiseClipseConsole console ) {
        IConfigurationElement[] contributions = Platform.getExtensionRegistry().getConfigurationElementsFor(
                "fr.centralesupelec.edf.riseclipse.main.meta_models" );
        for( int i = 0; i < contributions.length; i++ ) {
            String uri = contributions[i].getAttribute( "uri" );
            if(( uri == null ) || ( uri.isEmpty() )) {
                throw new RiseClipseFatalException( "Invalid metamodel URI for RiseClispse", null );
            }
            String name = contributions[i].getAttribute( "name" );
            if(( name == null ) || ( name.isEmpty() )) {
                throw new RiseClipseFatalException( "Invalid metamodel name for RiseClispse", null );
            }

            String adapterFactoryName = contributions[i].getAttribute( "adapterFactory" );
            String resourceFactoryName = contributions[i].getAttribute( "resourceFactory" );
            String resourceSetFactoryName = contributions[i].getAttribute( "resourceSetFactory" );
            String viewerFilterName = contributions[i].getAttribute( "viewerFilter" );
            
            AdapterFactory newAdapterFactory = null;
            IRiseClipseResourceFactory newResourceFactory = null;
            IRiseClipseResourceSetFactory newResourceSetFactory = null;
            ViewerFilter newViewerFilter = null;
            if( knownMetamodels.get( uri ) != null ) {
                // We allow for extension point in several plugins (one in the main, another in the edit)
                newAdapterFactory = knownMetamodels.get( uri ). getAdapterFactory().orElse( null );
                newResourceFactory = knownMetamodels.get( uri ).getResourceFactory().orElse( null );
                newResourceSetFactory = knownMetamodels.get( uri ).getResourceSetFactory().orElse( null );
                newViewerFilter = knownMetamodels.get( uri ).getViewerFilter().orElse( null );
            }
            try {
                if(( adapterFactoryName != null ) && ! adapterFactoryName.isEmpty() ) {
                    newAdapterFactory = ( AdapterFactory ) contributions[i].createExecutableExtension( "adapterFactory" );
                }
                if(( resourceFactoryName != null ) && ! resourceFactoryName.isEmpty() ) {
                    newResourceFactory = ( IRiseClipseResourceFactory ) contributions[i].createExecutableExtension( "resourceFactory" );
                }
                if(( resourceSetFactoryName != null ) && ! resourceSetFactoryName.isEmpty() ) {
                    newResourceSetFactory = ( IRiseClipseResourceSetFactory ) contributions[i].createExecutableExtension( "resourceSetFactory" );
                }
                if(( viewerFilterName != null ) && ! viewerFilterName.isEmpty() ) {
                    newViewerFilter = ( ViewerFilter ) contributions[i].createExecutableExtension( "viewerFilter" );
                }
            }
            catch( CoreException e ) {
                console.error( Category, 0, "Metamodel with uri " + uri + " has invalid factories." );
                continue;
            }
            if( knownMetamodels.get( uri ) == null ) {
                console.info( Category, 0, "Added metamodel " + name + " for URI " + uri );
            }
            knownMetamodels.put( uri, new RiseClipseMetamodel( name, newAdapterFactory,
                    newResourceFactory, newResourceSetFactory, newViewerFilter ));
        }
    }
    
    @SuppressWarnings( "serial" )
    private static class MetamodelFoundException extends SAXException {

        private @NonNull String ns;

        public MetamodelFoundException( @NonNull String ns ) {
            this.ns = ns;
        }

        public @NonNull String getMetamodel() {
            return ns;
        }
    }

    public static Optional< String > findMetamodelFor( @NonNull URI resourceURI ) {


       DefaultHandler defaultHandler = new DefaultHandler() {
           
            public void startElement( String uri, String localName, String qName, Attributes attributes )
                    throws SAXException {
                for( int i = 0; i < attributes.getLength(); ++i ) {
                    String furi = attributes.getURI( i );
                    if( furi.length() == 0 ) {
                        furi = attributes.getQName( i );
                        int dc = furi.indexOf( ':' );
                        if( dc != -1 ) {
                            furi = furi.substring( 0, dc );
                        }
                    }
                    if( XMLNS_ATTRIBUTE_NAME.equals( furi ) ) {
                        String ns = attributes.getValue( i );
                        if( ns.endsWith( "#" )) ns = ns.substring( 0, ns.length() - 1 );
                        if( knownMetamodels.containsKey( ns )) {
                            if( knownMetamodels.get( ns ).getResourceSetFactory().isPresent() ) {
                                // Stop parsing and give back result
                                // TODO: can we stop parsing without using an exception ?
                                throw new MetamodelFoundException( ns );
                            }
                        }
                    }
                }
                // no need to look after the root element
                throw new MetamodelFoundException( null );
            }
        };

        String res = null;
        try {
            InputStream inputStream = uriConverter.createInputStream( resourceURI );
            saxParser.parse( inputStream, defaultHandler );
        }
        catch( MetamodelFoundException e ) {
            res = e.getMetamodel();
        }
        catch( SAXException e ) {
            // Not an xml file or any other error : we will use the standard mechanism
        }
        catch( IOException e ) {
            // Not an xml file or any other error : we will use the standard mechanism
        }
        finally {
            saxParser.reset();
        }

        return Optional.ofNullable( res );
    }
    
    public static boolean isKnown( @NonNull String metamodelURI ) {
        // The resourceSetFactory is the criteria for a known metamodel
        return knownMetamodels.containsKey( metamodelURI ) && knownMetamodels.get( metamodelURI ).getResourceSetFactory().isPresent();
    }
    
    public static Optional< RiseClipseMetamodel > getMetamodel( @NonNull String metamodelURI ) {
        if( isKnown( metamodelURI )) {
            return Optional.ofNullable( knownMetamodels.get( metamodelURI ));
        }
        return Optional.empty();
    }


    private @NonNull String name;
    private AdapterFactory adapterFactory;
    private IRiseClipseResourceFactory resourceFactory;
    private IRiseClipseResourceSetFactory resourceSetFactory;
    private ViewerFilter viewerFilter;
    
    public RiseClipseMetamodel( @NonNull String name, AdapterFactory adapterFactory,
            IRiseClipseResourceFactory resourceFactory, IRiseClipseResourceSetFactory resourceSetFactory, ViewerFilter filter ) {
        super();
        this.name = name;
        this.adapterFactory = adapterFactory;
        this.resourceFactory = resourceFactory;
        this.resourceSetFactory = resourceSetFactory;
        this.viewerFilter = filter;
    }

    public @NonNull String getName() {
        return name;
    }

    public Optional< AdapterFactory > getAdapterFactory() {
        return Optional.ofNullable( adapterFactory );
    }

    public Optional< IRiseClipseResourceFactory > getResourceFactory() {
        return Optional.ofNullable( resourceFactory );
    }

    public Optional< IRiseClipseResourceSetFactory > getResourceSetFactory() {
        return Optional.ofNullable( resourceSetFactory );
    }

    public Optional< ViewerFilter > getViewerFilter() {
        return Optional.ofNullable( viewerFilter );
    }
    
}
