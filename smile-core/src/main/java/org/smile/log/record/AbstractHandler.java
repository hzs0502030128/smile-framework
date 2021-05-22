package org.smile.log.record;

import org.smile.log.Logger.Level;

public abstract class AbstractHandler implements Handler{
	
	protected RecordFormatter formatter=new SimpleRecordFormatter();
	
	protected HandleFilter filter;
	
	protected Level level;
	
	protected boolean needLog(LogRecord record){
		if(filter!=null&&!filter.isLoggable(record)){
			return false;
		}
		return record.getLevel().isEnabledFor(level);
	}

	@Override
	public void setFilter(HandleFilter filter) {
		this.filter=filter;
	}


	@Override
	public void setFormatter(RecordFormatter formatter) {
		this.formatter=formatter;
	}


	@Override
	public void setLevel(Level level) {
		this.level=level;
	}
}
