// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class MethDeclarations extends MethodDeclList {

    private MethodDeclList MethodDeclList;
    private MethodDeclPart MethodDeclPart;

    public MethDeclarations (MethodDeclList MethodDeclList, MethodDeclPart MethodDeclPart) {
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
        this.MethodDeclPart=MethodDeclPart;
        if(MethodDeclPart!=null) MethodDeclPart.setParent(this);
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public MethodDeclPart getMethodDeclPart() {
        return MethodDeclPart;
    }

    public void setMethodDeclPart(MethodDeclPart MethodDeclPart) {
        this.MethodDeclPart=MethodDeclPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
        if(MethodDeclPart!=null) MethodDeclPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
        if(MethodDeclPart!=null) MethodDeclPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        if(MethodDeclPart!=null) MethodDeclPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethDeclarations(\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclPart!=null)
            buffer.append(MethodDeclPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethDeclarations]");
        return buffer.toString();
    }
}
