package org.example;

public class Car {
    Engine engine;
    public Car(Engine engine){
        this.engine = engine;
    }
    public String drive(){
        return engine.start();
    }
}
