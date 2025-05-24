package co.edu.uniquindio.cityguardian.controller;


import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.*;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.services.ReportService;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ReportDto> filterReports(@RequestBody  FilterReportDto filterReportDto) throws Exception{
        return reportService.filterReports(filterReportDto);
    }

    @PostMapping("/create")
    public ResponseEntity<MessageDTO<String>> createNewReport(
            @Valid @RequestPart("report") CreateReportDto reportDto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) throws Exception {
        
        List<String> imageUrls = new ArrayList<>();
        if (imagenes != null && !imagenes.isEmpty()) {
            List<ImagenDTO> imageDTOs = imagenService.subirImagenes(imagenes);
            imageUrls = imageDTOs.stream().map(ImagenDTO::getUrl).collect(Collectors.toList());
        }
        
        reportService.createNewReport(reportDto, imageUrls);
        return ResponseEntity.status(201).body(new MessageDTO<>(false, "Su reporte ha sido creado exitosamente"));
    }

    @PutMapping("/{id}")
    public ReportDto editReport(@Valid @RequestBody EditReportDto reportDto, @PathVariable String id) throws Exception {
        return reportService.updateReport(reportDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO<String>> deleteReport(@PathVariable String id) throws Exception {
        reportService.deleteReport(id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Reporte eliminado correctamente"));
    }

    @GetMapping("/{id}")
    public ReportDto getReportById(@PathVariable String id) throws Exception {
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

    @GetMapping
    public List<ReportDto> getReports(){
        return reportService.getReports();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<MessageDTO<String>> addComment(@Valid @RequestBody CommentDto commentDto, @PathVariable String id) throws Exception {
        reportService.addComment(commentDto, id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Comentario agregado correctamente"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<MessageDTO<List<Report>>> getReportsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(new MessageDTO<>(false, reportService.getReportsByCategory(categoryId)));
    }
}
