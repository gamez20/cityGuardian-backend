package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.exceptions.RepeatedElementException;
import co.edu.uniquindio.cityguardian.mapping.dto.CommentDto;
import co.edu.uniquindio.cityguardian.model.*;
import co.edu.uniquindio.cityguardian.model.dto.CreateReportRequest;
import co.edu.uniquindio.cityguardian.model.dto.LocationDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.EditReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.FilterReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.mapping.mappers.ReportMapper;
import co.edu.uniquindio.cityguardian.repository.CategoryRepository;
import co.edu.uniquindio.cityguardian.repository.ReportRepository;
import co.edu.uniquindio.cityguardian.repository.UserRepository;
import co.edu.uniquindio.cityguardian.services.ReportService;
import co.edu.uniquindio.cityguardian.utils.LocationUtils;
import co.edu.uniquindio.cityguardian.utils.TokenUtils;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.threeten.bp.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

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
    public ReportDTO createNewReport(CreateReportRequest reportDto, List<String> imageUrls) throws Exception {
        Category category = categoryRepository.findById(reportDto.categoryId())
                .orElseThrow(() -> new Exception("La categoría no existe"));
        String email = TokenUtils.getEmailFromToken();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        Report report = reportMapper.toDocument(reportDto);
        report.setCategory(category);
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
        Category category = categoryRepository.findById(updatedReport.categoryId())
                .orElseThrow(() -> new Exception("La categoría no existe"));

        // Actualizar campos básicos
        existingReport.setTitle(updatedReport.title());
        existingReport.setDescription(updatedReport.description());
        existingReport.setCategory(category);

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
    public void markReportAsNotImportant(String id) throws Exception {
        Optional<Report> optionalReport = repository.findById(id);
        if (optionalReport.isEmpty()) {
            throw new RuntimeException("Reporte no encontrado");
        }
        Report report = optionalReport.get();
        report.setImportant(false);
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

    @Override
    public void addComment(String message, String reportId) throws Exception {
        Report report = repository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("No existe el reporte"));

        String email = TokenUtils.getEmailFromToken();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        // Crear comentario usando el mapper
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setMessage(message);
        comment.setUserId(user.getId());
        comment.setFirstName(user.getName());
        comment.setLastName(user.getLastName());
        comment.setDate(LocalDateTime.now().toString());

        if (report.getComments() == null) {
            report.setComments(new ArrayList<>());
        }
        report.getComments().add(comment);
        repository.save(report);
    }

    public List<Report> getReportsByCategory(String categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public void markAsVerified(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        report.addStateChange(ReportStatus.VERIFIED, null);
        repository.save(report);
    }

    @Override
    public void markAsRejected(String id, String rejectReason) throws Exception {
        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de rechazo es obligatorio");
        }

        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        report.addStateChange(ReportStatus.REJECTED, rejectReason);
        repository.save(report);
    }

    @Override
    public void markAsResolved(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        report.addStateChange(ReportStatus.RESOLVED, null);
        repository.save(report);
    }

    @Override
    public void sendToReview(String id) throws Exception {
        Report report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        report.addStateChange(ReportStatus.CREATED, null);
        repository.save(report);
    }

    @Override
    public List<ReportDTO> findReportsNearLocation(LocationDTO location, double radiusInKm) throws Exception {
        try {
            double targetLat = Double.parseDouble(location.latitude());
            double targetLon = Double.parseDouble(location.longitude());

            List<Report> allReports = repository.findAll();

            return allReports.stream()
                .filter(report -> report.getLocation() != null)
                .filter(report -> {
                    try {
                        if (report.getLocation() == null) {
                            return  false;
                        }
                        double reportLat = Double.parseDouble(report.getLocation().getLatitude());
                        double reportLon = Double.parseDouble(report.getLocation().getLongitude());
                        
                        double distance = LocationUtils.calculateDistance(
                            targetLat, targetLon, reportLat, reportLon);
                        
                        return distance <= radiusInKm;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .map(reportMapper::toReportDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al buscar reportes cercanos: " + e.getMessage());
        }
    }
}
