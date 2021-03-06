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

/**
 * This class is a marker for RuntimeException in RiseClipse
 * 
 * @author Dominique Marcadet
 *
 */
public class RiseClipseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RiseClipseRuntimeException( String error, Exception e ) {
        super( error, e );
    }

}
