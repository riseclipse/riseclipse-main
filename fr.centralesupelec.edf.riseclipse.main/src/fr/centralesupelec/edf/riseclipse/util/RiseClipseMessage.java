/*
*************************************************************************
**  Copyright (c) 2022 CentraleSupélec & EDF.
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

public class RiseClipseMessage {
    private @NonNull Severity severity;
    private @NonNull String category;
    private String filename;
    private int lineNumber;
    private @NonNull Object[] messageParts;
    
    // Conversion to String and concatenation is only done in getMessage()
    // to avoid doing it if message is not displayed.
    public RiseClipseMessage( @NonNull Severity severity, @NonNull String category, int line, @NonNull Object... messageParts ) {
        super();
        this.severity = severity;
        this.category = category;
        this.lineNumber = line;
        this.messageParts = messageParts;
    }

    public static RiseClipseMessage emergency( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.EMERGENCY, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage emergency( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.EMERGENCY, category, lineNumber, messageParts ).setFilename( filename );
    }
    
    public static RiseClipseMessage alert( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ALERT, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage alert( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ALERT, category, lineNumber, messageParts ).setFilename( filename );
    }

    public static RiseClipseMessage critical( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.CRITICAL, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage critical( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.CRITICAL, category, lineNumber, messageParts ).setFilename( filename );
    }

    public static RiseClipseMessage error( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ERROR, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage error( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ERROR, category, lineNumber, messageParts ).setFilename( filename );
    }

    public static RiseClipseMessage warning( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.WARNING, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage warning( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.WARNING, category, lineNumber, messageParts ).setFilename( filename );
    }

    public static RiseClipseMessage notice( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.NOTICE, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage notice( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.NOTICE, category, lineNumber, messageParts ).setFilename( filename );
    }

    public static RiseClipseMessage info( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.INFO, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage info( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.INFO, category, lineNumber, messageParts ).setFilename( filename );
    }

    public static RiseClipseMessage debug( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.DEBUG, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage debug( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.DEBUG, category, lineNumber, messageParts ).setFilename( filename );
    }

    public @NonNull Severity getSeverity() {
        return severity;
    }
    
    public @NonNull String getCategory() {
        return category;
    }
    
    public @NonNull String getFilename() {
        return filename;
    }
    
    public RiseClipseMessage setFilename( @NonNull String filename ) {
        this.filename = filename;
        return this;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public @NonNull String getMessage() {
        return toString( messageParts );
    }
    
    /**
     * Utility to create a String by concatenation of parts
     */
    private static @NonNull String toString( @NonNull Object... messageParts ) {
        StringBuilder s = new StringBuilder();
        for( int i = 0; i < messageParts.length; ++i ) s.append( messageParts[i].toString() );
        return s.toString();
    }
    
}
