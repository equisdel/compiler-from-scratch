package TercerEtapa;

import java.util.*;
public class TagTable {
    HashMap<String, HashMap<String,GotoPair>> tabla = new HashMap<>();

    public TagTable() {
        
    }

    public void addTag(String tag) {
        if (!tabla.containsKey(tag)) {
            tabla.put(tag, new HashMap<>());
        }
    }

    public void addGoto(String tag, GotoPair Terceto, String scope){
        addTag(tag);
        if (tabla.get(tag).get(Terceto))
        tabla.get(tag).put(Terceto,scope);

    }
}

