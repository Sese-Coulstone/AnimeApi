package com.aniflix.exceptions;

public class AnimeNotFoundException extends RuntimeException{
    public AnimeNotFoundException(String s) {
        super(s);
    }
}
