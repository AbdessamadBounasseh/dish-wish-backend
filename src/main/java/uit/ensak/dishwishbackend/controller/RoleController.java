package uit.ensak.dishwishbackend.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.RoleNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

//    @PostMapping("/save")
//    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
//        roleService.saveRole(role);
//        return ResponseEntity.status(HttpStatus.CREATED).body(role);
//    }

    @PostMapping("/add-to-user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleUserForm form) throws ClientNotFoundException, RoleNotFoundException {
        roleService.addRoleToUser(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}

@Getter
@Setter
class RoleUserForm {
    private String email;
    private String roleName;
}