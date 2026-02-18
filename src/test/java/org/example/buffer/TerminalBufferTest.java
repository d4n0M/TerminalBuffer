package org.example.buffer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalBufferTest {

    @Test
    void canInstantiate() {
        TerminalBuffer buffer = new TerminalBuffer();
        assertNotNull(buffer);
    }
}