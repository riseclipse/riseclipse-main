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

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

public class RiseClipseMessage {
    private final @NonNull Severity severity;
    private final @NonNull String category;
    private final @NonNull String filename;
    private final int lineNumber;
    private final @NonNull Object[] messageParts;
    
    // Conversion to String and concatenation is only done in getMessage()
    // to avoid doing it if message is not displayed.
    public RiseClipseMessage( @NonNull Severity severity, @NonNull String category, int line, @NonNull Object... messageParts ) {
        super();
        this.severity = severity;
        this.category = category;
        this.filename = "";
        this.lineNumber = line;
        this.messageParts = messageParts;
    }

    public RiseClipseMessage( @NonNull Severity severity, @NonNull String category, @NonNull String filename, int line, @NonNull Object... messageParts ) {
        super();
        this.severity = severity;
        this.category = category;
        this.filename = filename;
        this.lineNumber = line;
        this.messageParts = messageParts;
    }

    public static RiseClipseMessage emergency( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.EMERGENCY, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage emergency( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.EMERGENCY, category, filename, lineNumber, messageParts );
    }
    
    public static RiseClipseMessage alert( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ALERT, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage alert( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ALERT, category, filename, lineNumber, messageParts );
    }

    public static RiseClipseMessage critical( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.CRITICAL, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage critical( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.CRITICAL, category, filename, lineNumber, messageParts );
    }

    public static RiseClipseMessage error( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ERROR, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage error( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.ERROR, category, filename, lineNumber, messageParts );
    }

    public static RiseClipseMessage warning( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.WARNING, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage warning( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.WARNING, category, filename, lineNumber, messageParts );
    }

    public static RiseClipseMessage notice( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.NOTICE, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage notice( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.NOTICE, category, filename, lineNumber, messageParts );
    }

    public static RiseClipseMessage info( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.INFO, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage info( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.INFO, category, filename, lineNumber, messageParts );
    }

    public static RiseClipseMessage debug( @NonNull String category, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.DEBUG, category, lineNumber, messageParts );
    }
    public static RiseClipseMessage debug( @NonNull String category, @NonNull String filename, int lineNumber, @NonNull Object... messageParts ) {
        return new RiseClipseMessage( Severity.DEBUG, category, filename, lineNumber, messageParts );
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode( messageParts );
        result = prime * result + Objects.hash( category, filename, lineNumber, severity );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        RiseClipseMessage other = ( RiseClipseMessage ) obj;
        return Objects.equals( category, other.category ) && Objects.equals( filename, other.filename )
                && lineNumber == other.lineNumber && Arrays.deepEquals( messageParts, other.messageParts )
                && severity == other.severity;
    }
    
}
