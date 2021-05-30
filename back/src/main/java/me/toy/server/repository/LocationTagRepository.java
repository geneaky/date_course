package me.toy.server.repository;

import me.toy.server.entity.LocationTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationTagRepository extends JpaRepository<LocationTag,Long> {
}
