package savilerow;
/*

    Savile Row http://savilerow.cs.st-andrews.ac.uk/
    Copyright (C) 2014-2021 Peter Nightingale
    
    This file is part of Savile Row.
    
    Savile Row is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    Savile Row is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with Savile Row.  If not, see <http://www.gnu.org/licenses/>.

*/

import java.util.*;
import java.io.*;

public class Maximising extends ASTNodeC
{
    public static final long serialVersionUID = 1L;
    public Maximising(ASTNode r)
    {
        super(r);
    }
    
    public ASTNode copy()
    {
        assert numChildren()==1;
        return new Maximising(getChild(0));
    }
    
    @Override
    public boolean typecheck(SymbolTable st) {
        if(!getChild(0).typecheck(st)) return false;
        if(getChild(0).getDimension()>1) {
            CmdFlags.println("ERROR: Expected numerical expression or one-dimensional matrix in maximising, found a matrix: "+this);
	        return false;
        }
        if(getChild(0).getDimension()==1 && !(getChild(0) instanceof CompoundMatrix)) {
            CmdFlags.println("ERROR: Expected numerical expression or one-dimensional literal matrix in maximising, found a matrix: "+this);
	        return false;
        }
        return true;
    }
    
    public int polarity(int child) {
        return 1;   //  Puts a lower bound on the contained expression.
    }
    
    public void toMinion(BufferedWriter b, boolean bool_context) throws IOException {
        b.append("MAXIMISING ");
        getChild(0).toMinion(b, false);
        b.append("\n");
    }
    public String toString() {
        return "maximising "+getChild(0)+"\n";
    }
    public void toFlatzinc(BufferedWriter b, boolean bool_context) throws IOException {
        if(!getChild(0).isConstant()) {
            b.append("maximize ");
            getChild(0).toFlatzinc(b, false);
            b.append(";\n");
        }
        else {
            b.append("satisfy;\n");
        }
    }
    public void toMinizinc(StringBuilder b, boolean bool_context) {
        b.append("maximize ");
        getChild(0).toMinizinc(b, false);
        b.append(";\n");
    }
    public void toMIP(BufferedWriter b) throws IOException {
        b.append("Maximize\n");
        b.append("obj: ");
        getChild(0).toMIP(b);
        b.append("\n");
    }
    public void toSAT(Sat satModel) throws IOException {
        if(getChild(0) instanceof Identifier) {
            ArrayList<Intpair> a=getChild(0).getIntervalSetExp();
            for(int i=0; i<a.size(); i++) {
                Intpair p=a.get(i);
                for(long val=p.lower; val<=p.upper; val++) {
                    if(i<a.size()-1 || val<p.upper) {
                        satModel.addSoftClause(-getChild(0).orderEncode(satModel, val));
                    }
                }
            }
        }
        else {
            assert getChild(0) instanceof MaxSATObjective;
            getChild(0).toSAT(satModel);
        }
    }
}
