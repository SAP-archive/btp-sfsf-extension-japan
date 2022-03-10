package com.sap.sfsf.reshuffle.applicants.backend.model;

import java.util.List;

import lombok.Data;

@Data
public class FileExportModel {
    private String filename;
    private List<FileExportEntityModel> entities;
}
