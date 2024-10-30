package savilerow;
/*

    Savile Row http://savilerow.cs.st-andrews.ac.uk/
    Copyright (C) 2014-2018 Peter Nightingale
    
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

import savilerow.*;

public class HelpText {
    public static void printHelp() {
        System.out.println(
        "Savile Row " + CmdFlags.version + " (Repository Version: " + RepositoryVersion.repositoryVersion + ")\n"
        +" -help                         Print this message.\n"
        +"\n"
        +"Specifying Essence Prime input files:\n"
        +" -in-eprime <filename>         Optional if filename ends with \".eprime\".\n"
        +" -in-param <filename>          Parameter file; optional if ends with \".param\".\n"
        +" -params <string>              Format is same as for parameter file; language\n"
        +"                               line is optional: \"letting identifier=value\" or\n"
        +"                               \"letting identifier be value\". For example:\n"
        +"                                 -params \"letting n_nurses=4 letting \\\n"
        +"                                 Demand=[[1,0,1,0],[0,2,1,0]]\"\n"
        +"\n"
        +"Specifying output format:\n"
        +" -minion  (default)            Minion 3 format, flat for numerical expressions\n"
        +"                               but non-flat for And/Or, which are implemented\n"
        +"                               with watched-and/watched-or metaconstraints.\n"
        +" -gecode                       FlatZinc format for fzn-gecode. Uses mostly\n"
        +"                               same translation pipeline as for Minion output.\n"
        +"                               Main differences: model is entirely flat, some\n"
        +"                               constraints may be rewritten for reification.\n"
        +" -chuffed                      Format for fzn-chuffed. Similar to -gecode.\n"
        +" -or-tools                     Format for fzn-ortools. Similar to -gecode.\n"
        +" -flatzinc                     Standard FlatZinc output for any FlatZinc \n"
        +"                               solver.\n"
        +" -minizinc                     Partly flat, instance-level subset of MiniZinc,\n"
        +"                               allowing access to several solvers via the\n"
        +"                               MiniZinc toolchain. Similar to FlatZinc outputs\n"
        +" -sat                          DIMACS format for SAT solver.\n"
        +" -smt                          SMT-LIB 2 format for SMT solvers. \n"
        +" -maxsat                       DIMACS-like format for MaxSAT solver. \n"
        +"\n"
        +"Output filename options (each has default value so all are optional):\n"
        +" -out-minion <filename>        Minion output.\n"
        +" -out-gecode <filename>        Gecode (FlatZinc) output.\n"
        +" -out-chuffed <filename>       Chuffed (FlatZinc) output.\n"
        +" -out-flatzinc <filename>      FlatZinc output. \n"
        +" -out-minizinc <filename>      Minizinc output.\n"
        +" -out-sat <filename>           SAT (DIMACS) output, MaxSAT output.\n"
        +" -out-smt <filename>           SMT output.\n"
        +" -out-dominion <filename>      Dominion output.\n"
        +" -out-solution <filename>      Solution if Savile Row runs a solver and parses\n"
        +"                               the solver's output.\n"
        +" -out-info <filename>          Statistics from Savile Row and Minion.\n"
        +" -out-aux <filename>           Symbol table; required for ReadSolution mode.\n"
        +" -save-symbols                 Switch on output of symbol table.\n"
        +"\n"
        +"Optimisation levels:\n"
        +" -O0                           Switch off all optional optimisations.\n"
        +"                               Expressions are still simplified. For example,\n"
        +"                               boolean expression 3+4=10 simplifies to false.\n"
        +" -O1                           Space and time efficient optimisations:\n"
        +"                               -active-cse and -deletevars.\n"
        +" -O2  (default)                Generally recommended optimisations: -O1,\n"
        +"                               -reduce-domains-extend, and -aggregate. \n"
        +" -O3                           Most available optimisations: -O2, -ac-cse\n"
        +"                               and -tabulate.\n"
        +"\n"
        +"Symmetry breaking levels:\n"
        +" -S0                           Switch off all optimisations that change the\n"
        +"                               number of solutions.\n"
        +" -S1  (default)                Allow Savile Row to delete unused variables,\n"
        +"                               and to create auxiliary variables that are not\n"
        +"                               necessarily functionally defined by the \n"
        +"                               primary ('find') variables. May change the \n"
        +"                               number of solutions. Equivalent to \n"
        +"                               -remove-redundant-vars and -aux-non-functional.\n"
        +" -S2                           In addition to -S1, detect and break symmetry\n"
        +"                               among decision variables (-var-sym-breaking)\n"
        +"                               by applying a graph automorphism solver.\n"
        +"\n"
        +"Translation Options (CSE is common subexpression elimination):\n"
        +" -no-cse                       Switch off CSE.\n"
        +" -identical-cse                Perform CSE for identical subexpressions only.\n"
        +" -active-cse                   Perform Active CSE. May be used with -ac-cse;\n"
        +"                               attempts to match expressions modulo a simple\n"
        +"                               transformation such as negation. Active CSE \n"
        +"                               includes Identical CSE. \n"
        +" -ac-cse                       Perform Associative-Commutative CSE for\n"
        +"                               operators And (/\\), Or (\\/), Product and Sum;\n"
        +"                               exploits associativity and commutativity.\n"
        +" -active-ac-cse                Perform Active AC-CSE; extends AC-CSE on sums\n"
        +"                               by matching a subexpression with its negation.\n"
        +"                               For example it can extract x+y from the two\n"
        +"                               expressions x+y+z and w-x-y. \n"
        +"                               Identical to AC-CSE on And, Or and Product.\n"
        +" -deletevars                   Switch on variable deletion for variables that\n"
        +"                               equal a constant or another decision variable.\n"
        +" -reduce-domains               Filter domains of 'find' decision variables if\n"
        +"                               Minion is available. Calls Minion using \n"
        +"                               SACBounds_limit preprocessing, a restricted \n"
        +"                               form of SAC where the SAC test is applied to \n"
        +"                               the upper and lower bound of each variable and\n"
        +"                               the number of iterations is bounded. \n"
        +" -reduce-domains-extend        Extension of -reduce-domains that filters\n"
        +"                               domains of auxiliary and 'find' variables.\n"
        +" -aggregate                    Collect constraints into global constraints.\n"
        +"                               Currently performs two types of aggregation:\n"
        +"                               constructing GCC constraints from atleast and \n"
        +"                               atmost, and AllDifferent from not-equal, \n"
        +"                               less-than and shorter AllDifferent constraints.\n"
        +" -tabulate                     Convert some constraint expressions into table\n"
        +"                               constraints to improve propagation. Candidate\n"
        +"                               expressions are identified by a set of \n"
        +"                               heuristics. See documentation for more detail.\n"
        +" -factor-encoding              Apply the factor encoding to strengthen \n"
        +"                               propagation of overlapping table constraints.\n"
        +" -no-bound-vars                When translating to Minion, never use BOUND\n"
        +"                               type variables; use DISCRETE even for large\n"
        +"                               domains. Default is DISCRETE for domain size up\n"
        +"                               to 10,000, BOUND for larger domains. Using\n"
        +"                               BOUND type variables can reduce the level of\n"
        +"                               consistency enforced for some constraints.\n"
        +" -remove-redundant-vars        Remove redundant variables by adding constraint\n"
        +"                               assigning variables that are not mentioned in\n"
        +"                               some constraint or the objective function.\n"
        +"                               Enabling this may change the number of \n"
        +"                               solutions.\n"
        +" -aux-non-functional           Allow Savile Row to create auxiliary variables\n"
        +"                               that are not functionally defined on the \n"
        +"                               primary variables. May change the number of\n"
        +"                               solutions. \n"
        +" -var-sym-breaking             Automatically identify and break symmetry \n"
        +"                               among decision variables using a graph \n"
        +"                               automorphism solver. \n"
        +"\n"
        +"SAT Encoding Options:\n"
        +" -sat-polarity                 Use half-reification when flattening for SAT.\n"
        +" -amo-detect                   Detect AMO and EO relations as in CP 2019 paper\n"
        +"                               (see documentation for details). Detected\n"
        +"                               AMOs and EOs are used with MDD, GPW, LPW, SWC, \n"
        +"                               GGT, RGGT, GGTh, and GMTO encodings of both PB \n"
        +"                               and other sum constraints. \n"
        +"\n"
        +"  Encoding of at-most-one (AMO) and exactly-one constraints:\n"
        +" -sat-amo-product (default)    Use Chen's 2-product encoding.\n"
        +" -sat-amo-commander            Use the commander-variable encoding with group \n"
        +"                               size 3. \n"
        +" -sat-amo-ladder               The ladder encoding.\n"
        +" -sat-amo-bimander             The bimander (binary and commander) encoding.\n"
        +" -sat-amo-tree                 A totalizer-like encoding.\n"
        +"\n"
        +"  Encoding of pseudo-Boolean (PB) sum constraints:\n"
        +" -sat-pb-mdd                   Multi-value decision diagram (MDD) encoding.\n"
        +" -sat-pb-gpw                   Global polynomial watchdog. \n"
        +" -sat-pb-lpw                   Local polynomial watchdog. \n"
        +" -sat-pb-swc                   Sequential weighted counter.\n"
        +" -sat-pb-ggt                   Generalized generalized totalizer. \n"
        +" -sat-pb-rggt                  Reduced generalized generalized totalizer.\n"
        +" -sat-pb-ggth                  GGT built with the minRatio heuristic.\n"
        +" -sat-pb-gmto                  Generalized n-Level Modulo Totalizer.\n"
        +" -sat-pb-tree  (default)       A totalizer-like encoding.\n"
        +"\n"
        +"  Encoding of other linear constraints:\n"
        +" -sat-sum-mdd                  Multi-value decision diagram (MDD) encoding.\n"
        +" -sat-sum-gpw                  Global polynomial watchdog. \n"
        +" -sat-sum-lpw                  Local polynomial watchdog.\n"
        +" -sat-sum-swc                  Sequential weighted counter.\n"
        +" -sat-sum-ggt                  Generalized generalized totalizer. \n"
        +" -sat-sum-rggt                 Reduced generalized generalized totalizer.\n"
        +" -sat-sum-ggth                 GGT built with the minRatio heuristic.\n"
        +" -sat-sum-gmto                 Generalized n-Level Modulo Totalizer.\n"
        +" -sat-sum-tree  (default)      A totalizer-like encoding.\n"
        +" \n"
        +"  Encoding of table constraints:\n"
        +" -sat-table-mdd                Multi-value decision diagram (MDD) encoding for\n"
        +"                               table constraints. Default is support (Bacchus)\n"
        +"                               encoding. \n"
        +"\n"
        +"SMT Encoding and Backend Options:\n"
        +" -smt-bv  (default)            Use QF_BV (theory of bit vectors).\n"
        +" -smt-idl                      Use QF_IDL (integer difference logic).\n"
        +" -smt-lia                      Use QF_LIA (linear integer arithmetic).\n"
        +" -smt-nia                      Use QF_NIA (nonlinear integer arithmetic).\n"
        +" -smt-flat                     Use the flat encoding (see CP 2020 paper).\n"
        +" -smt-nested  (default)        Use the nested encoding (see CP 2020 paper).\n"
        +" -smt-no-decomp-alldiff        Do not decompose allDiff, use distinct instead.\n"
        +" -smt-pairwise-alldiff         Use a pairwise not-equal decomposition instead\n"
        +"                               of the default linear decomposition of allDiff.\n"
        +" -boolector  (default)         With -run-solver runs the Boolector solver.\n"
        +" -z3                           With -run-solver runs the solver Z3.\n"
        +" -yices2                       With -run-solver runs the solver Yices2.\n"
        +"                               If no solver is chosen, a default solver for\n"
        +"                               the chosen logic will be used as described\n"
        +"                               in the manual. \n"
        +"\n"
        +"Warnings:\n"
        +" -Wundef                       Identify potentially undefined expressions.\n"
        +"\n"
        +"Controlling Savile Row:\n"
        +" -timelimit <time>             Wall clock time limit in seconds. Savile\n"
        +"                               Row stops when time limit is reached, unless\n"
        +"                               translation is complete and a solver is already\n"
        +"                               running. To apply a time limit to a solver, use\n"
        +"                               -solver-options.\n"
        +" -cnflimit <max>               Limit SAT output to at most <max> clauses.\n"
        +" -seed <integer>               Some transformations use a pseudorandom number\n"
        +"                               generator; this sets the seed value.\n"
        +"Solver control:\n"
        +" -run-solver                   Run the backend solver. Also parse solver output.\n"
        +" -all-solutions                Output all solutions, to a sequence of numbered\n"
        +"                               files, one per solution. For example,\n"
        +"                               \"nurses.param.solution.000001\" to \"....000871\"\n"
        +"                               Cannot be used on optimisation problems.\n"
        +" -num-solutions <n>            Output <n> solutions, to a sequence of numbered\n"
        +"                               files as for -all-solutions.\n"
        +"                               Cannot be used on optimisation problems.\n"
        +" -solutions-to-null            Do not output solutions in any way.\n"
        +" -solver-options <string>      Pass through additional options to solver.\n"
        +" -solutions-to-stdout          Instead of writing solutions to files, send\n"
        +"                               them to stdout separated by ----------.\n"
        +"Solver control -- Minion:\n"
        +" -minion-bin <filename>        Specify where the Minion binary is. Default: use\n"
        +"                               the one included in the Savile Row distribution.\n"
        +" -preprocess                   Strength of preprocessing. Passed to Minion both \n"
        +"                               for solving (when using -run-solver) and for \n"
        +"                               domain filtering (when using -reduce-domains or \n"
        +"                               -O2 or higher). Possible values: None, GAC, \n"
        +"                               SACBounds, SACBounds_limit, SAC, SAC_limit, SSAC, \n"
        +"                               SSAC_limit, SSACBounds, SSACBounds_limit. \n"
        +"                               Default is SACBounds_limit.\n"
        +"Solver control -- Gecode:\n"
        +" -gecode-bin <filename>        Specify the Gecode FlatZinc binary.\n"
        +"                               Default is \"fzn-gecode\".\n"
        +"Solver control -- Chuffed:\n"
        +" -chuffed-bin <filename>       Specify the Chuffed FlatZinc binary.\n"
        +"                               Default is \"fzn-chuffed\".\n"
        +"\n"
        +"Solver control -- standard FlatZinc:\n"
        +" -fzn-bin                      Specify FlatZinc solver binary. \n"
        +"\n"
        +"Solver control -- SAT solver:\n"
        +" -sat-family <name>            Family of solver: \"cadical\", \"kissat\", \n"
        +"                               \"minisat\", \"glucose\", \"lingeling\", \n"
        +"                               \"nbc_minisat_all\", \"bc_minisat_all\". Allows \n"
        +"                               parsing output when using the -run-solver flag. \n"
        +"                               Default is \"kissat\". The _all values imply the \n"
        +"                               -all-solutions flag. \n"
        +" -satsolver-bin <filename>     Name of SAT solver binary. Default is\n"
        +"                               to use bundled Kissat solver, otherwise set\n"
        +"                               to \"minisat\", \"glucose\", \"lingeling\", \n"
        +"                               \"nbc_minisat_all_release\", or \n"
        +"                               \"bc_minisat_all_release\" based on -sat-family.\n"
        +" -interactive-solver           Enables interactive usage of the supported \n"
        +"                               solvers when Savile Row built with this \n"
        +"                               feature. Supported SAT solvers are used \n"
        +"                               incrementally via JNI calls. Currently \n"
        +"                               supported: \"glucose\", \"cadical\" and \n"
        +"                               \"nbc_minisat_all\".\n"
        +" -opt-strategy <name>          Controls optimisation for SAT and SMT solvers.\n"
        +"                               May be linear, unsat, or bisect (default) as\n"
        +"                               described in the manual.\n"
        +"                               \n"
        +"Solver control -- SMT solver:\n"
        +" -boolector-bin <filename>     Name of Boolector solver binary. \n"
        +" -z3-bin <filename>            Name of Z3 solver binary.\n"
        +" -yices2-bin <filename>        Name of Yices 2 solver binary.\n"
        +"\n"
        +"Mode of operation:\n"
        +" -mode ReadSolution            Takes a solution table file created by Minion\n"
        +"                               and produces an Essence Prime solution file.\n"
        +"                               If the solution table file contains multiple\n"
        +"                               solutions, -all-solutions can be used to parse\n"
        +"                               them all, or -num-solutions <n> for first n.\n"
        +" -minion-sol-file <filename>   Solution table file, produced by Minion's\n"
        +"                               -solsout flag. In ReadSolution mode, flags\n"
        +"                               -out-aux and -out-solution are required, so\n"
        +"                               that Savile Row can load the symbol table\n"
        +"                               saved when translating the problem instance,\n"
        +"                               and to specify where to write solutions, but\n"
        +"                               -all-solutions and -num-solutions are optional.\n"
        +"Examples:\n"
        +"     ./savilerow examples/sudoku/sudoku.eprime \\\n"
        +"       examples/sudoku/sudoku.param -run-solver\n"
        +"  Use default optimisation (-O2) and solver (Minion). Minion is called twice,\n"
        +"  first to filter variable domains and then to solve the instance.\n"
        +"     ./savilerow examples/carSequencing/carSequencing.eprime \\\n"
        +"       examples/carSequencing/carSequencing10.param -O3 -sat -run-solver\n"
        +"  Targets SAT; with no solver specified, the default (kissat) will be used.\n"
        );
    }
}