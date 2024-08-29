// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class DesignatorFunc extends DesignatorOp {

    private ActualParsBegin ActualParsBegin;
    private ActualPars ActualPars;

    public DesignatorFunc (ActualParsBegin ActualParsBegin, ActualPars ActualPars) {
        this.ActualParsBegin=ActualParsBegin;
        if(ActualParsBegin!=null) ActualParsBegin.setParent(this);
        this.ActualPars=ActualPars;
        if(ActualPars!=null) ActualPars.setParent(this);
    }

    public ActualParsBegin getActualParsBegin() {
        return ActualParsBegin;
    }

    public void setActualParsBegin(ActualParsBegin ActualParsBegin) {
        this.ActualParsBegin=ActualParsBegin;
    }

    public ActualPars getActualPars() {
        return ActualPars;
    }

    public void setActualPars(ActualPars ActualPars) {
        this.ActualPars=ActualPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActualParsBegin!=null) ActualParsBegin.accept(visitor);
        if(ActualPars!=null) ActualPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActualParsBegin!=null) ActualParsBegin.traverseTopDown(visitor);
        if(ActualPars!=null) ActualPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActualParsBegin!=null) ActualParsBegin.traverseBottomUp(visitor);
        if(ActualPars!=null) ActualPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFunc(\n");

        if(ActualParsBegin!=null)
            buffer.append(ActualParsBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualPars!=null)
            buffer.append(ActualPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFunc]");
        return buffer.toString();
    }
}
