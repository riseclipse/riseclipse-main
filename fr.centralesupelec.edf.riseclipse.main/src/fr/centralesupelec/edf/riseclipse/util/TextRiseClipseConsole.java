/*
*************************************************************************
**  Copyright (c) 2016-2021 CentraleSupélec & EDF.
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

/**
 * Basic RiseClipse console on System.out
 * 
 * @author Dominique Marcadet
 *
 */
public class TextRiseClipseConsole extends AbstractRiseClipseConsole {
    
    public TextRiseClipseConsole() {
        super();
    }

    public TextRiseClipseConsole( boolean useColor ) {
        super( useColor );
    }

	/**
	 * Output message on System.out
	 */
    @Override
    protected void doOutputMessage( String m ) {
        System.out.println( m );
        System.out.flush();
    }
}
