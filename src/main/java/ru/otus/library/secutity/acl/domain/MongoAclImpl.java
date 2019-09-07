package ru.otus.library.secutity.acl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
public class MongoAclImpl implements Acl, MutableAcl, AuditableAcl, OwnershipAcl {


    @Id
    private String id;
    private Acl parentAcl;
    @Transient
    private transient AclAuthorizationStrategy aclAuthorizationStrategy;
    @Transient
    private transient PermissionGrantingStrategy permissionGrantingStrategy;
    private List<AccessControlEntry> aces = new ArrayList<>();
    private ObjectIdentity objectIdentity;

    private Sid owner; // OwnershipAcl
    private List<Sid> loadedSids = null; // includes all SIDs the WHERE clause covered,
    // even if there was no ACE for a SID
    private boolean entriesInheriting = true;


    public MongoAclImpl(ObjectIdentity objectIdentity, String id,
                   AclAuthorizationStrategy aclAuthorizationStrategy, AuditLogger auditLogger) {
            this.objectIdentity = objectIdentity;
        this.id = id;
        this.aclAuthorizationStrategy = aclAuthorizationStrategy;
        this.permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(
                auditLogger);
    }

    public MongoAclImpl(){

    }

    @Override
    public void deleteAce(int aceIndex) throws NotFoundException {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_GENERAL);
        verifyAceIndexExists(aceIndex);

        synchronized (aces) {
            this.aces.remove(aceIndex);
        }
    }

    private void verifyAceIndexExists(int aceIndex) {
        if (aceIndex < 0) {
            throw new NotFoundException("aceIndex must be greater than or equal to zero");
        }
        if (aceIndex >= this.aces.size()) {
            throw new NotFoundException(
                    "aceIndex must refer to an index of the AccessControlEntry list. "
                            + "List size is " + aces.size() + ", index was " + aceIndex);
        }
    }

    @Override
    public void insertAce(int atIndexLocation, Permission permission, Sid sid,
                          boolean granting) throws NotFoundException {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_GENERAL);
        Assert.notNull(permission, "Permission required");
        Assert.notNull(sid, "Sid required");
        if (atIndexLocation < 0) {
            throw new NotFoundException(
                    "atIndexLocation must be greater than or equal to zero");
        }
        if (atIndexLocation > this.aces.size()) {
            throw new NotFoundException(
                    "atIndexLocation must be less than or equal to the size of the AccessControlEntry collection");
        }

        MongoAceImpl ace = new MongoAceImpl(null, this, sid,
                permission, granting, false, false);

        synchronized (aces) {
            this.aces.add(atIndexLocation, ace);
        }
    }

    @Override
    public List<AccessControlEntry> getEntries() {
        // Can safely return AccessControlEntry directly, as they're immutable outside the
        // ACL package
        return new ArrayList<>(aces);
    }

    @Override
    public Serializable getId() {
        return this.id;
    }

    @Override
    public ObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }

    @Override
    public boolean isEntriesInheriting() {
        return entriesInheriting;
    }

    /**
     * Delegates to the {@link PermissionGrantingStrategy}.
     *
     * @throws UnloadedSidException if the passed SIDs are unknown to this ACL because the
     * ACL was only loaded for a subset of SIDs
     * @see DefaultPermissionGrantingStrategy
     */
    @Override
    public boolean isGranted(List<Permission> permission, List<Sid> sids,
                             boolean administrativeMode) throws NotFoundException, UnloadedSidException {
        Assert.notEmpty(permission, "Permissions required");
        Assert.notEmpty(sids, "SIDs required");

        if (!this.isSidLoaded(sids)) {
            throw new UnloadedSidException("ACL was not loaded for one or more SID");
        }

        return permissionGrantingStrategy.isGranted(this, permission, sids,
                administrativeMode);
    }

    @Override
    public boolean isSidLoaded(List<Sid> sids) {
        // If loadedSides is null, this indicates all SIDs were loaded
        // Also return true if the caller didn't specify a SID to find
        if ((this.loadedSids == null) || (sids == null) || (sids.size() == 0)) {
            return true;
        }

        // This ACL applies to a SID subset only. Iterate to check it applies.
        for (Sid sid : sids) {
            boolean found = false;

            for (Sid loadedSid : loadedSids) {
                if (sid.equals(loadedSid)) {
                    // this SID is OK
                    found = true;

                    break; // out of loadedSids for loop
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void setEntriesInheriting(boolean entriesInheriting) {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_GENERAL);
        this.entriesInheriting = entriesInheriting;
    }

    @Override
    public void setOwner(Sid newOwner) {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_OWNERSHIP);
        Assert.notNull(newOwner, "Owner required");
        this.owner = newOwner;
    }

    @Override
    public Sid getOwner() {
        return this.owner;
    }

    @Override
    public void setParent(Acl newParent) {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_GENERAL);
        Assert.isTrue(newParent == null || !newParent.equals(this),
                "Cannot be the parent of yourself");
        this.parentAcl = newParent;
    }

    @Override
    public Acl getParentAcl() {
        return parentAcl;
    }

    @Override
    public void updateAce(int aceIndex, Permission permission) throws NotFoundException {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_GENERAL);
        verifyAceIndexExists(aceIndex);

        synchronized (aces) {
            MongoAceImpl ace = (MongoAceImpl) aces.get(aceIndex);
            ace.setPermission(permission);
        }
    }

    @Override
    public void updateAuditing(int aceIndex, boolean auditSuccess, boolean auditFailure) {
        aclAuthorizationStrategy.securityCheck(this,
                AclAuthorizationStrategy.CHANGE_AUDITING);
        verifyAceIndexExists(aceIndex);

        synchronized (aces) {
            MongoAceImpl ace = (MongoAceImpl) aces.get(aceIndex);
            ace.setAuditSuccess(auditSuccess);
            ace.setAuditFailure(auditFailure);
        }
    }
}
