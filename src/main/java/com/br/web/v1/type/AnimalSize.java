package com.br.web.v1.type;

public enum AnimalSize {
    SMALL(0),
    AVERAGE(1),
    BIG(2);

    private final int value;

    private AnimalSize(int i){
        this.value=i;
    }

    public static AnimalSize getByValue(Long i) {
        for(AnimalSize dt : AnimalSize.values()) {
            if(dt.value == i) {
                return dt;
            }
        }
        throw new IllegalArgumentException("no datatype with " + i + " exists");
    }
}
