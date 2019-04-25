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
