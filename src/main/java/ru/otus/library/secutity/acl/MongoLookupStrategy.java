package ru.otus.library.secutity.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.security.util.FieldUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.otus.library.secutity.acl.domain.MongoAcl;
import ru.otus.library.secutity.acl.domain.ObjectPermission;
import ru.otus.library.util.CustomLog;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class MongoLookupStrategy implements LookupStrategy {

    private final AclCache aclCache;

    private final MongoTemplate mongoTemplate;

    private final AclAuthorizationStrategy aclAuthorizationStrategy;

    private final PermissionGrantingStrategy grantingStrategy;

    private final PermissionFactory permissionFactory = new DefaultPermissionFactory();

    private int batchSize = 50;
    private final Field fieldAces = FieldUtils.getField(AclImpl.class, "aces");
    
    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) {
        Map<ObjectIdentity, Acl> result = new HashMap<>();
        Set<ObjectIdentity> currentBatchToLoad = new HashSet<>();

        for (int i = 0; i < objects.size(); i++) {
            final ObjectIdentity oid = objects.get(i);
            boolean aclFound = false;

            // Check we don't already have this ACL in the results
            if (result.containsKey(oid)) {
                aclFound = true;
            }

            // Check cache for the present ACL entry
            if (!aclFound) {
                Acl acl = aclCache.getFromCache(oid);

                // Ensure any cached element supports all the requested SIDs
                // (they should always, as our base impl doesn't filter on SID)
                if (acl != null) {
                    if (acl.isSidLoaded(sids)) {
                        if (definesAccessPermissionsForSids(acl, sids)) {
                            result.put(acl.getObjectIdentity(), acl);
                            aclFound = true;
                        }
                    } else {
                        throw new IllegalStateException("Error: SID-filtered element detected when implementation does not perform SID filtering "
                                + "- have you added something to the cache manually?");
                    }
                }
            }

            // Load the ACL from the database
            if (!aclFound) {
                currentBatchToLoad.add(oid);
            }

            // Is it time to load from Mongo the currentBatchToLoad?
            if ((currentBatchToLoad.size() == this.batchSize) || ((i + 1) == objects.size())) {
                if (currentBatchToLoad.size() > 0) {
                    Map<ObjectIdentity, Acl> loadedBatch = lookupObjectIdentities(currentBatchToLoad, sids);

                    // Add loaded batch (all elements 100% initialized) to results
                    result.putAll(loadedBatch);

                    currentBatchToLoad.clear();
                }
            }
        }

        return result;
    }


    
    private boolean definesAccessPermissionsForSids(Acl acl, List<Sid> sids) {
        // check whether the list of sids is a match-all list or if the owner is found within the list
        if (sids == null || sids.isEmpty() || sids.contains(acl.getOwner())) {
            return true;
        }
        // check the contained permissions for permissions granted to a certain user available in the provided list of sids
        if (hasPermissionsForSids(acl, sids)) {
            return true;
        }
        // check if a parent reference is available and inheritance is enabled
        if (acl.getParentAcl() != null && acl.isEntriesInheriting()) {
            if (definesAccessPermissionsForSids(acl.getParentAcl(), sids)) {
                return true;
            }

            return hasPermissionsForSids(acl.getParentAcl(), sids);
        }
        return false;
    }

    
    private boolean hasPermissionsForSids(Acl acl, List<Sid> sids) {
        for (AccessControlEntry ace : acl.getEntries()) {
            if (sids.contains(ace.getSid())) {
                return true;
            }
        }
        return false;
    }
    
    private Map<ObjectIdentity, Acl> lookupObjectIdentities(
            final Collection<ObjectIdentity> objectIdentities, List<Sid> sids) {

        Set<Serializable> objectIds = new LinkedHashSet<>();
        Set<String> types = new LinkedHashSet<>();
        for (ObjectIdentity domainObject : objectIdentities) {
            objectIds.add(domainObject.getIdentifier());
            types.add(domainObject.getType());
        }
        Criteria where = Criteria.where("instanceId").in(objectIds).and("className").in(types);
        List<MongoAcl> foundAcls = mongoTemplate.find(query(where).with(new Sort(Sort.Direction.ASC, "instanceId", "permissions.position")), MongoAcl.class);

        Map<ObjectIdentity, Acl> resultMap = new HashMap<>();

        for (MongoAcl foundAcl : new ArrayList<>(foundAcls)) {
            Acl acl = null;
            try {
                acl = convertToAcl(foundAcl, foundAcls);
            } catch (ClassNotFoundException cnfEx) {
                // TODO: add exception logging
            }
            if (null != acl) {
                // check if the ACL does define access rules for any of the sids available in the given list
                // owners and parent owners have full access on the ACE/domain object while other users have to be looked up
                // within the permissions
                if (definesAccessPermissionsForSids(acl, sids)) {
                    resultMap.put(acl.getObjectIdentity(), acl);
                }
            }
        }

        return resultMap;
    }

    
    private Acl convertToAcl(MongoAcl mongoAcl, List<MongoAcl> foundAcls) throws ClassNotFoundException {
        Acl parent = null;
        if (mongoAcl.getParentId() != null) {
            MongoAcl parentAcl = null;
            // attempt to load a parent ACL from the list of loaded ACLs
            for (MongoAcl found : foundAcls) {
                if (found.getId().equals(mongoAcl.getParentId())) {
                    parentAcl = found;
                    break;
                }
            }
            // if the parent ACL was not loaded already, try to find it via its id
            if (null == parentAcl) {
                parentAcl = mongoTemplate.findById(mongoAcl.getParentId(), MongoAcl.class);
            }
            if (parentAcl != null) {
                if (!foundAcls.contains(parentAcl)) {
                    foundAcls.add(parentAcl);
                }
                Acl cachedParent = aclCache.getFromCache(new ObjectIdentityImpl(parentAcl.getClassName(), parentAcl.getInstanceId()));
                if (null == cachedParent) {
                    parent = convertToAcl(parentAcl, foundAcls);
                    aclCache.putInCache((MutableAcl) parent);
                } else {
                    parent = cachedParent;
                }
            } else {
                // TODO: Log warning that no parent could be found
            }
        }
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Class.forName(mongoAcl.getClassName()), mongoAcl.getInstanceId());
        Sid owner;
        if (mongoAcl.getOwner().isPrincipal()) {
            owner = new PrincipalSid(mongoAcl.getOwner().getName());
        } else {
            owner = new GrantedAuthoritySid(mongoAcl.getOwner().getName());
        }
        AclImpl acl = new AclImpl(objectIdentity, mongoAcl.getId(), aclAuthorizationStrategy, grantingStrategy, parent,
                null, mongoAcl.isInheritPermissions(), owner);

        for (ObjectPermission permission : mongoAcl.getPermissions()) {
            Sid sid;
            if (permission.getSid().isPrincipal()) {
                sid = new PrincipalSid(permission.getSid().getName());
            } else {
                sid = new GrantedAuthoritySid(permission.getSid().getName());
            }
            Permission permissions = permissionFactory.buildFromMask(permission.getPermission());
            AccessControlEntryImpl ace =
                    new AccessControlEntryImpl(permission.getId(), acl, sid, permissions,
                            permission.isGranting(), permission.isAuditSuccess(), permission.isAuditFailure());
            // directly adding this permission entry to the Acl isn't possible as the returned list by acl.getEntries()
            // is a copy of the internal list and acl.insertAce(...) requires elevated security permissions
            // acl.getEntries().add(ace);
            // acl.insertAce(acl.getEntries().size(), permissions, user, permission.isGranting());
            List<AccessControlEntryImpl> aces = readAces(acl);
            aces.add(ace);
        }

        // add the loaded ACL to the cache
        aclCache.putInCache(acl);

        return acl;
    }
    
    private List<AccessControlEntryImpl> readAces(AclImpl acl) {
        try {
            Field field = AclImpl.class.getDeclaredField("aces");
            field.setAccessible(true);
            return (List<AccessControlEntryImpl>) field.get(acl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
