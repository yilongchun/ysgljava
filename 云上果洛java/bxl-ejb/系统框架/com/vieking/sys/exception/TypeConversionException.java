/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.vieking.sys.exception;


public class TypeConversionException extends MyRunException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2170411216560830890L;

	/**
     * Constructs a <code>XworkException</code> with no detail  message.
     */
    public TypeConversionException() {
    }

    /**
     * Constructs a <code>XworkException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public TypeConversionException(String s) {
        super(s);
    }

    /**
     * Constructs a <code>XworkException</code> with no detail  message.
     */
    public TypeConversionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a <code>XworkException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public TypeConversionException(String s, Throwable cause) {
        super(s, cause);
    }
}
