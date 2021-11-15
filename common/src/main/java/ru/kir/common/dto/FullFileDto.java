package ru.kir.common.dto;

import lombok.Data;

@Data
public class FullFileDto {
    private String fileName;
    private long startPosition;
    private byte[] fileInBytes;
}
