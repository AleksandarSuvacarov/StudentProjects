// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class DesignatorStmtSingle extends DesignatorStatementList {

    private DesignatorStatementPart DesignatorStatementPart;

    public DesignatorStmtSingle (DesignatorStatementPart DesignatorStatementPart) {
        this.DesignatorStatementPart=DesignatorStatementPart;
        if(DesignatorStatementPart!=null) DesignatorStatementPart.setParent(this);
    }

    public DesignatorStatementPart getDesignatorStatementPart() {
        return DesignatorStatementPart;
    }

    public void setDesignatorStatementPart(DesignatorStatementPart DesignatorStatementPart) {
        this.DesignatorStatementPart=DesignatorStatementPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStatementPart!=null) DesignatorStatementPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementPart!=null) DesignatorStatementPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtSingle(\n");

        if(DesignatorStatementPart!=null)
            buffer.append(DesignatorStatementPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtSingle]");
        return buffer.toString();
    }
}
