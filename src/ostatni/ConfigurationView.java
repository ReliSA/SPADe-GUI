package ostatni;

import java.sql.Timestamp;

public class ConfigurationView {
    private String workItemType;
    private String workItemName;
    private String workItemDescription;
    private Timestamp created;
    private Long personId;
    private String personName;
    private String roleName;
    private String roleClass;
    private String roleSuperClass;
    private String confPerRelName;
    private Long projectId;

    public ConfigurationView(String workItemType, String workItemName, String workItemDescription, Timestamp created, Long personId, String personName, String roleName, String roleClass, String roleSuperClass, String confPerRelName, Long projectId) {
        this.workItemType = workItemType;
        this.workItemName = workItemName;
        this.workItemDescription = workItemDescription;
        this.created = created;
        this.personId = personId;
        this.personName = personName;
        this.roleName = roleName;
        this.roleClass = roleClass;
        this.roleSuperClass = roleSuperClass;
        this.confPerRelName = confPerRelName;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}