/*
*************************************************************************
**  Copyright (c) 2022 CentraleSupélec & EDF.
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl;
import org.xml.sax.SAXException;

public abstract class AbstractRiseClipseXMLLoadImpl extends XMLLoadImpl {

    protected AbstractRiseClipseXMLLoadImpl( XMLHelper helper ) {
        super( helper );
    }

    @Override
    protected SAXParser makeParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory f = SAXParserFactory.newInstance();
        // Sonar: XML parsers should not be vulnerable to XXE attacks (java:S2755)
        f.setFeature( "http://apache.org/xml/features/disallow-doctype-decl", true );
        f.setNamespaceAware( true );
        return f.newSAXParser();
    }

    /*
     * XMLLoadImpl.handleErrors() will throw either Resource.IOWrappedException or IOException
     * for errors found while parsing document.
     * Errors are not always fatal (like an InvalidValueException when an Integer
     * is expected and something else is found).
     * We will handle such errors later
     */
    @Override
    protected void handleErrors() throws IOException {
        // Nothing
    }
}
