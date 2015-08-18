package com.dsoft.entity;

import javax.persistence.*;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "logo")
    private String logo;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_cell_no")
    private String agentCellNo;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_cell_no")
    private String companyCellNo;

    @Column(name = "company_email")
    private String companyEmail;

    @Transient
    private Boolean saved;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentCellNo() {
        return agentCellNo;
    }

    public void setAgentCellNo(String agentCellNo) {
        this.agentCellNo = agentCellNo;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyCellNo() {
        return companyCellNo;
    }

    public void setCompanyCellNo(String companyCellNo) {
        this.companyCellNo = companyCellNo;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentCellNo='" + agentCellNo + '\'' +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyCellNo='" + companyCellNo + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", saved=" + saved +
                '}';
    }
}
