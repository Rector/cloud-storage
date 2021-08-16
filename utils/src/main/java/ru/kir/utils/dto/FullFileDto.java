package ru.kir.utils.dto;

import lombok.Data;

@Data
public class FullFileDto {
    private String fileName;
    private long startPosition;
    private byte[] fileInBytes;
}
