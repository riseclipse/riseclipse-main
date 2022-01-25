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

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Base class for RiseClipse consoles.
 * It implements the Singleton design pattern, handle the level of messages
 * and format them according to a Formatter.  
 *  
 * @author Dominique Marcadet
 *
 */
public abstract class AbstractRiseClipseConsole implements IRiseClipseConsole {

    // ANSI escape codes for colors
//    private static final String ANSI_RESET    = "\u001B[0m";
//    private static final String ANSI_BLACK    = "\u001B[30m";
//    private static final String ANSI_RED      = "\u001B[31m";
//    //private static final String ANSI_GREEN  = "\u001B[32m";
//    private static final String ANSI_YELLOW   = "\u001B[33m";
//    private static final String ANSI_BLUE     = "\u001B[34m";
//    //private static final String ANSI_PURPLE = "\u001B[35m";
//    private static final String ANSI_CYAN     = "\u001B[36m";
//    //private static final String ANSI_WHITE  = "\u001B[37m";
    
    // Message prefixes
//    private static final String VERBOSE_PREFIX = "VERBOSE: ";
//    private static final String    INFO_PREFIX = "INFO:    ";
//    private static final String WARNING_PREFIX = "WARNING: ";
//    private static final String   ERROR_PREFIX = "ERROR:   ";
//    private static final String   FATAL_PREFIX = "FATAL:   ";
    
    // Colored message prefixes
//    private static final String COLORED_VERBOSE_PREFIX = ANSI_CYAN   + VERBOSE_PREFIX + ANSI_RESET;
//    private static final String    COLORED_INFO_PREFIX = ANSI_BLUE   + INFO_PREFIX    + ANSI_RESET;
//    private static final String COLORED_WARNING_PREFIX = ANSI_YELLOW + WARNING_PREFIX + ANSI_RESET;
//    private static final String   COLORED_ERROR_PREFIX = ANSI_RED    + ERROR_PREFIX   + ANSI_RESET;
//    private static final String   COLORED_FATAL_PREFIX = ANSI_BLACK  + FATAL_PREFIX   + ANSI_RESET;
    
    /**
     * The unique instance of AbstractRiseClipseConsole
     */
    protected static @NonNull IRiseClipseConsole console = new TextRiseClipseConsole();
    
    /**
     * Give access to the singleton.
     * It creates a {@link TextRiseClipseConsole} if there is none.
     * 
     * @return The unique instance of AbstractRiseClipseConsole
     */
    public static synchronized @NonNull IRiseClipseConsole getConsole() {
        return console;
    }

    /**
     * Change the current singleton.
     * @param newConsole the new console to use
     */
    public static synchronized void changeConsole( @NonNull IRiseClipseConsole newConsole ) {
        console = newConsole;
    }

    /**
     * The current level of displayed messages
     */
    protected @NonNull Severity currentLevel = Severity.WARNING;

    /**
     * The string used to format messages
     *   1$ is severity
     *   2$ is category
     *   $3 is lineNumber
     *   $4 is message
     *   $5 is filename 
     *   $6 is the color start prefix
     *   $7 is the color end prefix
     */
    private @NonNull String formatString = "%1$-7s: [%2$s] %4$s at line %3$d";
    
    @Override
    public @NonNull String getFormatString() {
        return formatString;
    }

    @Override
    public @NonNull String setFormatString( @NonNull String formatString ) {
        String oldFormat = this.formatString;
        this.formatString = formatString;
        return oldFormat;
    }

    //    private String coloredFormatString = "$6s%1$7s$7s: [$2s] $4s at line $3d";
    
    /**
     * Whether to use color for message prefixes
     */
    private boolean useColor;
    
    /**
     * Messages which have been displayed once
     */
    private Set< String > displayedMessages;
    
    /**
     * Constructs a new console, using it as the unique one
     * (the previous one, if any, is forgotten).
     * The initial level is set to {@link Severity#WARNING}
     * Color is used according if argument is true
     */
    protected AbstractRiseClipseConsole( boolean useColor ) {
        console = this;
        this.useColor = useColor;
    }
    
    /**
     * Constructs a new console, using it as the unique one
     * (the previous one, if any, is forgotten).
     * The initial level is set to {@link Severity#WARNING}
     * Color is not used.
     */
    protected AbstractRiseClipseConsole() {
        this( false );
    }
    
    /**
     * Basic implementation of {@link IRiseClipseConsole#getLevel()}
     */
    @Override
    public @NonNull Severity getLevel() {
        return currentLevel;
    }

    /**
     * Basic implementation of {@link IRiseClipseConsole#setLevel(Severity)}
     */
    @Override
    public @NonNull Severity setLevel( @NonNull Severity level ) {
        Severity previousLevel = currentLevel;
        currentLevel = level;
        return previousLevel;
    }
    
    @Override
    public void output( @NonNull RiseClipseMessage message ) {
        if( currentLevel.compareTo( message.getSeverity() ) <= 0 ) {
            Formatter formatter = new Formatter();
            String m = formatter.format(
                    formatString,
                    message.getSeverity(),
                    message.getCategory(),
                    message.getLineNumber(),
                    message.getMessage()
            ).toString();
            formatter.close();
            if( displayedMessages != null ) {
                if( displayedMessages.contains( m )) {
                    return;
                }
                displayedMessages.add( m );
            }
            doOutputMessage( m );
        }
    }

    /**
     * Output message m on the current console
     * @param m message to display
     */
    protected abstract void doOutputMessage( @NonNull String m );

    @Override
    public void displayIdenticalMessages() {
        displayedMessages = null;        
    }

    @Override
    public void doNotDisplayIdenticalMessages() {
        displayedMessages = new HashSet< String >();
    }
    
}
