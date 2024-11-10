package com.app.persistence.repository;

import com.app.persistence.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Page<Notification> findAllByUserId(Long userId, Pageable pageable);
}
