package com.hnh.service.general;

import com.hnh.dto.general.NotificationResponse;

public interface NotificationService {

    void pushNotification(String uniqueKey, NotificationResponse notification);

}

