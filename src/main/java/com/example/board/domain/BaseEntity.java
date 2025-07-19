package com.example.board.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
	@CreatedDate //엔티티가 최초 한번 persist 될 때 자동으로 현재 시각 주입
	@Column(name = "create_date", updatable = false)
	private LocalDateTime createDate;

	@LastModifiedDate //엔티티가 INSERT 되거나 UPDATE 될 때마다 자동으로 현재 시각 주입
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

}
