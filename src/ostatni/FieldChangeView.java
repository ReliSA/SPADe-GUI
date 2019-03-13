package ostatni;

public class FieldChangeView {
    private String fieldChangeName;
    private Long fieldChangeId;
    private String newValue;
    private String oldValue;
    private Long changedWorkItemId;
    private String workItemChangeName;
    private String workItemChangeDesc;
    private Long personId;
    private String personName;
    private String roleName;
    private String roleClass;
    private String roleSuperClass;

    public FieldChangeView(String fieldChangeName, Long fieldChangeId, String newValue, String oldValue, Long changedWorkItemId, String workItemChangeName, String workItemChangeDesc, Long personId, String personName, String roleName, String roleClass, String roleSuperClass) {
        this.fieldChangeName = fieldChangeName;
        this.fieldChangeId = fieldChangeId;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.changedWorkItemId = changedWorkItemId;
        this.workItemChangeName = workItemChangeName;
        this.workItemChangeDesc = workItemChangeDesc;
        this.personId = personId;
        this.personName = personName;
        this.roleName = roleName;
        this.roleClass = roleClass;
        this.roleSuperClass = roleSuperClass;
    }

    public String getFieldChangeName() {
        return fieldChangeName;
    }

    public void setFieldChangeName(String fieldChangeName) {
        this.fieldChangeName = fieldChangeName;
    }

    public Long getFieldChangeId() {
        return fieldChangeId;
    }

    public void setFieldChangeId(Long fieldChangeId) {
        this.fieldChangeId = fieldChangeId;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public Long getChangedWorkItemId() {
        return changedWorkItemId;
    }

    public void setChangedWorkItemId(Long changedWorkItemId) {
        this.changedWorkItemId = changedWorkItemId;
    }

    public String getWorkItemChangeName() {
        return workItemChangeName;
    }

    public void setWorkItemChangeName(String workItemChangeName) {
        this.workItemChangeName = workItemChangeName;
    }

    public String getWorkItemChangeDesc() {
        return workItemChangeDesc;
    }

    public void setWorkItemChangeDesc(String workItemChangeDesc) {
        this.workItemChangeDesc = workItemChangeDesc;
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