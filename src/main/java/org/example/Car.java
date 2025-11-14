package org.example;

public class Car {
    Engine engine = new Engine();

    public Car(Engine engine){
        this.engine = engine;
    }

    public String drive(){
        return engine.start();
    }

}
