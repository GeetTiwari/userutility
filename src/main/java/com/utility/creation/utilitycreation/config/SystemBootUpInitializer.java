package com.utility.creation.utilitycreation.config;

import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import com.utility.creation.utilitycreation.repository.UserRepo;
import com.utility.creation.utilitycreation.repository.UserRoleMappingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.utility.creation.utilitycreation.model.entity.Roles;
import com.utility.creation.utilitycreation.repository.RoleRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class SystemBootUpInitializer implements CommandLineRunner {

    private final RoleRepository rolesRepository;
    private final UserRepo userRepo;
    private final AdminUserProperties adminUserProperties;
    private final UserRoleMappingRepository userRoleMappingRepository;

    @Value("${app.roles.default}")
    private String[] defaultRoles;

    public SystemBootUpInitializer(RoleRepository rolesRepository, UserRepo userRepo, AdminUserProperties adminUserProperties, UserRoleMappingRepository userRoleMappingRepository) {
        this.rolesRepository = rolesRepository;
        this.userRepo = userRepo;
        this.adminUserProperties = adminUserProperties;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    @Override
    public void run(String... args) {
        if (rolesRepository.count() == 0) {
            int id = 1;
            for (String roleName : defaultRoles) {
                Roles role = new Roles(id++, roleName);
                rolesRepository.save(role);
            }
            System.out.println("Default roles initialized.");
            initAdminUser();

        } else {
            System.out.println("Roles already exist in the database.");
        }
    }

    public void initAdminUser() {
        try {
            String adminEmail = adminUserProperties.getEmail();
            String adminPassword = adminUserProperties.getPassword();
            String adminRoleName = adminUserProperties.getRole();

            // Check if the admin user already exists
            if (userRepo.findByEmail(adminEmail) == null) {
                // Fetch the ADMIN role (DO NOT CREATE IT)
                Roles adminRole = rolesRepository.findByRoleName(adminRoleName);
                if (adminRole == null) {
                    System.err.println("X ERROR: Admin role '" + adminRoleName + "' not found! Please ensure it exists.");
                    return;
                }

                // Create Admin User
                User adminUser = new User();
                String adminUserId = String.valueOf(Math.abs(UUID.randomUUID().hashCode()));
                adminUser.setId(adminUserId);
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(adminPassword); // Consider encoding it
                adminUser.setFirstName(adminUserProperties.getFirstName());
                adminUser.setLastName(adminUserProperties.getLastName());
                adminUser.setPhone(adminUserProperties.getPhone());
                adminUser.setCreatedDate(LocalDate.now());
                adminUser.setCreatedTime(LocalTime.now());
                // Save the Admin User
                userRepo.save(adminUser);

                // Assign Role to Admin User
                UserRoleMapping mapping = new UserRoleMapping();
                mapping.setMid("UserRoleMapping:mid:" + adminUser.getId());
                mapping.setId(adminUser.getId());
                mapping.setRoleId(adminRole.getRoleId());

                // Save the Role Mapping
                userRoleMappingRepository.save(mapping);
                System.out.println("Admin user created successfully!");
            } else {
                System.out.println("Admin user already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error during initialization: " + e.getMessage());
        }
    }
}
