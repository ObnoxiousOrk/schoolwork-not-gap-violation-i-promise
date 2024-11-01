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

import java.util.* ;
import java.io.* ;

// This thread does all the work. The main thread sleeps until timeout, then
// wakes and kills this thread. 

public final class SRWorkThread extends Thread {
    public void run() {
        if(CmdFlags.getMode()==CmdFlags.ReadSolution || CmdFlags.getMode()==CmdFlags.ReadDomainStore) {
            MinionSolver min=new MinionSolver();
            if(CmdFlags.getMode()==CmdFlags.ReadSolution) {
                min.parseSolutionMode();
            }
            else {
                min.parseDomainStore();
            }
            return;
        }
        
        //  Read the files.
        EPrimeReader reader = new EPrimeReader(CmdFlags.eprimefile, true);
        Model m=reader.readModel() ;
        assert m.constraints != null;
        
        // Get the parameters
        ArrayList<ASTNode> parameters=new ArrayList<ASTNode>();
        if(CmdFlags.paramfile!=null) {
            EPrimeReader paramfile = new EPrimeReader(CmdFlags.paramfile, true);
            parameters=paramfile.readParameterFile(m);
        }
        if(CmdFlags.paramstring!=null) {
            EPrimeReader paramfile = new EPrimeReader(CmdFlags.paramstring, false);
            parameters=paramfile.readParameterFile(m);
        }
        
        ModelContainer mc=new ModelContainer(m, new ArrayList<ASTNode>(parameters));
        
        if(CmdFlags.getParamToJSON()) {
            paramToJSON(parameters);
            System.exit(0);
        }
        
        if(CmdFlags.dryruns) {
            //  Three dry runs.  Reset the clock after each one; if there is a timeout during a dryrun then SR will exit.
            //  For the timelimit, time starts again for each dryrun.
            
            ModelContainer mc2=mc.copy();
            mc2.dryrun();
            CmdFlags.startTime=System.currentTimeMillis();
            
            mc2=mc.copy();
            mc2.dryrun();
            CmdFlags.startTime=System.currentTimeMillis();
            
            mc2=mc.copy();
            mc2.dryrun();
            CmdFlags.startTime=System.currentTimeMillis();
        }
        
        CmdFlags.startTime=System.currentTimeMillis();
        
        if(CmdFlags.make_tab) {
            mc.makeTableScopes();
        }
        else {
            mc.process();
        }
        
        System.exit(0);  // This is needed otherwise the other thread (Main thread) will continue to 
        // sleep and SR will not exit when it has finished. 
    }
    
    // This should really be somewhere else -- dump JSON version of param file.
    public void paramToJSON(ArrayList<ASTNode> parameters) {
        try {
            BufferedWriter o=new BufferedWriter(new FileWriter(CmdFlags.paramfile+".json"));
            
            o.write("{\n");
            for(int i=0; i< parameters.size(); i++) {
                o.write("\"");
                o.write(parameters.get(i).getChild(0).toString());
                o.write("\" : ");
                ASTNode a=parameters.get(i).getChild(1);
                if(a instanceof BooleanConstant || a instanceof NumberConstant) {
                    o.write(a.toString());
                }
                else if(a instanceof EmptyMatrix) {
                    o.write("[ ]");
                }
                else {
                    o.write(((CompoundMatrix)a).toStringSimpleMatrix());
                }
                if(i<parameters.size()-1) {
                    o.write(",");
                }
                o.write("\n");
            }
            
            o.write("}\n");
            o.close();
        }
        catch(IOException blah) {
        }
    }
}

