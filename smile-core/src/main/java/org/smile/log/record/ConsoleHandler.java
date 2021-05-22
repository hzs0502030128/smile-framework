package org.smile.log.record;

import org.smile.log.Logger;


public class ConsoleHandler extends  AbstractHandler{
	
	@Override
	public void handle(LogRecord record,Throwable throwable) {
		if(needLog(record)){
			System.out.println(formatter.format(record));
			if (throwable != null) {
				if(record.getLevel()==Logger.Level.ERROR){
					throwable.printStackTrace(System.err);
				}else{
					throwable.printStackTrace(System.out);
				}
			}
		}
	}

}
