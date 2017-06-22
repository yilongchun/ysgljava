/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.vieking.sys.exception;

import java.io.PrintStream;
import java.io.PrintWriter;


public class MyRunException extends RuntimeException {


	private static final long serialVersionUID = -8422113809701300617L;
	
	Throwable throwable;


    public MyRunException() {
    }

    public MyRunException(String s) {
        super(s);
    }

    public MyRunException(Throwable cause) {
        this.throwable = cause;
    }

    public MyRunException(String s, Throwable cause) {
        super(s);
        this.throwable = cause;
    }


    public Throwable getThrowable() {
        return throwable;
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);

        if (throwable != null) {
            s.println("with nested exception " + throwable);
            throwable.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);

        if (throwable != null) {
            s.println("with nested exception " + throwable);
            throwable.printStackTrace(s);
        }
    }

    public String toString() {
        if (throwable == null) {
            return super.toString();
        }

        return super.toString() + "\n    with nested exception \n" + throwable.toString();
    }
}
