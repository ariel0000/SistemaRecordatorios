/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;


public class ComboItem {
    
    private String key;
    private String value;

    public ComboItem(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String valor){
        this.value = valor;
    }

    public boolean equals(int id){
        String idCI = String.valueOf(id);
        return idCI.equals(this.key);
    }
    
}