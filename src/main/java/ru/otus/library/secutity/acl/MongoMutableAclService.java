package ru.otus.library.secutity.acl;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import ru.otus.library.secutity.acl.domain.MongoAceImpl;
import ru.otus.library.secutity.acl.domain.MongoAclImpl;
import ru.otus.library.secutity.acl.repository.MyAclRepository;

import java.util.List;
import java.util.Map;


public class MongoMutableAclService extends MongoAclService implements MutableAclService {

    private final AclCache aclCache;
    private final LookupStrategy lookupStrategy;

    public MongoMutableAclService(MyAclRepository aclRepository, LookupStrategy lookupStrategy, AclCache aclCache) {
        super(aclRepository, lookupStrategy);
        this.aclCache=aclCache;
        this.lookupStrategy=lookupStrategy;
    }

    @Override
    public MutableAcl createAcl(ObjectIdentity objectIdentity) throws AlreadyExistsException {
        Assert.notNull(objectIdentity, "Object Identity required");

        // Check this object identity hasn't already been persisted
        if (aclRepository.findFirstByObjectIdentity(objectIdentity) != null) {
            throw new AlreadyExistsException("Object identity '" + objectIdentity
                    + "' already exists");
        }

        // Need to retrieve the current principal, in order to know who "owns" this ACL
        // (can be changed later on)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid sid = new PrincipalSid(auth);


        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval
        // etc)
        Acl acl = readAclById(objectIdentity);
        Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");


        return (MutableAcl) aclRepository.save((MongoAclImpl) acl);
    }

    @Override
    public void deleteAcl(ObjectIdentity objectIdentity, boolean deleteChildren) throws ChildrenExistException {
        Assert.notNull(objectIdentity, "Object Identity required");
        Assert.notNull(objectIdentity.getIdentifier(),
                "Object Identity doesn't provide an identifier");

        if (deleteChildren) {
            List<ObjectIdentity> children = findChildren(objectIdentity);
            if (children != null) {
                for (ObjectIdentity child : children) {
                    deleteAcl(child, true);
                }
            }
        }

//        Long oidPrimaryKey = retrieveObjectIdentityPrimaryKey(objectIdentity);

//        // Delete this ACL's ACEs in the acl_entry table
//        deleteEntries(oidPrimaryKey);
//
//        // Delete this ACL's acl_object_identity row
//        deleteObjectIdentity(oidPrimaryKey);

        // Clear the cache
        aclCache.evictFromCache(objectIdentity);
    }

    @Override
    public MutableAcl updateAcl(MutableAcl acl) throws NotFoundException {
        MongoAclImpl mongoAcl = aclRepository.findById(acl.getId().toString())
                .orElseThrow(() -> new NotFoundException("No entry for ACL " + acl.getId() + " found"));


        // Update the acl entry
        aclRepository.save(mongoAcl);

        // Clear the cache, including children
        clearCacheIncludingChildren(acl.getObjectIdentity());

        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval etc)
        return (MutableAcl) readAclById(acl.getObjectIdentity());
    }

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> result = lookupStrategy.readAclsById(objects, sids);

        for (ObjectIdentity oid : objects) {
            if (!result.containsKey(oid)) {
                result.put(oid,getDenyAcl(oid,sids));
            }
        }

        return result;
    }

    private Acl getDenyAcl(ObjectIdentity oid,List<Sid> sids){
        MongoAclImpl acl = new MongoAclImpl();
       if (lookupStrategy instanceof MongoLookupStrategy) {
           MongoLookupStrategy mongoLookup= (MongoLookupStrategy) lookupStrategy;
           acl.setAclAuthorizationStrategy(mongoLookup.getAclAuthorizationStrategy());
           acl.setPermissionGrantingStrategy(mongoLookup.getGrantingStrategy());
       } else throw new RuntimeException("Unknown Lookup Strategy");
        acl.setObjectIdentity(oid);
        CumulativePermission permission = new CumulativePermission();
        permission.set(BasePermission.ADMINISTRATION);
        permission.set(BasePermission.READ);
        permission.set(BasePermission.DELETE);
        permission.set(BasePermission.CREATE);
        permission.set(BasePermission.WRITE);
        if (sids !=null) {
            acl.setLoadedSids(sids);
            for (int i = 0; i < sids.size(); i++) {
                acl.getAces().add(new MongoAceImpl(i, acl, sids.get(i),BasePermission.READ , true, true, true));
            }
        }
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth !=null) {

        }

        return acl;
    }

    private void clearCacheIncludingChildren(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "ObjectIdentity required");
        List<ObjectIdentity> children = findChildren(objectIdentity);
        if (children != null) {
            for (ObjectIdentity child : children) {
                clearCacheIncludingChildren(child);
            }
        }
        aclCache.evictFromCache(objectIdentity);
    }
}
