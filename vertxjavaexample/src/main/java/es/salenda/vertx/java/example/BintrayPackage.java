package es.salenda.vertx.java.example;

import io.vertx.core.json.JsonObject;

public class BintrayPackage {
    String name;
    boolean linked;

    BintrayPackage(String name, boolean linked) {
        this.name = name;
        this.linked = linked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }
}
