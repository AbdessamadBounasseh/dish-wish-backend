package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.RoleNotFoundException;
import uit.ensak.dishwishbackend.model.Role;

public interface IRoleSevice {

    Role saveRole(Role role);

    void addRoleToUser(String email, String roleName) throws ClientNotFoundException, RoleNotFoundException;
}
