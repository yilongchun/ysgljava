package com.vieking.sys.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ResourceException extends RuntimeException {

	private static final long serialVersionUID = -8713976839240872528L;

	Throwable throwable;

	public ResourceException() {
	}

	public ResourceException(String s) {
		super(s);
	}

	public ResourceException(String s, Throwable cause) {
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

		return super.toString() + "\n    with nested exception \n"
				+ throwable.toString();
	}
}
