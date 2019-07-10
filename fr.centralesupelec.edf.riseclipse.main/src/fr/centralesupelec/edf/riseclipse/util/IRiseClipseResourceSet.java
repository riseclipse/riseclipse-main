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

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Interface which must be implemented by all ResourceSet of RiseClipse conforming metamodels.
 * It specifies minimal needed services.
 * Those are defined as default methods so that implementation is not needed. 
 *  
 * @author Dominique Marcadet
 *
 */
public interface IRiseClipseResourceSet extends ResourceSet {

    /**
     * Display on given console information about content of this resourceSet.
     * Standard information should be displayed at the INFO_LEVEL.
     * More detailed information can be displayed at the VERBOSE_LEVEL
     * 
     * @param console the IRiseClipseConsole to use for display
     */
    default void printStatistics( IRiseClipseConsole console ) {
        // Nothing
    }

    /**
     * Execute any needed action after the model is loaded in the resourcesnof
     * this resourceSet but before it is displayed or used. 
     * 
     * @param console the IRiseClipseConsole to use for displaying any message
     *                when running this method
     */
    default void finalizeLoad( IRiseClipseConsole console ) {
        // Nothing
    }
    
}
