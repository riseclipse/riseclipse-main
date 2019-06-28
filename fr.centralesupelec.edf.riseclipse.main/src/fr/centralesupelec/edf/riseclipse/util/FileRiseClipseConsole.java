/**
 *  Copyright (c) 2019 CentraleSupélec & EDF.
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

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * RiseClipse console using a file
 * 
 * @author Dominique Marcadet
 *
 */
public class FileRiseClipseConsole extends AbstractRiseClipseConsole {
    
    private PrintWriter writer;

    public FileRiseClipseConsole( String name ) {
        super();
        
        try {
            writer = new PrintWriter( name );
        }
        catch( FileNotFoundException e ) {
            throw new RiseClipseFatalException( "Unable to create file " + name, e );
        }
    }

    /**
     * Output message in file
     */
    @Override
    protected void doOutputMessage( String m ) {
        writer.println( m );
        writer.flush();
    }
}
