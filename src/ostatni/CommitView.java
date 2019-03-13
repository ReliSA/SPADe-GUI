package ostatni;

import java.sql.Timestamp;

public class CommitView {
    private String workItemType;
    private String workItemDescription;
    private Timestamp created;
    private String url;
    private Long personId;
    private String personName;
    private String roleName;
    private String roleClass;
    private String roleSuperClass;
    private String confPerRelName;
    private String confPerRelDesc;
    private Timestamp committed;
    private Boolean isRelease;
    private String tagName;
    private String branchName;
    private Boolean isMain;
    private Long projectId;

    public CommitView(String workItemType, String workItemDescription, Timestamp created, String url, Long personId, String personName, String roleName, String roleClass, String roleSuperClass, String confPerRelName, String confPerRelDesc, Timestamp committed, Boolean isRelease, String tagName, String branchName, Boolean isMain, Long projectId) {
        this.workItemType = workItemType;
        this.workItemDescription = workItemDescription;
        this.created = created;
        this.url = url;
        this.personId = personId;
        this.personName = personName;
        this.roleName = roleName;
        this.roleClass = roleClass;
        this.roleSuperClass = roleSuperClass;
        this.confPerRelName = confPerRelName;
        this.confPerRelDesc = confPerRelDesc;
        this.committed = committed;
        this.isRelease = isRelease;
        this.tagName = tagName;
        this.branchName = branchName;
        this.isMain = isMain;
        this.projectId = projectId;
    }

    public String getWorkItemType() {
        return workItemType;
    }

    public void setWorkItemType(String workItemType) {
        this.workItemType = workItemType;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleClass() {
        return roleClass;
    }

    public void setRoleClass(String roleClass) {
        this.roleClass = roleClass;
    }

    public String getRoleSuperClass() {
        return roleSuperClass;
    }

    public void setRoleSuperClass(String roleSuperClass) {
        this.roleSuperClass = roleSuperClass;
    }

    public String getConfPerRelName() {
        return confPerRelName;
    }

    public void setConfPerRelName(String confPerRelName) {
        this.confPerRelName = confPerRelName;
    }

    public String getConfPerRelDesc() {
        return confPerRelDesc;
    }

    public void setConfPerRelDesc(String confPerRelDesc) {
        this.confPerRelDesc = confPerRelDesc;
    }

    public Timestamp getCommitted() {
        return committed;
    }

    public void setCommitted(Timestamp committed) {
        this.committed = committed;
    }

    public Boolean getRelease() {
        return isRelease;
    }

    public void setRelease(Boolean release) {
        isRelease = release;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}