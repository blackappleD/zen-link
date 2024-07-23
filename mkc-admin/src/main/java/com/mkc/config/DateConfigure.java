package com.mkc.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class DateConfigure {

	@Bean
	public Converter<String, LocalDate> string2LocalDate() {
		return new Converter<String, LocalDate>() {
			@Override
			public LocalDate convert(String s) {
				if(StringUtils.isBlank(s)) return null;
				return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
		};
	}

	@Bean
	public Converter<LocalDate, String> localDate2String() {
		return new Converter<LocalDate, String>() {
			@Override
			public String convert(LocalDate localDate) {
				if(localDate == null) return null;
				return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
		};
	}
	
	@Bean
	public Converter<String, LocalDateTime> string2LocalDateTime() {
		return new Converter<String, LocalDateTime>() {
			@Override
			public LocalDateTime convert(String s) {
				if(StringUtils.isBlank(s)) return null;
				String format = "yyyy-MM-dd HH:mm:ss";
				if(s.length() == 23) format = "yyyy-MM-dd HH:mm:ss.SSS";
				return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(format));
			}
		};
	}
}
