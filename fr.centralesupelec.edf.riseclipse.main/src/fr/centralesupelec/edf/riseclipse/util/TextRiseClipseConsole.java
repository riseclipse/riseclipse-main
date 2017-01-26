/**
 *  Copyright (c) 2017 CentraleSupélec & EDF.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  This file is part of the RiseClipse tool
 *  
 *  Contributors:
 *      Computer Science Department, CentraleSupélec : initial implementation
 *  Contacts:
 *      Dominique.Marcadet@centralesupelec.fr
 */
package fr.centralesupelec.edf.riseclipse.util;

/**
 * Basic RiseClipse console on System.out
 * 
 * @author Dominique Marcadet
 *
 */
public class TextRiseClipseConsole extends AbstractRiseClipseConsole {

	/**
	 * Output message on System.out
	 */
    @Override
    protected void doOutputMessage( String m ) {
        System.out.println( m );
        System.out.flush();
    }
}
