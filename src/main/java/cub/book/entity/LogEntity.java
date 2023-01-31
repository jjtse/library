package cub.book.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tb_log")
public class LogEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="log_id")	
	private Integer logId;
	
	@Column(name="log_time")
	private LocalDateTime logTime;
	
	@Column(name="log_type")	
	private String logType;	
	
	@Column(name="log_source")	
	private String logSource;

	@Column(name="log_message")	
	private String logMessage;	
	
}
