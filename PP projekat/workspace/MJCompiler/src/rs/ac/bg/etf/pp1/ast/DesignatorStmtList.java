// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class DesignatorStmtList extends DesignatorStatementList {

    private DesignatorStatementList DesignatorStatementList;
    private DesignatorStatementPart DesignatorStatementPart;

    public DesignatorStmtList (DesignatorStatementList DesignatorStatementList, DesignatorStatementPart DesignatorStatementPart) {
        this.DesignatorStatementList=DesignatorStatementList;
        if(DesignatorStatementList!=null) DesignatorStatementList.setParent(this);
        this.DesignatorStatementPart=DesignatorStatementPart;
        if(DesignatorStatementPart!=null) DesignatorStatementPart.setParent(this);
    }

    public DesignatorStatementList getDesignatorStatementList() {
        return DesignatorStatementList;
    }

    public void setDesignatorStatementList(DesignatorStatementList DesignatorStatementList) {
        this.DesignatorStatementList=DesignatorStatementList;
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
        if(DesignatorStatementList!=null) DesignatorStatementList.accept(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementList!=null) DesignatorStatementList.traverseTopDown(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementList!=null) DesignatorStatementList.traverseBottomUp(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtList(\n");

        if(DesignatorStatementList!=null)
            buffer.append(DesignatorStatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementPart!=null)
            buffer.append(DesignatorStatementPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtList]");
        return buffer.toString();
    }
}
