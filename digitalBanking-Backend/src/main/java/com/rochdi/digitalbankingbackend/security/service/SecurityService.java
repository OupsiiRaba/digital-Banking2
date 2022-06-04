package com.rochdi.digitalbankingbackend.security.service;

import com.rochdi.digitalbankingbackend.security.entities.AppRole;
import com.rochdi.digitalbankingbackend.security.entities.AppUser;

import java.util.List;

public interface SecurityService {

    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser( String username, String roleName);
    AppUser loadUserByUsername( String username);
    List<AppUser> listUsers();
}
