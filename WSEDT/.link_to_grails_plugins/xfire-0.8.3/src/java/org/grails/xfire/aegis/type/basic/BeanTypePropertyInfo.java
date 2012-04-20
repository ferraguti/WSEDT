package org.grails.xfire.aegis.type.basic;

class BeanTypePropertyInfo
{
    private boolean nillable = false;

    private int minOccurs = 1;

    public int getMinOccurs()
    {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs)
    {
        this.minOccurs = minOccurs;
    }

    public boolean isNillable()
    {
        return nillable;
    }

    public void setNillable(boolean nillable)
    {
        this.nillable = nillable;
    }
}
