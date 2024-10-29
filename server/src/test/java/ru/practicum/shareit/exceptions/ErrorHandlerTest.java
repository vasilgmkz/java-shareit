package ru.practicum.shareit.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorHandlerTest {

    private final ErrorHandler errorHandler;

    EntityNotFoundException entityNotFoundException;
    NotFoundException notFoundException;
    ConflictExceptions conflictExceptions;
    InternalServerException internalServerException;
    DataIntegrityViolationException dataIntegrityViolationException;

    @BeforeEach
    void setUp() {
        entityNotFoundException = new EntityNotFoundException("message");
        notFoundException = new NotFoundException("message");
        conflictExceptions = new ConflictExceptions("message");
        internalServerException = new InternalServerException("message");
        dataIntegrityViolationException = new DataIntegrityViolationException("message");
    }

    @Test
    @DisplayName("EntityNotFoundException")
    void testEntityNotFoundException() {
        ErrorResponse errorResponse = errorHandler.entityNotFoundException(entityNotFoundException);
        assertEquals(errorResponse.getError(), "message");
    }

    @Test
    @DisplayName("NotFoundException")
    void testNotFoundException() {
        ErrorResponse errorResponse = errorHandler.handleNotFoundError(notFoundException);
        assertEquals(errorResponse.getError(), "message");
    }

    @Test
    @DisplayName("ConflictExceptions")
    void testConflictExceptions() {
        ErrorResponse errorResponse = errorHandler.handleConflictError(conflictExceptions);
        assertEquals(errorResponse.getError(), "message");
    }

    @Test
    @DisplayName("InternalServerException")
    void testInternalServerException() {
        ErrorResponse errorResponse = errorHandler.internalServerException(internalServerException);
        assertEquals(errorResponse.getError(), "message");
    }

    @Test
    @DisplayName("DataIntegrityViolationException")
    void testDataIntegrityViolationException() {
        ErrorResponse errorResponse = errorHandler.dataIntegrityViolationException(dataIntegrityViolationException);
        assertEquals(errorResponse.getError(), "message");
    }
}
