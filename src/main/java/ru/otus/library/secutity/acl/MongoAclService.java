package ru.otus.library.secutity.acl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import ru.otus.library.secutity.acl.domain.MongoAcl;
import ru.otus.library.secutity.acl.repository.AclRepository;


import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoAclService implements AclService {

    private final AclRepository aclRepository;

    private final LookupStrategy lookupStrategy;

    
    @Override
    public List<ObjectIdentity> findChildren(ObjectIdentity objectIdentity) {
        List<MongoAcl> aclsForDomainObject =
                aclRepository.findByInstanceIdAndClassName(objectIdentity.getIdentifier(), objectIdentity.getType());
        if (null == aclsForDomainObject || aclsForDomainObject.isEmpty()) {
            return null;
        }
        Set<MongoAcl> children = new HashSet<>();
        // find children for each found ACL entity
        for (MongoAcl acl : aclsForDomainObject) {
            List<MongoAcl> childAclsOfDomainObject = aclRepository.findByParentId(acl.getId());
            children.addAll(childAclsOfDomainObject);
        }

        List<ObjectIdentity> foundChildren = new ArrayList<>();
        for (MongoAcl child : children) {
            try {
                ObjectIdentity oId = new ObjectIdentityImpl(Class.forName(child.getClassName()), child.getInstanceId());
                if (!foundChildren.contains(oId)) {
                    foundChildren.add(oId);
                }
            } catch (ClassNotFoundException cnfEx) {
                log.error("Could not find class of domain object '{}' referenced by ACL {}",
                        child.getClassName(), child.getId());
            }
        }
        return foundChildren;
    }
    
    @Override
    public Acl readAclById(ObjectIdentity objectIdentity) throws NotFoundException {
        return readAclById(objectIdentity, null);
    }
    
    @Override
    public Acl readAclById(ObjectIdentity objectIdentity, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> map = readAclsById(Collections.singletonList(objectIdentity), sids);
        return map.get(objectIdentity);
    }
    
    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> list) throws NotFoundException {
        return readAclsById(list, null);
    }
    
    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> result = lookupStrategy.readAclsById(objects, sids);
        return result;
    }
}
