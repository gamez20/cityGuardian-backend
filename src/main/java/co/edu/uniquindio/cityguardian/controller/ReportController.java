package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.*;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.model.dto.CreateReportRequest;
import co.edu.uniquindio.cityguardian.model.dto.LocationDTO;
import co.edu.uniquindio.cityguardian.services.ReportService;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ImagenService imagenService;

    @PostMapping("/filter")
    public List<ReportDTO> filterReports(@RequestBody FilterReportDto filterReportDto) throws Exception {
        return reportService.filterReports(filterReportDto);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDTO<String>> createNewReport(
            @Valid @RequestPart("report") CreateReportRequest reportDto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) throws Exception {
        try {
            List<String> imageUrls = new ArrayList<>();
            if (imagenes != null && !imagenes.isEmpty()) {
                List<ImagenDTO> imageDTOs = imagenService.subirImagenes(imagenes);
                imageUrls = imageDTOs.stream().map(ImagenDTO::getUrl).collect(Collectors.toList());
            }

            reportService.createNewReport(reportDto, imageUrls);
            return ResponseEntity.status(201).body(new MessageDTO<>(false, "Su reporte ha sido creado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO<EditReportDto>> editReport(
            @PathVariable String id,
            @Valid @RequestPart("report") EditReportDto reportDto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> nuevasImagenes) throws Exception {
        try {
            // Obtener el reporte actual para acceder a sus imágenes existentes
            ReportDTO reporteActual = reportService.getReportById(id);

            // Eliminar las imágenes antiguas que ya no están en el nuevo DTO
            if (reporteActual.imageUrls() != null) {
                for (String oldImageUrl : reporteActual.imageUrls()) {
                    if (reportDto.imageUrls() == null || !reportDto.imageUrls().contains(oldImageUrl)) {
                        imagenService.eliminarImagen(oldImageUrl);
                    }
                }
            }

            // Procesar nuevas imágenes si existen
            List<String> imageUrls = reportDto.imageUrls() != null ? new ArrayList<>(reportDto.imageUrls())
                    : new ArrayList<>();

            if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
                List<ImagenDTO> imageDTOs = imagenService.subirImagenes(nuevasImagenes);
                imageUrls.addAll(imageDTOs.stream()
                        .map(ImagenDTO::getUrl)
                        .collect(Collectors.toList()));
            }

            // Crear nuevo DTO con las URLs actualizadas
            EditReportDto reportDtoWithImages = new EditReportDto(
                    reportDto.title(),
                    reportDto.description(),
                    reportDto.categoryId(),
                    imageUrls);

            reportService.updateReport(reportDtoWithImages, id);
            return ResponseEntity.ok(new MessageDTO<>(false, reportDtoWithImages));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<EditReportDto>(true, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO<String>> deleteReport(@PathVariable String id) throws Exception {
        reportService.deleteReport(id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Reporte eliminado correctamente"));
    }

    @GetMapping("/{id}")
    public ReportDTO getReportById(@PathVariable String id) throws Exception {
        return reportService.getReportById(id);
    }

    @PutMapping("/{id}/solve")
    public ResponseEntity<MessageDTO<String>> markReportAsSolve(@PathVariable String id) throws Exception {
        reportService.markReportAsSolved(id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Reporte marcado como resuelto"));
    }

    @PutMapping("/{id}/important")
    public ResponseEntity<MessageDTO<String>> markReportAsImportant(@PathVariable String id) throws Exception {
        reportService.markReportAsImportant(id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Reporte marcado como importante"));
    }

    @PutMapping("/{id}/NotImportant")
    public ResponseEntity<MessageDTO<String>> markReportAsNotImportant(@PathVariable String id) throws Exception {
        reportService.markReportAsNotImportant(id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Reporte marcado como importante"));
    }


    @GetMapping
    public List<ReportDTO> getReports() {
        return reportService.getReports();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<MessageDTO<String>> addComment(
            @PathVariable String id,
            @Valid @RequestBody CreateCommentDto commentDto) throws Exception {
        try {
            reportService.addComment(commentDto.message(), id);
            return ResponseEntity.ok(new MessageDTO<>(false, "Comentario agregado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<MessageDTO<List<Report>>> getReportsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(new MessageDTO<>(false, reportService.getReportsByCategory(categoryId)));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<MessageDTO<String>> verifyReport(@PathVariable String id) {
        try {
            reportService.markAsVerified(id);
            return ResponseEntity.ok(new MessageDTO<>(false, "Reporte verificado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<MessageDTO<String>> rejectReport(
            @PathVariable String id,
            @RequestParam String rejectReason) {
        try {
            reportService.markAsRejected(id, rejectReason);
            return ResponseEntity.ok(new MessageDTO<>(false, "Reporte rechazado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<MessageDTO<String>> resolveReport(@PathVariable String id) {
        try {
            reportService.markAsResolved(id);
            return ResponseEntity.ok(new MessageDTO<>(false, "Reporte marcado como resuelto"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PatchMapping("/{id}/review")
    public ResponseEntity<MessageDTO<String>> sendToReview(@PathVariable String id) {
        try {
            reportService.sendToReview(id);
            return ResponseEntity.ok(new MessageDTO<>(false, "Reporte enviado a revisión"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PostMapping("/nearby")
    public ResponseEntity<MessageDTO<List<ReportDTO>>> findNearbyReports(
            @Valid @RequestBody LocationDTO location) {
        try {
            List<ReportDTO> nearbyReports = reportService.findReportsNearLocation(location, 5.0);
            return ResponseEntity.ok(new MessageDTO<>(false, nearbyReports));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, List.of()));
        }
    }
}
