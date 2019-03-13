package ostatni;

import java.sql.Timestamp;
import java.util.Date;

public class WorkUnitView {
    private Long personId;
    private String personName;
    private String roleName;
    private String roleClass;
    private String roleSuperClass;
    private Long assigneeId;
    private String assigneeName;
    private String assigneeRoleName;
    private String assigneeRoleClass;
    private String assigneeRoleSuperClass;
    private Date dueDate;
    private Double estimatedTime;
    private Integer progress;
    private Double spentTime;
    private Date startDate;
    private String activityName;
    private String activityDesc;
    private Date activityStartDate;
    private Date activityEndDate;
    private String iterationName;
    private String iterationDesc;
    private Date iterationStartDate;
    private Date iterationEndDate;
    private Timestamp iterationCreated;
    private String phaseName;
    private String phaseDesc;
    private Date phaseStartDate;
    private Date phaseEndDate;
    private Timestamp phaseCreated;
    private String priorityName;
    private String priorityDesc;
    private String priorityClass;
    private String prioritySuperClass;
    private String severityName;
    private String severityDesc;
    private String severityClass;
    private String severitySuperClass;
    private String resolutionName;
    private String resolutionDesc;
    private String resolutionClass;
    private String resolutionSuperClass;
    private String statusName;
    private String statusDesc;
    private String statusClass;
    private String statusSuperClass;
    private String wuTypeName;
    private String wuTypeDesc;
    private String wuTypeClass;
    private String categoryName;
    private String categoryDesc;

