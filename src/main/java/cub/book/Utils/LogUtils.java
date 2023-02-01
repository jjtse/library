package cub.book.Utils;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import cub.book.entity.LogEntity;
import cub.book.producer.BookProducer;

@ApplicationScoped
public class LogUtils {
	
	@Inject
	BookProducer bookProducer;

	public void message(String logType, String logSource,String logMessage) {
		
		LocalDateTime currentTime = LocalDateTime.now();
		LogEntity logEntity = new LogEntity();
		logEntity.setLogTime(currentTime);
		logEntity.setLogType(logType); // INFO or Error
		logEntity.setLogSource(logSource);// 方法 
		logEntity.setLogMessage(logMessage); //message
		bookProducer.sendLogToKafka(logEntity);
		
	}
}
