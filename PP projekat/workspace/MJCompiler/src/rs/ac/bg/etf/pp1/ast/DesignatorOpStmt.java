// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class DesignatorOpStmt extends DesignatorStatement {

    private Designator Designator;
    private DesignatorOp DesignatorOp;

    public DesignatorOpStmt (Designator Designator, DesignatorOp DesignatorOp) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorOp=DesignatorOp;
        if(DesignatorOp!=null) DesignatorOp.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorOp getDesignatorOp() {
        return DesignatorOp;
    }

    public void setDesignatorOp(DesignatorOp DesignatorOp) {
        this.DesignatorOp=DesignatorOp;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorOp!=null) DesignatorOp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorOp!=null) DesignatorOp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorOp!=null) DesignatorOp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorOpStmt(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorOp!=null)
            buffer.append(DesignatorOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorOpStmt]");
        return buffer.toString();
    }
}
