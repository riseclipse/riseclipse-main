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
import java.io.PrintWriter;

import org.eclipse.jdt.annotation.NonNull;

/**
 * RiseClipse console using a file
 * 
 * @author Dominique Marcadet
 *
 */
public class FileRiseClipseConsole extends AbstractRiseClipseConsole {
    
    private PrintWriter writer;

    public FileRiseClipseConsole( @NonNull String name ) {
        super();
        
        try {
            writer = new PrintWriter( name );
        }
        catch( IOException e ) {
            throw new RiseClipseFatalException( "Unable to create file " + name, e );
        }
    }

    /**
     * Output message in file
     */
    @Override
    protected void doOutputMessage( @NonNull String m ) {
        writer.println( m );
        writer.flush();
    }
}
