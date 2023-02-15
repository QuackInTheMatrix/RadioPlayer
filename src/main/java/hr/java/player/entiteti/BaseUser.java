package hr.java.player.entiteti;

import java.io.Serializable;

public record BaseUser(String username, Integer passwordHash) implements Serializable { }
