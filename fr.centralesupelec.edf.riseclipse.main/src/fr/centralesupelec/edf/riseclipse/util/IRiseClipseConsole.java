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
 * Interface for outputting messages to users.
 * It defines four increasing levels of messages and allows
 * for choosing the minimal level of displayed messages.
 *  
 * @author Dominique Marcadet
 *
 */
public interface IRiseClipseConsole {
	
    public final static int VERBOSE_LEVEL = 1;
	public final static int INFO_LEVEL = 2;
	public final static int WARNING_LEVEL = 3;
	public final static int ERROR_LEVEL = 4;
	public final static int FATAL_LEVEL = 5;
	
	/**
	 * Set the current level of displayed messages.
	 * 
	 * @param level minimal level of messages to be displayed,
	 *              ignored if outside [VERBOSE_LEVEL..FATAL_LEVEL]
	 * @return the previous level
	 */
	public int setLevel( int level );
    
    /**
     * Output message o.toString() if current level is VERBOSE_LEVEL
     * 
     * @param o message to be displayed
     */
    public void verbose( Object... o );

	/**
	 * Output message o.toString() if current level is INFO_LEVEL or below
	 * 
	 * @param o message to be displayed
	 */
    public void info( Object... o );

	/**
	 * Output message o.toString() if current level is WARNING_LEVEL or below
	 * 
	 * @param o message to be displayed
	 */
    public void warning( Object... o );

	/**
	 * Output message o.toString() if current level is ERROR_LEVEL or below
	 * 
	 * @param o message to be displayed
	 */
    public void error( Object... o );

	/**
	 * Output message o.toString() if current level is FATAL_LEVEL or below
	 * 
	 * @param o message to be displayed
	 */
    public void fatal( Object... o );

    /**
     * All messages will be displayed, even if an identical one has already been displayed
     */
    public void displayIdenticalMessages();

    /**
     * Messages will be displayed only once. An identical one will be ignored
     */
    public void doNotDisplayIdenticalMessages();

}

