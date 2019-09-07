package ru.otus.library.secutity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AclCreationUtil {

    private final MutableAclService aclService;


    private final GrantedAuthoritySid ROLE_ADMIN = new GrantedAuthoritySid("ROLE_ADMIN");
    private final GrantedAuthoritySid ROLE_USER = new GrantedAuthoritySid("ROLE_USER");

    public void createDefaultAcl(ObjectIdentity oid) {
        MutableAcl acl=null;
        try {
            acl = aclService.createAcl(oid);
        } catch (AlreadyExistsException e) {
            log.info(e.getMessage());
            return;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid owner = new PrincipalSid(auth);
        acl.setOwner(owner);


        acl.insertAce(acl.getEntries().size(), BasePermission.READ, ROLE_ADMIN, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, ROLE_ADMIN, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, ROLE_ADMIN, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, ROLE_ADMIN, true);

        acl.insertAce(acl.getEntries().size(), BasePermission.READ, ROLE_USER, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, owner, true);

        aclService.updateAcl(acl);

    }
}
