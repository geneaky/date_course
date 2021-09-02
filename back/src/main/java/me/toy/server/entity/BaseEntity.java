package me.toy.server.entity;


import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt;
}
