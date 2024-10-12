package com.br.web.v1.type;

public enum AnimalType {
    DOG(0),
    CAT(1);

    private final int value;

    private AnimalType(int i){
        this.value=i;
    }

    public static AnimalType getByValue(Long i) {
        for(AnimalType dt : AnimalType.values()) {
            if(dt.value == i) {
                return dt;
            }
        }
        throw new IllegalArgumentException("no datatype with " + i + " exists");
    }
}
