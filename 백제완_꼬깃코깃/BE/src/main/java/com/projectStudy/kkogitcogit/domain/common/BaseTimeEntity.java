package com.projectStudy.kkogitcogit.domain.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // Entity 상속
@EntityListeners(AuditingEntityListener.class) // 생성, 수정 이벤트 감지
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    // Soft Delete
    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt == null;
    }
}