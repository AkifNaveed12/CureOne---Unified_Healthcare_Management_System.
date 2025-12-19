package com.cureone.patientsandrecords.model;

//Made By Hamza Ali FA24-BSE-124

public class MedicalRecord {

    private int recordID;
    private int patientID;
    private String diagnosis;
    private String treatment;
    private String doctorName;
    private String recordDate;

    public MedicalRecord() {}

    public MedicalRecord(int recordID, int patientID, String diagnosis,
                         String treatment, String doctorName, String recordDate) {

        this.recordID = recordID;
        this.patientID = patientID;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.doctorName = doctorName;
        this.recordDate = recordDate;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "\nRecord ID: " + recordID +
                "\nPatient ID: " + patientID +
                "\nDiagnosis: " + diagnosis +
                "\nTreatment: " + treatment +
                "\nDoctor Name: " + doctorName +
                "\nRecord Date: " + recordDate;
    }
}

