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

import org.eclipse.jdt.annotation.NonNull;

/**
 * Interface for outputting messages to users.
 * It allows for choosing the minimal level of displayed messages.
 *  
 * @author Dominique Marcadet
 *
 */
public interface IRiseClipseConsole {
	
    /**
     * Get the current level of displayed messages.
     * 
     * @return the current level
     */
    @NonNull Severity getLevel();
    
    /**
     * Set the current level of displayed messages.
     * 
     * @param level minimal level of messages to be displayed
     * @return the previous level
     */
    @NonNull Severity setLevel( @NonNull Severity level );
    
    /**
     * Get the current string used for formatting messages.
     * 
     * @return the current format string
     */
    @NonNull String getFormatString();
    
    /**
     * Set the string used for formatting messages.
     * This string will be given to a java.util.Formatter with the following argument positions: 
     *   1$ is severity
     *   2$ is category
     *   $3 is lineNumber
     *   $4 is message
     *   $5 is filename 
     *   $6 is the color start prefix
     *   $7 is the color end prefix
     * 
     * @param formatString string to use for formatting messages
     * @return             the previous format string
     */
    @NonNull String setFormatString( @NonNull String formatString );
    
    /**
     * Output category, line and messageParts, and exits
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void emergency( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        output( RiseClipseMessage.emergency( category, lineNumber, messageParts ));
        System.exit( -1 );
    }

    /**
     * Output category, filename, line and messageParts, and exits
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void emergency( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        output( RiseClipseMessage.emergency( category, filename, lineNumber, messageParts ));
        System.exit( -1 );
    }

    /**
     * Output category, line and messageParts if current level is ALERT or below
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void alert( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.ALERT.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.alert( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is ALERT or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void alert( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.ALERT.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.alert( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, line and messageParts if current level is CRITICAL or below
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void critical( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.CRITICAL.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.critical( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is CRITICAL or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void critical( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.CRITICAL.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.critical( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, line and messageParts if current level is ERROR or above
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void error( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.ERROR.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.error( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is ERROR or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void error( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.ERROR.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.error( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, line and messageParts if current level is WARNING or above
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void warning( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.WARNING.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.warning( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is WARNING or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void warning( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.WARNING.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.warning( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output Output category, line and messageParts if current level is NOTICE or above
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void notice( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.NOTICE.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.notice( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is NOTICE or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void notice( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.NOTICE.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.notice( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output Output category, line and messageParts if current level is INFO or above
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void info( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.INFO.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.info( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is INFO or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void info( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.INFO.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.info( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output Output category, line and messageParts if current level is INFO or above
     * 
     * @param category     category of the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void debug( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.DEBUG.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.debug( category, lineNumber, messageParts ));
        }
    }

    /**
     * Output category, filename, line and messageParts if current level is INFO or above
     * 
     * @param category     category of the message
     * @param filename     filename corresponding to the information indicated by the message
     * @param lineNumber   line number corresponding to the information indicated by the message
     * @param messageParts parts of the message to be displayed
     */
    default void debug( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        if( Severity.DEBUG.compareTo( getLevel() ) <= 0 ) {
            output( RiseClipseMessage.debug( category, filename, lineNumber, messageParts ));
        }
    }

    /**
     * Output message on the console
     * 
     * @param message message to be displayed
     */
    void output( @NonNull RiseClipseMessage message );

    /**
     * All messages will be displayed, even if an identical one has already been displayed
     */
    void displayIdenticalMessages();

    /**
     * Messages will be displayed only once. An identical one will be ignored
     */
    void doNotDisplayIdenticalMessages();

}

