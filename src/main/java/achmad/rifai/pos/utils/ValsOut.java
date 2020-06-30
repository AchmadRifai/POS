/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos.utils;

/**
 *
 * @author AmMas
 * @param <T>
 */
public class ValsOut<T> {
    private T val;
    private int type;

    public ValsOut() {
    }

    public ValsOut(T val, int type) {
        this.val = val;
        this.type = type;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