    public WorkUnitView(Long personId, String personName, String roleName, String roleClass, String roleSuperClass, Long assigneeId, String assigneeName, String assigneeRoleName, String assigneeRoleClass, String assigneeRoleSuperClass, Date dueDate, Double estimatedTime, Integer progress, Double spentTime, Date startDate, String activityName, String activityDesc, Date activityStartDate, Date activityEndDate, String iterationName, String iterationDesc, Date iterationStartDate, Date iterationEndDate, Timestamp iterationCreated, String phaseName, String phaseDesc, Date phaseStartDate, Date phaseEndDate, Timestamp phaseCreated, String priorityName, String priorityDesc, String priorityClass, String prioritySuperClass, String severityName, String severityDesc, String severityClass, String severitySuperClass, String resolutionName, String resolutionDesc, String resolutionClass, String resolutionSuperClass, String statusName, String statusDesc, String statusClass, String statusSuperClass, String wuTypeName, String wuTypeDesc, String wuTypeClass, String categoryName, String categoryDesc) {
        this.personId = personId;
        this.personName = personName;
        this.roleName = roleName;
        this.roleClass = roleClass;
        this.roleSuperClass = roleSuperClass;
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
        this.assigneeRoleName = assigneeRoleName;
        this.assigneeRoleClass = assigneeRoleClass;
        this.assigneeRoleSuperClass = assigneeRoleSuperClass;
        this.dueDate = dueDate;
        this.estimatedTime = estimatedTime;
        this.progress = progress;
        this.spentTime = spentTime;
        this.startDate = startDate;
        this.activityName = activityName;
        this.activityDesc = activityDesc;
        this.activityStartDate = activityStartDate;
        this.activityEndDate = activityEndDate;
        this.iterationName = iterationName;
        this.iterationDesc = iterationDesc;
        this.iterationStartDate = iterationStartDate;
        this.iterationEndDate = iterationEndDate;
        this.iterationCreated = iterationCreated;
        this.phaseName = phaseName;
        this.phaseDesc = phaseDesc;
        this.phaseStartDate = phaseStartDate;
        this.phaseEndDate = phaseEndDate;
        this.phaseCreated = phaseCreated;
        this.priorityName = priorityName;
        this.priorityDesc = priorityDesc;
        this.priorityClass = priorityClass;
        this.prioritySuperClass = prioritySuperClass;
        this.severityName = severityName;
        this.severityDesc = severityDesc;
        this.severityClass = severityClass;
        this.severitySuperClass = severitySuperClass;
        this.resolutionName = resolutionName;
        this.resolutionDesc = resolutionDesc;
        this.resolutionClass = resolutionClass;
        this.resolutionSuperClass = resolutionSuperClass;
        this.statusName = statusName;
        this.statusDesc = statusDesc;
        this.statusClass = statusClass;
        this.statusSuperClass = statusSuperClass;
        this.wuTypeName = wuTypeName;
        this.wuTypeDesc = wuTypeDesc;
        this.wuTypeClass = wuTypeClass;
        this.categoryName = categoryName;
        this.categoryDesc = categoryDesc;
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

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeRoleName() {
        return assigneeRoleName;
    }

    public void setAssigneeRoleName(String assigneeRoleName) {
        this.assigneeRoleName = assigneeRoleName;
    }

    public String getAssigneeRoleClass() {
        return assigneeRoleClass;
    }

    public void setAssigneeRoleClass(String assigneeRoleClass) {
        this.assigneeRoleClass = assigneeRoleClass;
    }

    public String getAssigneeRoleSuperClass() {
        return assigneeRoleSuperClass;
    }

    public void setAssigneeRoleSuperClass(String assigneeRoleSuperClass) {
        this.assigneeRoleSuperClass = assigneeRoleSuperClass;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Double getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(Double spentTime) {
        this.spentTime = spentTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public Date getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(Date activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public Date getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(Date activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public String getIterationDesc() {
        return iterationDesc;
    }

    public void setIterationDesc(String iterationDesc) {
        this.iterationDesc = iterationDesc;
    }

    public Date getIterationStartDate() {
        return iterationStartDate;
    }

    public void setIterationStartDate(Date iterationStartDate) {
        this.iterationStartDate = iterationStartDate;
    }

    public Date getIterationEndDate() {
        return iterationEndDate;
    }

    public void setIterationEndDate(Date iterationEndDate) {
        this.iterationEndDate = iterationEndDate;
    }

    public Timestamp getIterationCreated() {
        return iterationCreated;
    }

    public void setIterationCreated(Timestamp iterationCreated) {
        this.iterationCreated = iterationCreated;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPhaseDesc() {
        return phaseDesc;
    }

    public void setPhaseDesc(String phaseDesc) {
        this.phaseDesc = phaseDesc;
    }

    public Date getPhaseStartDate() {
        return phaseStartDate;
    }

    public void setPhaseStartDate(Date phaseStartDate) {
        this.phaseStartDate = phaseStartDate;
    }

    public Date getPhaseEndDate() {
        return phaseEndDate;
    }

    public void setPhaseEndDate(Date phaseEndDate) {
        this.phaseEndDate = phaseEndDate;
    }

    public Timestamp getPhaseCreated() {
        return phaseCreated;
    }

    public void setPhaseCreated(Timestamp phaseCreated) {
        this.phaseCreated = phaseCreated;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public void setPriorityDesc(String priorityDesc) {
        this.priorityDesc = priorityDesc;
    }

    public String getPriorityClass() {
        return priorityClass;
    }

    public void setPriorityClass(String priorityClass) {
        this.priorityClass = priorityClass;
    }

    public String getPrioritySuperClass() {
        return prioritySuperClass;
    }

    public void setPrioritySuperClass(String prioritySuperClass) {
        this.prioritySuperClass = prioritySuperClass;
    }

    public String getSeverityName() {
        return severityName;
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
    }

    public String getSeverityDesc() {
        return severityDesc;
    }

    public void setSeverityDesc(String severityDesc) {
        this.severityDesc = severityDesc;
    }

    public String getSeverityClass() {
        return severityClass;
    }

    public void setSeverityClass(String severityClass) {
        this.severityClass = severityClass;
    }

    public String getSeveritySuperClass() {
        return severitySuperClass;
    }

    public void setSeveritySuperClass(String severitySuperClass) {
        this.severitySuperClass = severitySuperClass;
    }

    public String getResolutionName() {
        return resolutionName;
    }

    public void setResolutionName(String resolutionName) {
        this.resolutionName = resolutionName;
    }

    public String getResolutionDesc() {
        return resolutionDesc;
    }

    public void setResolutionDesc(String resolutionDesc) {
        this.resolutionDesc = resolutionDesc;
    }

    public String getResolutionClass() {
        return resolutionClass;
    }

    public void setResolutionClass(String resolutionClass) {
        this.resolutionClass = resolutionClass;
    }

    public String getResolutionSuperClass() {
        return resolutionSuperClass;
    }

    public void setResolutionSuperClass(String resolutionSuperClass) {
        this.resolutionSuperClass = resolutionSuperClass;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusClass() {
        return statusClass;
    }

    public void setStatusClass(String statusClass) {
        this.statusClass = statusClass;
    }

    public String getStatusSuperClass() {
        return statusSuperClass;
    }

    public void setStatusSuperClass(String statusSuperClass) {
        this.statusSuperClass = statusSuperClass;
    }

    public String getWuTypeName() {
        return wuTypeName;
    }

    public void setWuTypeName(String wuTypeName) {
        this.wuTypeName = wuTypeName;
    }

    public String getWuTypeDesc() {
        return wuTypeDesc;
    }

    public void setWuTypeDesc(String wuTypeDesc) {
        this.wuTypeDesc = wuTypeDesc;
    }

    public String getWuTypeClass() {
        return wuTypeClass;
    }

    public void setWuTypeClass(String wuTypeClass) {
        this.wuTypeClass = wuTypeClass;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }
}