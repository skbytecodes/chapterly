package com.chapterly;
import com.chapterly.entity.Order;
import java.time.LocalDateTime;

import com.chapterly.entity.Payment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class ChapterlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChapterlyApplication.class, args);
	}
}
