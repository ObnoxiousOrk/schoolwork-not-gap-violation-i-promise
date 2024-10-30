package savilerow;
/*

    Savile Row http://savilerow.cs.st-andrews.ac.uk/
    Copyright (C) 2014-2021 Peter Nightingale and Luke Ryan
    
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

public class TransformDecomposeCumulative extends TreeTransformerBottomUpNoWrapper
{
    private boolean onlyDecomposeReified;
    private static boolean taskDecomp=true;
    
    public TransformDecomposeCumulative(boolean _onlyDecomposeReified) { 
        super(null);
        onlyDecomposeReified = _onlyDecomposeReified;
    }
    
    protected NodeReplacement processNode(ASTNode curnode)
	{
        if(curnode instanceof Cumulative) {
            
            if (onlyDecomposeReified && curnode.getParent().inTopAnd()) {
                return null;
            }
            
            ASTNode starts = curnode.getChildConst(0);
            ASTNode durations = curnode.getChildConst(1);
            ASTNode resourceReqs = curnode.getChildConst(2);
            ASTNode bound = curnode.getChild(3);
            
            assert starts instanceof CompoundMatrix;
            assert durations instanceof CompoundMatrix;
            assert resourceReqs instanceof CompoundMatrix;
            
            ArrayList<ASTNode> ct = new ArrayList<>();
            
            if(taskDecomp) {
                ct.add(taskDecomp(starts, durations, resourceReqs, bound));
            }
            else {
                ct.add(timeDecomp(starts, durations, resourceReqs, bound));
            }
            
            return new NodeReplacement(new And(ct));
        }
        return null;
    }
    
    private static ASTNode timeDecomp(ASTNode starts, ASTNode durations, ASTNode resourceReqs, ASTNode bound) {
        ArrayList<ASTNode> ct=new ArrayList<>();
        
        ASTNode zero = NumberConstant.make(0);
        
        ct.add(new LessEqual(zero, bound));
        
        for (int i = 1; i < starts.numChildren(); i++) {
            ct.add(new LessEqual(zero, durations.getChild(i)));
            ct.add(new LessEqual(zero, resourceReqs.getChild(i)));
        }
        
        Intpair p=starts.getChild(1).getBounds();
        Intpair dur=durations.getChild(1).getBounds();
        
        long start=p.lower;
        long end=p.upper+dur.upper;
        
        for(int i=2; i<starts.numChildren(); i++) {
            p=starts.getChild(i).getBounds();
            dur=durations.getChild(i).getBounds();
            if(start>p.lower) {
                start=p.lower;
            }
            if(end<p.upper+dur.upper) {
                end=p.upper+dur.upper;
            }
        }
        
        for(long timestep=start; timestep<=end; timestep++) {
            ArrayList<ASTNode> sum = new ArrayList<>();
            for(int i=1; i<starts.numChildren(); i++) {
                sum.add(new Times(resourceReqs.getChild(i), 
                    new And(new LessEqual(starts.getChild(i), NumberConstant.make(timestep)), 
                        new Less(NumberConstant.make(timestep), new WeightedSum(starts.getChild(i), durations.getChild(i))))));
            }
            ct.add(new LessEqual(new WeightedSum(sum), bound));
        }
        
        return new And(ct);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //
    //   Task decomposition
    
    public static ASTNode taskDecomp(ASTNode starts, ASTNode durations, ASTNode resourceReqs, ASTNode bound) {
        ArrayList<ASTNode> ct=new ArrayList<>();
        
        ASTNode zero = NumberConstant.make(0);
        
        ct.add(new LessEqual(zero, bound));
        
        for (int i = 1; i < starts.numChildren(); i++) {
            ct.add(new LessEqual(zero, durations.getChild(i)));
            ct.add(new LessEqual(zero, resourceReqs.getChild(i)));
            
            ASTNode usage = usage(i, starts, durations, resourceReqs);
            ct.add(new LessEqual(usage, bound));
        }
        
        return new And(ct);
    }
    
    //  Part of task decomposition -- resource usage at the start time of task i. 
    private static ASTNode usage(int i, ASTNode starts, ASTNode durations, ASTNode resourceReqs) {
        ArrayList<ASTNode> sumTerms = new ArrayList<>();
        
        for (int j = 1; j < starts.numChildren(); j++) {
            sumTerms.add(
                new Times(
                    new And(
                        new LessEqual(starts.getChild(j), starts.getChild(i)),
                        new Less(starts.getChild(i), new WeightedSum(starts.getChild(j), durations.getChild(j)))
                    ),
                    resourceReqs.getChild(j)
                )
            );
        }
        
        return new WeightedSum(sumTerms);
    }
    
}
