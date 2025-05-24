package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.exceptions.RepeatedElementException;
import co.edu.uniquindio.cityguardian.mapping.dto.CommentDto;
import co.edu.uniquindio.cityguardian.mapping.dto.CreateReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.EditReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.FilterReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDto;
import co.edu.uniquindio.cityguardian.mapping.mappers.ReportMapper;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.repository.ReportRepository;
import co.edu.uniquindio.cityguardian.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository repository;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void createNewReport(CreateReportDto reportDto, List<String> imageUrls) throws Exception {
        Report report = reportMapper.toDocument(reportDto);
        repository.save(report);

    }

    @Override
    public ReportDto updateReport(EditReportDto updatedReport, String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()){
            throw new RuntimeException("Reporte no encontrado");
        }

        Report existingReport = optionalReport.get();
        objectMapper.updateValue(existingReport, updatedReport);
        return reportMapper.toReportDto(repository.save(existingReport));
    }

    @Override
    public void deleteReport(String id) throws Exception {
        Optional<Report> reportOptional = repository.findById(id);
        if (reportOptional.isEmpty()){
            throw  new RuntimeException("Reporte no encontrado");
        }
        repository.deleteById(id);
    }

    @Override
    public ReportDto getReportById(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()){
            throw new RuntimeException("Reporte no encontrado");
        }
        return reportMapper.toReportDto(optionalReport.get());
    }

    @Override
    public List<ReportDto> getReports() {
        List<Report> reports = repository.findAll();
        return reports.stream().map(reportMapper::toReportDto).toList();
    }

    @Override
    public void markReportAsSolved(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()){
            throw new RuntimeException("Reporte no encontrado");
        }
        Report report = optionalReport.get();
        report.setSolved(true);
        repository.save(report);
    }

    @Override
    public void markReportAsImportant(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()){
            throw new RuntimeException("Reporte no encontrado");
        }
        Report report = optionalReport.get();
        report.setImportant(true);
        repository.save(report);

    }

    @Override
    public List<ReportDto> filterReports(FilterReportDto filterReportDto) throws Exception {

        Query query = new Query();

        if (filterReportDto.initialDate() != null && !filterReportDto.initialDate().isEmpty()) {
            query.addCriteria(Criteria.where("date")
                    .gte(filterReportDto.initialDate()));
        }

        if (filterReportDto.finalDate() != null && !filterReportDto.finalDate().isEmpty()) {
            query.addCriteria(Criteria.where("date")
                    .lte(filterReportDto.finalDate()));
        }

        if (filterReportDto.status() != null && !filterReportDto.status().isEmpty()) {
            query.addCriteria(Criteria.where("status")
                    .is(filterReportDto.status()));
        }

        if (filterReportDto.category() != null && !filterReportDto.category().isEmpty()) {
            query.addCriteria(Criteria.where("category")
                    .is(filterReportDto.category()));
        }

        List<Report> reports = mongoTemplate.find(query, Report.class);

        return reports.stream().map(reportMapper::toReportDto).toList();
    }


    public boolean idExist(String id){
        return  repository.findById(id).isPresent();
    }

    public void addComment(CommentDto commentDto, String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()){
            throw new RepeatedElementException("No se puede agregar el comentario, por que no existe el reporte");
        }
        Report report = optionalReport.get();
        System.out.println("Comment: " + commentDto.description());
        if (report.getComments() == null) {
            List<String> comments = Collections.singletonList(commentDto.description());
            report.setComments(comments);
        } else {
            report.getComments().add(commentDto.description());
        }


        repository.save(report);
    }


}
