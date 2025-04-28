/*
 * Copyright (c) 2025.
 * Xiang Chen xchenht@connect.ust.hk
 * This project is developed only for HKUST COMP3021 Programming Assignment
 */

package hk.ust.cse.comp3021;

import hk.ust.cse.comp3021.annotation.JsonCheck;
import hk.ust.cse.comp3021.annotation.JsonFilter;
import hk.ust.cse.comp3021.client.GPT4oClient;
import hk.ust.cse.comp3021.exception.JsonCheckException;
import hk.ust.cse.comp3021.exception.JsonFilterException;
import hk.ust.cse.comp3021.exception.JsonRangeCheckException;
import hk.ust.cse.comp3021.exception.PersistenceException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class Task2PrivateTest {
    static File resourceDir;

    static JSONObject getJsonResource(String resourceName) {
        return Utils.parseJSON(new File(resourceDir, resourceName).getPath());
    }

    static void runSession(JSONObject session) throws PersistenceException {
        ChatClient client = new GPT4oClient(session);
        String response = client.query("Who did I asked about?");
        assertTrue(response.toLowerCase().contains("charles"));
    }

    @BeforeAll
    static void setUp() {
        try {
            Path sessions = Path.of("sessions");
            if (!Files.exists(sessions)) {
                Files.createDirectory(sessions);
            }
        } catch (IOException e) {
            Utils.printlnError("Failed to create the session directory: " + e.getMessage());
        }
        resourceDir = new File("../src/test/resources");
        assertTrue(Files.exists(Path.of("./GPT-4o.txt")));
    }

    @Test
    void testMissingFields() {
        JSONObject session = getJsonResource("PrivateTest1");
        assertNotNull(session);
        assertThrows(Exception.class, () -> runSession(session));
    }

    @Test
    void testWrongFields() {
        JSONObject session = getJsonResource("PrivateTest2");
        assertNotNull(session);
        assertThrows(Exception.class, () -> runSession(session));
    }

    @Test
    void testExtraFields() throws PersistenceException {
        JSONObject session = getJsonResource("PrivateTest3");
        assertNotNull(session);
        runSession(session);
    }

    @Test
    void testAddJsonFilter() {
        JSONObject session = getJsonResource("PrivateTest4");
        assertNotNull(session);
        assertThrows(JsonFilterException.class, () -> runSession(session));
    }

    @Test
    void testAddJsonIgnore() throws PersistenceException {
        JSONObject session = getJsonResource("Base");
        assertNotNull(session);
        ChatClient client = new GPT4oClient(session);
        assertFalse(client.toJSON().has("temperature"));
    }

    @Test
    void testAddJsonCheck1() {
        JSONObject session = getJsonResource("PrivateTest5");
        assertNotNull(session);
        assertThrows(JsonCheckException.class, () -> runSession(session));
    }

    @Test
    void testAddJsonCheck2() throws PersistenceException {
        JSONObject session = getJsonResource("Base");
        assertNotNull(session);
        ChatClient client = new GPT4oClient(session);
        assertEquals(1, client.toJSON().getInt("unitPromptPrice"));
    }

    @Test
    void testJsonRangeCheck() {
        JSONObject session = getJsonResource("PrivateTest6");
        assertNotNull(session);
        assertThrows(JsonRangeCheckException.class, () -> runSession(session));
    }
}
