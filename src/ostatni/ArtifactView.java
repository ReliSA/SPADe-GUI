package ostatni;

import java.sql.Timestamp;

public class ArtifactView {
    private String workItemName;
    private String description;
    private Timestamp created;
    private String url;
    private String artifactClass;
    private String mimeType;
    private Long size;
    private Long personId;
    private String personName;
    private String roleName;
    private String roleClass;
    private String roleSuperClass;

    public ArtifactView(String workItemName, String description, Timestamp created, String url, String artifactClass, String mimeType, Long size, Long personId, String personName, String roleName, String roleClass, String roleSuperClass) {
        this.workItemName = workItemName;
        this.description = description;
        this.created = created;
        this.url = url;
        this.artifactClass = artifactClass;
        this.mimeType = mimeType;
        this.size = size;
        this.personId = personId;
        this.personName = personName;
        this.roleName = roleName;
        this.roleClass = roleClass;
        this.roleSuperClass = roleSuperClass;
    }

    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getArtifactClass() {
        return artifactClass;
    }

    public void setArtifactClass(String artifactClass) {
        this.artifactClass = artifactClass;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
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
}