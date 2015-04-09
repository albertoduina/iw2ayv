package utils;


import ij.text.TextWindow;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyFileLogger {
	public static Logger logger;
	
	static {
		try {
			boolean append = true;
			FileHandler fh = new FileHandler("MyFileLog.txt", append);
			fh.setFormatter(new SimpleFormatter());
			logger = Logger.getLogger("MyFileLogger");
			logger.addHandler(fh);
			
			
			
			
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public static String here() {
		String out = "file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName()
				+ " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName();
		return out;
	}

	public static String here(String str) {
		String out = "file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName()
				+ " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " " + str;
		return out;
	}

}
