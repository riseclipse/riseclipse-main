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

/*
 * org.apache.commons.lang3 is no more available in Orbit for Eclipse 2022-06
 */

public class Pair< L, R > {
    
    private final L left;
    private final R right;
    
    public Pair( L left, R right ) {
        this.left = left;
        this.right = right;
    }
    
    static public < L, R >Pair< L, R > of( L left, R right ) {
        return new Pair< L, R >( left, right );
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

}
