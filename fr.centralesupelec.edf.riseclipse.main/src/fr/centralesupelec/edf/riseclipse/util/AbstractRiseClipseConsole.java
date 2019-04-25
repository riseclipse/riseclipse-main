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

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for RiseClipse consoles.
 * It implements the Singleton design pattern, handle the level of messages
 * and prefixes each message with the level name.  
 *  
 * @author Dominique Marcadet
 *
 */
public abstract class AbstractRiseClipseConsole implements IRiseClipseConsole {

    // ANSI escape codes for colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    //private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    //private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    //private static final String ANSI_WHITE = "\u001B[37m";
    
    // Message prefixes
    private static final String VERBOSE_PREFIX = "VERBOSE: ";
    private static final String    INFO_PREFIX = "INFO:    ";
    private static final String WARNING_PREFIX = "WARNING: ";
    private static final String   ERROR_PREFIX = "ERROR:   ";
    private static final String   FATAL_PREFIX = "FATAL:   ";
    
    // Colored message prefixes
    private static final String COLORED_VERBOSE_PREFIX = ANSI_CYAN   + VERBOSE_PREFIX + ANSI_RESET;
    private static final String    COLORED_INFO_PREFIX = ANSI_BLUE   + INFO_PREFIX    + ANSI_RESET;
    private static final String COLORED_WARNING_PREFIX = ANSI_YELLOW + WARNING_PREFIX + ANSI_RESET;
    private static final String   COLORED_ERROR_PREFIX = ANSI_RED    + ERROR_PREFIX   + ANSI_RESET;
    private static final String   COLORED_FATAL_PREFIX = ANSI_BLACK  + FATAL_PREFIX   + ANSI_RESET;
    
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
     * Messages which have been displayed once
     */
    private Set< String > displayedMessages;
    
    /**
     * Whether to use color for message prefixes
     */
    private boolean useColor;
    
    /**
     * Constructs a new console, using it as the unique one
     * (the previous one, if any, is forgotten).
     * The initial level is set to {@link IRiseClipseConsole#WARNING_LEVEL}
     * Color is used according if argument is true
     */
    protected AbstractRiseClipseConsole( boolean useColor ) {
        console = this;
        currentLevel = WARNING_LEVEL;
        this.useColor = useColor;
    }
    
    /**
     * Constructs a new console, using it as the unique one
     * (the previous one, if any, is forgotten).
     * The initial level is set to {@link IRiseClipseConsole#WARNING_LEVEL}
     * Color is not used.
     */
    protected AbstractRiseClipseConsole() {
        this( false );
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
            if( useColor ) {
                outputMessage( COLORED_VERBOSE_PREFIX + o.toString() );
            }
            else {
                outputMessage( VERBOSE_PREFIX + o.toString() );
            }
        }
    }
    
	/**
	 * Prefixes message with INFO and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
	 * if current level is {@link #INFO_LEVEL} or below
	 */
    public final void info( Object o ) {
    	if( currentLevel <= INFO_LEVEL ) {
            if( useColor ) {
                outputMessage( COLORED_INFO_PREFIX + o.toString() );
            }
            else {
                outputMessage( INFO_PREFIX + o.toString() );
            }
    	}
    }
    
	/**
	 * Prefixes message with WARNING and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
	 * if current level is {@link #WARNING_LEVEL} or below
	 */
	public final void warning( Object o ) {
        if( currentLevel <= WARNING_LEVEL ) {
            if( useColor ) {
                outputMessage( COLORED_WARNING_PREFIX + o.toString() );
            }
            else {
                outputMessage( WARNING_PREFIX + o.toString() );
            }
        }
    }
    
	/**
	 * Prefixes message with ERROR and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)}
	 * if current level is {@link #ERROR_LEVEL} or below
	 */
    public final void error( Object o ) {
        if( currentLevel <= ERROR_LEVEL ) {
            if( useColor ) {
                outputMessage( COLORED_ERROR_PREFIX + o.toString() );
            }
            else {
                outputMessage( ERROR_PREFIX + o.toString() );
            }
        }
    }
    
	/**
	 * Prefixes message with FATAL and delegates to {@link AbstractRiseClipseConsole#doOutputMessage(String)},
	 * then throws a {@link RiseClipseFatalException}
	 */
    public final void fatal( Object o ) {
        if( useColor ) {
            outputMessage( COLORED_FATAL_PREFIX + o.toString() );
        }
        else {
            outputMessage( FATAL_PREFIX + o.toString() );
        }
		throw new RiseClipseFatalException(  "FATAL: " + o.toString(), null );
    }
    
    private void outputMessage( String m ) {
        if( displayedMessages != null ) {
            if( displayedMessages.contains( m )) {
                return;
            }
            displayedMessages.add( m );
        }
        doOutputMessage( m );
    }

    /**
     * Output message s on the current console
     * @param m message to display
     */
    protected abstract void doOutputMessage( String m );

    @Override
    public void displayIdenticalMessages() {
        displayedMessages = null;        
    }

    @Override
    public void doNotDisplayIdenticalMessages() {
        displayedMessages = new HashSet< String >();
    }
    
}
