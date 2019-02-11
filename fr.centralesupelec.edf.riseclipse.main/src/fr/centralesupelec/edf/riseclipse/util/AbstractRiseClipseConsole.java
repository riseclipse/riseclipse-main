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
 * Base class for RiseClipse consoles.
 * It implements the Singleton design pattern, handle the level of messages
 * and prefixes each message with the level name.  
 *  
 * @author Dominique Marcadet
 *
 */
public abstract class AbstractRiseClipseConsole implements IRiseClipseConsole {

	/**
	 * The unique instance of AbstractRiseClipseConsole
	 */
    protected static IRiseClipseConsole console;
    
    /**
     * Give access to the singleton.
     * It creates a {@link TextRiseClipseConsole} if there is none.
     * 
     * @return The unique instance of AbstractRiseClipseConsole
     */
    public static synchronized IRiseClipseConsole getConsole() {
        if( console == null ) {
            console = new TextRiseClipseConsole();
        }
        return console;
    }

    /**
     * Change the current singleton.
     * @param newConsole the new console to use
     */
    public static synchronized void changeConsole( IRiseClipseConsole newConsole ) {
        if( newConsole != null ) {
            console = newConsole;
        }
    }

    /**
     * The current level of displayed messages
     */
    protected int currentLevel;
    
	/**
	 * Constructs a new console, using it as the unique one
	 * (the previous one, if any, is forgotten).
	 * The initial level is set to {@link IRiseClipseConsole#WARNING_LEVEL}
	 */
    protected AbstractRiseClipseConsole() {
        console = this;
        currentLevel = WARNING_LEVEL;
    }
    
    /**
     * Basic implementation of {@link IRiseClipseConsole#setLevel(int)}
     */
	public int setLevel( int level ) {
	    int previousLevel = currentLevel;
		if(( level >= VERBOSE_LEVEL ) && ( level <= FATAL_LEVEL )) {
			currentLevel = level;
		}
		return previousLevel;
	}
    
    /**
     * Prefixes message with VERBOSE and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
     * if current level is {@link #VERBOSE_LEVEL}
     */
    public final void verbose( Object o ) {
        if( currentLevel <= VERBOSE_LEVEL ) {
            doOutputMessage( "VERBOSE: " + o.toString() );
        }
    }
    
	/**
	 * Prefixes message with INFO and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
	 * if current level is {@link #INFO_LEVEL} or below
	 */
    public final void info( Object o ) {
    	if( currentLevel <= INFO_LEVEL ) {
    		doOutputMessage( "INFO: " + o.toString() );
    	}
    }
    
	/**
	 * Prefixes message with WARNING and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
	 * if current level is {@link #WARNING_LEVEL} or below
	 */
	public final void warning( Object o ) {
        if( currentLevel <= WARNING_LEVEL ) {
    		doOutputMessage( "WARNING: " + o.toString() );
        }
    }
    
	/**
	 * Prefixes message with ERROR and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
	 * if current level is {@link #ERROR_LEVEL} or below
	 */
    public final void error( Object o ) {
        if( currentLevel <= ERROR_LEVEL ) {
    		doOutputMessage( "ERROR: " + o.toString() );
        }
    }
    
	/**
	 * Prefixes message with FATAL and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)},
	 * then throws a {@link RiseClipseFatalException}
	 */
    public final void fatal( Object o ) {
		doOutputMessage( "FATAL: " + o.toString() );
		throw new RiseClipseFatalException(  "FATAL: " + o.toString(), null );
    }

    /**
     * Output message s on the current console
     * @param m message to display
     */
    protected abstract void doOutputMessage( String m );
    
}
