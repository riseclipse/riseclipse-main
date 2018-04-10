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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RiseClipseResourceSet extends ResourceSetImpl {
    
    private HashMap< String, Factory > resourceFactories;
    
    private ResourceFactoryFinder factoryFinder;

    public RiseClipseResourceSet( HashMap< String, Resource.Factory > resourceFactories ) {
        this.resourceFactories = resourceFactories;
        this.factoryFinder = new ResourceFactoryFinder();
    }

    @Override
    public Resource createResource( URI uri, String contentType ) {
        Resource.Factory f = this.factoryFinder.findFactoryFor( uri );
        // If we don't find any factory registered as RiseClipse metamodel, use the
        // standard mechanism. This is at least needed for OCL documents
        if( f != null ) {
            Resource result = f.createResource( uri );
            getResources().add( result );
            return result;
        }
        return super.createResource( uri, contentType );
    }
    
    @SuppressWarnings( "serial" )
    private static class FactoryFoundException extends SAXException {

        private Resource.Factory factory;

        public FactoryFoundException( Resource.Factory factory ) {
            this.factory = factory;
        }

        public Resource.Factory getFactory() {
            return factory;
        }
    }

    protected class ResourceFactoryFinder {
        private SAXParser saxParser;

        public ResourceFactoryFinder() {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try {
                saxParser = saxParserFactory.newSAXParser();
            }
            catch( ParserConfigurationException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch( SAXException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public Resource.Factory findFactoryFor( URI uri ) {

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
                        // TODO: define named constant
                        if( "xmlns".equals( furi ) ) {
                            String ns = attributes.getValue( i );
                            if( RiseClipseResourceSet.this.resourceFactories.containsKey( ns ) ) {
                                Resource.Factory factory = RiseClipseResourceSet.this.resourceFactories
                                        .get( ns );
                                // Stop parsing and give back result
                                throw new FactoryFoundException( factory );
                            }
                        }
                    }
                }
            };

            try {
                URIConverter uriConverter = getURIConverter();
                InputStream inputStream = uriConverter.createInputStream( uri );
                saxParser.parse( inputStream, defaultHandler );
            }
            catch( FactoryFoundException e ) {
                return e.getFactory();
            }
            catch( SAXException e ) {
                // Not an xml file or any other error : we will use the standard mechanism
                return null;
            }
            catch( IOException e ) {
                // Not an xml file or any other error : we will use the standard mechanism
                return null;
            }

            return null;
        }
    }
}
