package ru.otus.library.security.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.model.Genre;

@Service
@RequiredArgsConstructor
@Slf4j
public class AclEditService {

    private final MutableAclService aclService;

    private final GrantedAuthoritySid ROLE_ADMIN = new GrantedAuthoritySid("ROLE_ADMIN");
    private final GrantedAuthoritySid ROLE_USER = new GrantedAuthoritySid("ROLE_USER");

    public Book createAclForBook(Book book) {
        log.info("Create ACL for book {}", book);
        createDefaultAcl(new ObjectIdentityImpl(book));
        return book;
    }

    public Book deleteAclForBook(Book book) {
        log.info("Delete ACL for book {}", book);
        deleteAcl(new ObjectIdentityImpl(book));
        return book;
    }

    public Author createAclForAuthor(Author author) {
        log.info("Create ACL for author {}", author);
        createDefaultAcl(new ObjectIdentityImpl(author));
        return author;
    }

    public Author deleteAclForAuthor(Author author) {
        log.info("Delete ACL for Author {}", author);
        deleteAcl(new ObjectIdentityImpl(author));
        return author;
    }

    public Genre createAclForGenre(Genre genre) {
        log.info("Create ACL for genre {}", genre);
        createDefaultAcl(new ObjectIdentityImpl(genre));
        return genre;
    }

    public Genre deleteAclForGenre(Genre genre) {
        log.info("Delete ACL for Genre {}", genre);
        deleteAcl(new ObjectIdentityImpl(genre));
        return genre;
    }

    public Comment createAclForComment(Comment comment) {
        log.info("Create ACL for comment {}", comment);
        createDefaultAcl(new ObjectIdentityImpl(comment));
        return comment;
    }

    public Comment deleteAclForComment(Comment comment) {
        log.info("Delete ACL for comment {}", comment);
        deleteAcl(new ObjectIdentityImpl(comment));
        return comment;
    }

    private void deleteAcl(ObjectIdentity oid) {
        aclService.deleteAcl(oid, false);
    }

    public void createDefaultAcl(ObjectIdentity oid) {
        MutableAcl acl = null;
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
