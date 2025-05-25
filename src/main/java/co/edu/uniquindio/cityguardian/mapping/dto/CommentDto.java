package co.edu.uniquindio.cityguardian.mapping.dto;

public record CommentDto(
        String id,
        String userId,
        String firstName,
        String lastName,
        String date,
        String message
) {}
