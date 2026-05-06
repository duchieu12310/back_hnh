package com.hnh.service.auth;

public interface UserManagementService {
    void approveUser(Long userId);
    void rejectUser(Long userId, String reason);
}
