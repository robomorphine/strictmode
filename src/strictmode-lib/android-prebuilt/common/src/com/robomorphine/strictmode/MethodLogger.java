package com.robomorphine.strictmode;

import android.util.Log;

public class MethodLogger {
	
	public static void logMethod(boolean printArgs, Object... args) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		String className = "unknown";
		String methodName = "unknown";
		if (elements.length >= 3) {
			className = elements[3].getClassName();
			methodName = elements[3].getMethodName();
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(className);
		builder.append(".");
		builder.append(methodName);
		if (printArgs) {
			builder.append("( ");
			for (int i = 0; i < args.length; i++) {
				if (i != 0) {
					builder.append(", ");
				}
				builder.append(args[i].getClass().getSimpleName());
				builder.append(" = ");
				builder.append("\"");
				builder.append(args[i].toString());
				builder.append("\"");
			}
		} else {
			builder.append("()");
		}
		Log.e(MethodLogger.class.getSimpleName(), builder.toString());
	}
}
