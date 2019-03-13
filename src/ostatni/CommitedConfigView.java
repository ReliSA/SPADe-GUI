package ostatni;

import java.sql.Timestamp;

public class CommitedConfigView {
    private String workItemType;
    private String workItemName;
    private String workItemDescription;
    private Timestamp created;
    private Long authorId;
    private String authorName;
    private String authorRoleName;
    private String authorClass;
    private String authorSuperClass;
    private Long personId;
    private String personName;
    private String personRoleName;
    private String personRoleClass;
    private String personRoleSuperClass;
    private String confPerRelName;
    private Timestamp committed;
    private Long projectId;

    public CommitedConfigView(String workItemType, String workItemName, String workItemDescription, Timestamp created, Long authorId, String authorName, String authorRoleName, String authorClass, String authorSuperClass, Long personId, String personName, String personRoleName, String personRoleClass, String personRoleSuperClass, String confPerRelName, Timestamp committed, Long projectId) {
        this.workItemType = workItemType;
        this.workItemName = workItemName;
        this.workItemDescription = workItemDescription;
        this.created = created;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorRoleName = authorRoleName;
        this.authorClass = authorClass;
        this.authorSuperClass = authorSuperClass;
        this.personId = personId;
        this.personName = personName;
        this.personRoleName = personRoleName;
        this.personRoleClass = personRoleClass;
        this.personRoleSuperClass = personRoleSuperClass;
        this.confPerRelName = confPerRelName;
        this.committed = committed;
        this.projectId = projectId;
    }

    public String getWorkItemType() {
        return workItemType;
    }

    public void setWorkItemType(String workItemType) {
        this.workItemType = workItemType;
    }

    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    public String getWorkItemDescription() {
        return workItemDescription;
    }

    public void setWorkItemDescription(String workItemDescription) {
        this.workItemDescription = workItemDescription;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorRoleName() {
        return authorRoleName;
    }

    public void setAuthorRoleName(String authorRoleName) {
        this.authorRoleName = authorRoleName;
    }

    public String getAuthorClass() {
        return authorClass;
    }

    public void setAuthorClass(String authorClass) {
        this.authorClass = authorClass;
    }

    public String getAuthorSuperClass() {
        return authorSuperClass;
    }

    public void setAuthorSuperClass(String authorSuperClass) {
        this.authorSuperClass = authorSuperClass;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonRoleName() {
        return personRoleName;
    }

    public void setPersonRoleName(String personRoleName) {
        this.personRoleName = personRoleName;
    }

    public String getPersonRoleClass() {
        return personRoleClass;
    }

    public void setPersonRoleClass(String personRoleClass) {
        this.personRoleClass = personRoleClass;
    }

    public String getPersonRoleSuperClass() {
        return personRoleSuperClass;
    }

    public void setPersonRoleSuperClass(String personRoleSuperClass) {
        this.personRoleSuperClass = personRoleSuperClass;
    }

    public String getConfPerRelName() {
        return confPerRelName;
    }

    public void setConfPerRelName(String confPerRelName) {
        this.confPerRelName = confPerRelName;
    }

    public Timestamp getCommitted() {
        return committed;
    }

    public void setCommitted(Timestamp committed) {
        this.committed = committed;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}