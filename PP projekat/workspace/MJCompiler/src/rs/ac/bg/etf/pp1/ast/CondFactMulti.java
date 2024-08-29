// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class CondFactMulti extends CondFactorList {

    private CondFactor CondFactor;
    private CondFactorList CondFactorList;

    public CondFactMulti (CondFactor CondFactor, CondFactorList CondFactorList) {
        this.CondFactor=CondFactor;
        if(CondFactor!=null) CondFactor.setParent(this);
        this.CondFactorList=CondFactorList;
        if(CondFactorList!=null) CondFactorList.setParent(this);
    }

    public CondFactor getCondFactor() {
        return CondFactor;
    }

    public void setCondFactor(CondFactor CondFactor) {
        this.CondFactor=CondFactor;
    }

    public CondFactorList getCondFactorList() {
        return CondFactorList;
    }

    public void setCondFactorList(CondFactorList CondFactorList) {
        this.CondFactorList=CondFactorList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondFactor!=null) CondFactor.accept(visitor);
        if(CondFactorList!=null) CondFactorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFactor!=null) CondFactor.traverseTopDown(visitor);
        if(CondFactorList!=null) CondFactorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFactor!=null) CondFactor.traverseBottomUp(visitor);
        if(CondFactorList!=null) CondFactorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactMulti(\n");

        if(CondFactor!=null)
            buffer.append(CondFactor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFactorList!=null)
            buffer.append(CondFactorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactMulti]");
        return buffer.toString();
    }
}
