package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.exceptions.RepeatedElementException;
import co.edu.uniquindio.cityguardian.mapping.dto.CommentDto;
import co.edu.uniquindio.cityguardian.mapping.dto.CreateReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.EditReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.FilterReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.mapping.mappers.ReportMapper;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.repository.CategoryRepository;
import co.edu.uniquindio.cityguardian.model.User;
import co.edu.uniquindio.cityguardian.repository.ReportRepository;
import co.edu.uniquindio.cityguardian.repository.UserRepository;
import co.edu.uniquindio.cityguardian.services.ReportService;
import co.edu.uniquindio.cityguardian.utils.TokenUtils;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import co.edu.uniquindio.cityguardian.model.Category;
import co.edu.uniquindio.cityguardian.model.ReportStatus;

import java.util.*;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository repository;

    @Autowired
    private ImagenService imagenService;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ReportDTO createNewReport(CreateReportDto reportDto, List<String> imageUrls) throws Exception {
        Category category = categoryRepository.findById(reportDto.categoryId())
                .orElseThrow(() -> new Exception("La categoría no existe"));
        String email = TokenUtils.getEmailFromToken();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        Report report = reportMapper.toDocument(reportDto);
        report.setUserId(user.getId());
        report.setImageUrls(imageUrls);
        Report savedReport = repository.save(report);

        user.addReportId(savedReport.getId());
        userRepository.save(user);

        return reportMapper.toReportDto(savedReport);
    }

    @Override
    public ReportDTO updateReport(EditReportDto updatedReport, String id) throws Exception {
        Report existingReport = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        // Validar que la categoría existe
        categoryRepository.findById(updatedReport.categoryId())
                .orElseThrow(() -> new Exception("La categoría no existe"));

        // Actualizar campos básicos
        existingReport.setTitle(updatedReport.title());
        existingReport.setDescription(updatedReport.description());
        existingReport.setCategoryId(updatedReport.categoryId());

        // Actualizar las URLs de las imágenes con la nueva lista
        existingReport.setImageUrls(updatedReport.imageUrls());

        Report savedReport = repository.save(existingReport);
        return reportMapper.toReportDto(savedReport);
    }

    @Override
    public void deleteReport(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        User user = userRepository.findById(report.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.removeReportId(id);
        userRepository.save(user);

        // Eliminar las imágenes asociadas
        if (report.getImageUrls() != null && !report.getImageUrls().isEmpty()) {
            for (String imageUrl : report.getImageUrls()) {
                try {
                    imagenService.eliminarImagen(imageUrl);
                } catch (Exception e) {
                    // Log error but continue with deletion
                    System.err.println("Error eliminando imagen: " + imageUrl + " - " + e.getMessage());
                }
            }
        }

        repository.deleteById(id);
    }

    @Override
    public ReportDTO getReportById(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()) {
            throw new RuntimeException("Reporte no encontrado");
        }
        return reportMapper.toReportDto(optionalReport.get());
    }

    @Override
    public List<ReportDTO> getReports() {
        List<Report> reports = repository.findAll();
        return reports.stream().map(reportMapper::toReportDto).toList();
    }

    @Override
    public void markReportAsSolved(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()) {
            throw new RuntimeException("Reporte no encontrado");
        }
        Report report = optionalReport.get();
        report.setSolved(true);
        repository.save(report);
    }

    @Override
    public void markReportAsImportant(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()) {
            throw new RuntimeException("Reporte no encontrado");
        }
        Report report = optionalReport.get();
        report.setImportant(true);
        repository.save(report);

    }

    @Override
    public List<ReportDTO> filterReports(FilterReportDto filterReportDto) throws Exception {

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

    public boolean idExist(String id) {
        return repository.findById(id).isPresent();
    }

    public void addComment(CommentDto commentDto, String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()) {
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

    public List<Report> getReportsByCategory(String categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public void markAsVerified(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        report.setStatus(ReportStatus.VERIFIED);
        report.setRejectReason(null);
        repository.save(report);
    }

    @Override
    public void markAsRejected(String id, String rejectReason) throws Exception {
        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de rechazo es obligatorio");
        }

        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        report.setStatus(ReportStatus.REJECTED);
        report.setRejectReason(rejectReason);
        repository.save(report);
    }

    @Override
    public void markAsResolved(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        report.setStatus(ReportStatus.RESOLVED);
        report.setRejectReason(null);
        repository.save(report);
    }

    @Override
    public void sendToReview(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        report.setStatus(ReportStatus.CREATED);
        report.setRejectReason(null);
        repository.save(report);
    }

}
