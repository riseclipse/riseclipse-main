/**
 *  Copyright (c) 2017 CentraleSupélec & EDF.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  This file is part of the RiseClipse tool
 *  
 *  Contributors:
 *      Computer Science Department, CentraleSupélec : initial implementation
 *  Contacts:
 *      Dominique.Marcadet@centralesupelec.fr
 */
package fr.centralesupelec.edf.riseclipse.util;

/**
 * This class is for fatal exceptions in RiseClipse, they must
 * not be caught.
 * 
 * @author Dominique Marcadet
 *
 */
public class RiseClipseFatalException extends RiseClipseRuntimeException {

	private static final long serialVersionUID = 1L;

	public RiseClipseFatalException( String error, Exception e ) {
		super( error, e );
	}

}
