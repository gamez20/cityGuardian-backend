package co.edu.uniquindio.cityguardian.services;

import co.edu.uniquindio.cityguardian.mapping.dto.CommentDto;
import co.edu.uniquindio.cityguardian.mapping.dto.CreateReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.EditReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.FilterReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;

import java.util.List;

public interface ReportService {

    ReportDTO createNewReport(CreateReportDto reportDto, List<String> imageUrls) throws Exception;
    ReportDTO updateReport(EditReportDto updatedReport, String id) throws Exception;
    void deleteReport(String id) throws Exception;
    ReportDTO getReportById(String id) throws Exception;
    List<ReportDTO> getReports();
    void markReportAsSolved(String id) throws Exception;
    void markReportAsImportant(String id) throws Exception;
    List<ReportDTO> filterReports(FilterReportDto filterReportDto) throws Exception;
    void addComment(CommentDto commentDto, String id) throws Exception;
}
