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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.annotation.NonNull;

/**
 * Interface which must be implemented by all Resource of RiseClipse conforming metamodels.
 * It specifies minimal needed services.
 * Those are defined as default methods so that implementation is not needed. 
 *  
 * @author Dominique Marcadet
 *
 */
public interface IRiseClipseResource extends Resource {

    /**
     * Display on given console information about content of this resource.
     * Standard information should be displayed at the INFO_LEVEL.
     * More detailed information can be displayed at the VERBOSE_LEVEL
     * 
     * @param console the IRiseClipseConsole to use for display
     */
    default void printStatistics( @NonNull IRiseClipseConsole console ) {
        // Nothing
    }

    /**
     * Execute any needed action after the model is loaded in the resource but before
     * it is displayed or used. 
     * 
     * @param console the IRiseClipseConsole to use for displaying any message
     *                when running this method
     */
    default void finalizeLoad( @NonNull IRiseClipseConsole console ) {
        // Nothing
    }
    
}
