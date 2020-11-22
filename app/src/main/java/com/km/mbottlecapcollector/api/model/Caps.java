package com.km.mbottlecapcollector.api.model;

import java.util.List;

public class Caps {
    private List<Cap> Caps;

    public Caps(List<com.km.mbottlecapcollector.api.model.Cap> caps) {
        Caps = caps;
    }

    public List<com.km.mbottlecapcollector.api.model.Cap> getCap() {
        return Caps;
    }

    public void setCap(List<com.km.mbottlecapcollector.api.model.Cap> caps) {
        Caps = caps;
    }
}
