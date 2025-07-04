package com.aniflix.exceptions;

public class FileExistsException extends RuntimeException {
    public FileExistsException(String s) {
        super(s);
    }
}
