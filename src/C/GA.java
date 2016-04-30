package C;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.lang.Math;

class Population
{
    public final static int MAXPOPSIZE = 500;
    public final static int MAXVECSIZE = 30;
    public final static int MAXCONSTR = 10;
    public final static int BITS_PER_BYTE = 8;
    public final static int UINTSIZE = (BITS_PER_BYTE * 4);
    public final static double EPSILON = 1e-6;
    public final static double INFINITY = 1e7;

    public double[] xreal = new double[MAXVECSIZE]; /* real-coded variables */
    public double[] xbin = new double[MAXVECSIZE]; /* binary-coded variables */
    public double obj, penalty; /* objective fn. etc. */
    public double[] cons = new double[MAXCONSTR];
    public int[] chrom; /* chrosome string */
    public int parent1;
    public int parent2; /* s.no. of parents */
    public int crossVar; /* cross over variable */
}

public class GA
{
    static int maxGen;
    static int popSize;
    static int maxRun;
    static int nvarBin;
    static int nvarReal;
    static int tourneypos; /* Current position of tournament */
    static int tourneySize; /* Tournament size ( = 2 for binary ) */
    static int[] tourneylist = new int[Population.MAXPOPSIZE]; /* List of indices of individuals for tournament selection routine */
    static int criticalSize; /* subpopulation size used in TS (0.25*N) */
    static int lChrom; /* chrosome string length */
    static int chromsize; /* Number of bytes needed to store */
    static int nc; /* Number of constraints */
    static boolean RIGID;
    static boolean SHARING;
    static boolean REPORT;
    static int no_xover; /* No. of cross overs done */
    static int no_mutation;
    static int binmut; /* No. of mutations done */
    static int MINM;
    static int bestEver_gen; /* Generation no. of best ever indiv. */

    static double basicSeed;
    static double nDistributionC;
    static double nDistributionM;
    static double sigmaShare; /* Sharing distance */
    static double xOver; /* Cross over probability */
    static double mutationBin; /* Mutation probability */
    static double mutationReal;
    static double sumObj; /* Sum of objective fn. values */
    static double avgObj; /* Average of objective fn. values */
    static double maxObj; /* Maximum objective fn. value */
    static double minObj; /* Minimum objective fn. value */

    static PrintWriter fpOut; // fp_rep fr
    static PrintWriter fpRep; // fp_out fp
    static PrintWriter fpPlot; // fp_plot fl

    static Population[] oldPop;
    static Population[] newPop;
    static Population bestEver, currentBest; /* Best fit individual till current gen. */
    static int[] chrLen = new int[Population.MAXVECSIZE];
    static int[] xbinLower = new int[Population.MAXVECSIZE];       /* Lower and Upper bounds on each */
    static double[] minx_bin = new double[Population.MAXVECSIZE];  /* Minimum and maximum values of design */
    static double[] maxx_bin = new double[Population.MAXVECSIZE];  /* Variables in a population */
    static double[] minx_real = new double[Population.MAXVECSIZE]; /* Minimum and maximum values of design */
    static double[] maxx_real = new double[Population.MAXVECSIZE]; /* Variables in a population */

    static int[] xbinUpper = new int[Population.MAXVECSIZE];        /* design variable */
    static double[] xrealLower = new double[Population.MAXVECSIZE]; /* Lower and Upper bounds on each */
    static double[] xrealUpper = new double[Population.MAXVECSIZE]; /* Design variable */
    static double[] oldrand = new double[55]; /* Array of 55 random numbers */
    static int jrand; /* current random number */
    static double seed; /* Random seed number */
    
    static ChuongTrinhChinh ctc;

    public static void inputAppParameters()
    {
    };

    public static void inputParameters(String propFile) throws Exception
    {
        Properties prop = new Properties();
        InputStream input = null;

        input = new FileInputStream(propFile);
        // Load a properties file
        prop.load(input);

        // How many generations ?
        maxGen = Integer.parseInt(prop.getProperty("maxgen", "100"));

        // Population Size ?
        popSize = Integer.parseInt(prop.getProperty("popsize", "100"));
        if (popSize > Population.MAXPOPSIZE)
        {
            System.out.print("Increase the value of MAXPOPSIZE in program and re-run the program");
            return;
        }

        // Number of binary-coded variables (Maximum " + Population.MAXVECSIZE + ")
        nvarBin = Integer.parseInt(prop.getProperty("nvarbin", "10"));
        // Number of real-coded variables (Maximum " + Population.MAXVECSIZE + ")
        nvarReal = Integer.parseInt(prop.getProperty("nvarreal", "10"));

        if (nvarBin > 0)
        {
            lChrom = 0;
            int chrLenTemp = Integer.parseInt(prop.getProperty("chrlen", "10"));
            int xbinLowerTemp = Integer.parseInt(prop.getProperty("xbinlower", "-10"));
            int xbinUpperTemp = Integer.parseInt(prop.getProperty("xbinupper", "10"));
            for (int k = 0; k < nvarBin; k++)
            {
                // String length
                chrLen[k] = chrLenTemp;
                lChrom += chrLen[k];

                // Lower bounds of xbin
                xbinLower[k] = xbinLowerTemp;

                // Upper bounds of xbin
                xbinUpper[k] = xbinUpperTemp;
            }
        }

        if (nvarReal > 0)
        {
            double xrealLowerTmp = Double.parseDouble(prop.getProperty("xreallower", "-10"));
            double xrealUpperTmp = Double.parseDouble(prop.getProperty("xrealupper", "10"));
            for (int k = 0; k < nvarReal; k++)
            {
                // Lower bounds of xreal
                xrealLower[k] = xrealLowerTmp;

                // Upper bounds of xreal
                xrealUpper[k] = xrealUpperTmp;
            }
        }

        if (nvarReal > 0)
        {
            RIGID = "y".equals(prop.getProperty("RIGID", "n"));
        }

        SHARING = "y".equals(prop.getProperty("SHARING", "n"));
        if (SHARING)
            // Niching parameter value
            sigmaShare = Double.parseDouble(prop.getProperty("sigmashare", "3"));

        REPORT = "y".equals(prop.getProperty("REPORT", "n"));

        // How many runs ?
        maxRun = Integer.parseInt(prop.getProperty("maxrun", "10"));

        tourneySize = 2;
        // Cross Over Probability? (0 to 1)
        xOver = Double.parseDouble(prop.getProperty("xover", "0.5"));

        if (nvarBin > 0)
        {
            // Mutation Probability for binary strings? (0 to 1)
            mutationBin = Double.parseDouble(prop.getProperty("mutationbin", "0.1"));
        }
        if (nvarReal > 0)
        {
            // Mutation Probability for real variables? (0 to 1)
            mutationReal = Double.parseDouble(prop.getProperty("mutationreal", "0.1"));
        }
        if (nvarReal > 0)
        {
            // Give distr. index nDistributionC and nDistributionM for SBX and
            // mutation? ");
            nDistributionC = Double.parseDouble(prop.getProperty("nDistributionC", "0.7"));
            nDistributionM = Double.parseDouble(prop.getProperty("nDistributionM", "0.8"));
        }

        // Give random seed (0 to 1.0)
        basicSeed = Double.parseDouble(prop.getProperty("basicseed", "0.6"));

        criticalSize = popSize / 4;
        inputAppParameters();
        
        String classPath = GA.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //String pathFile = classPath.replace("bin/", "src/sample/mmA2008_MinMax.c");
        //String pathFile = classPath.replace("bin/", "src/sample/SelectionSort.c");
        //String pathFile = classPath.replace("bin/", "src/sample/tA2008_Triangle.c");
        //String pathFile = classPath.replace("bin/", "src/sample/gA2008_GreatestCommonDivisor.c");
        //String pathFile = classPath.replace("bin/", "src/sample/QuadraticEquation2.c");
        //String pathFile = classPath.replace("bin/", "src/sample/triangleMansour2004.c");
        String pathFile = classPath.replace("bin/", "src/sample/tritypeBueno2002.c");        
        //String pathFile = classPath.replace("bin/", "src/sample/iA2008_InsertionSort.c");
        //String pathFile = classPath.replace("bin/", "src/sample/mmTriangle.c");

        ctc = new ChuongTrinhChinh(pathFile);
        ctc.run();
        ctc.initPathListID(nvarReal);        
    }

    public static void selectMemory()
    {
        if (tourneySize > popSize)
        {
            System.out.println("FATAL: Tournament size > pop size ");
        }
    }

    public static void initReport()
    {
        int k;

        fpRep.printf("\n=============================================");
        fpRep.printf("\n             INITIAL REPORT                  ");
        fpRep.printf("\n=============================================");

        fpRep.printf("\n Variable Boundaries : ");
        if (nvarReal > 0)
            if (RIGID)
                fpRep.printf(" Rigid");
            else
                fpRep.printf(" Flexible");
        fpRep.printf("\n Population size            : %d", popSize);
        fpRep.printf("\n Total no. of generations   : %d", maxGen);
        fpRep.printf("\n Cross over probability     : %6.4f", xOver);
        if (nvarBin > 0)
            fpRep.printf("\n Mutation probability (binary): %6.4f", mutationBin);
        if (nvarReal > 0)
            fpRep.printf("\n Mutation probability (real): %6.4f", mutationReal);
        if (SHARING)
        {
            fpRep.printf("\n Niching to be done :");
            fpRep.printf("\n Niching parameter value: %6.4f", sigmaShare);
        }
        if (nvarBin > 0)
            fpRep.printf("\n Total String length              : %d", lChrom);
        if (nvarBin > 0)
            fpRep.printf("\n Number of binary-coded variables: %d", nvarBin);
        if (nvarReal > 0)
            fpRep.printf("\n Number of real-coded variables  : %d", nvarReal);
        fpRep.printf("\n Total Runs to be performed : %d", maxRun);
        if (nvarReal > 0)
        {
            fpRep.printf("\n Exponent (n for SBX)       : %7.2f", nDistributionC);
            fpRep.printf("\n Exponent (n for Mutation)  : %7.2f", nDistributionM);
        }
        fpRep.printf("\n Lower and Upper bounds     :");
        for (k = 0; k < nvarBin; k++)
            fpRep.printf("\n   %8.4f   <=   x_bin[%d]   <= %8.4f, string length = %d", (double) xbinLower[k], k + 1, (double) xbinUpper[k], chrLen[k]);

        for (k = 0; k < nvarReal; k++)
            fpRep.printf("\n   %8.4f   <=   x_real[%d]   <= %8.4f", xrealLower[k], k + 1, xrealUpper[k]);

        fpRep.printf("\n=================================================\n");

        appInitReport();
    }

    /* Application-dependent initial report called by initreport() */
    public static void appInitReport()
    {
    }

    /* Create next batch of 55 random numbers */
    public static void advance_random()
    {
        int j1;
        double new_random;

        for (j1 = 0; j1 < 24; j1++)
        {
            new_random = oldrand[j1] - oldrand[j1 + 31];
            if (new_random < 0.0)
                new_random = new_random + 1.0;
            oldrand[j1] = new_random;
        }
        for (j1 = 24; j1 < 55; j1++)
        {
            new_random = oldrand[j1] - oldrand[j1 - 24];
            if (new_random < 0.0)
                new_random = new_random + 1.0;
            oldrand[j1] = new_random;
        }
    }

    /* Get random off and running */
    public static void warmup_random(double random_seed)
    {
        int j1, ii;
        double new_random, prev_random;

        oldrand[54] = random_seed;
        new_random = 0.000000001;
        prev_random = random_seed;
        for (j1 = 1; j1 <= 54; j1++)
        {
            ii = (21 * j1) % 54;
            oldrand[ii] = new_random;
            new_random = prev_random - new_random;
            if (new_random < 0.0)
            {
                new_random = new_random + 1.0;
            }
            prev_random = oldrand[ii];
        }

        advance_random();
        advance_random();
        advance_random();

        jrand = 0;
    }

    public static void randomize()
    {
        int j1;

        for (j1 = 0; j1 <= 54; j1++)
            oldrand[j1] = 0.0;
        jrand = 0;

        warmup_random(seed);
    }

    public static void app_initialize()
    {

    }

    /* Fetch a single random number between 0.0 and 1.0 - */
    /* Subtractive Method . See Knuth, D. (1969), v. 2 for */
    /* details.Name changed from random() to avoid library */
    /* conflicts on some machines */
    public static double randomperc()
    {
        jrand++;
        if (jrand >= 55)
        {
            jrand = 1;
            advance_random();
        }
        return ((double) oldrand[jrand]);
    }

    public static boolean flip(double prob)
    {
        return (randomperc() <= prob);
    }

    public static void copyIndividual(Population indiv1, Population indiv2)
    {
        int k;

        for (k = 0; k < nvarBin; k++)
            indiv2.xbin[k] = indiv1.xbin[k];
        for (k = 0; k < nvarReal; k++)
            indiv2.xreal[k] = indiv1.xreal[k];
        for (k = 0; k < nc; k++)
            indiv2.cons[k] = indiv1.cons[k];
        indiv2.obj = indiv1.obj;
        indiv2.penalty = indiv1.penalty;
        indiv2.parent1 = indiv1.parent1;
        indiv2.parent2 = indiv1.parent2;
        indiv2.crossVar = indiv1.crossVar;
        for (k = 0; k < chromsize; k++)
            indiv2.chrom[k] = indiv1.chrom[k];
    }

    /*
     * ====================================================================
     * Decodes the value of a group of binary strings and puts the decoded
     * values into an array 'value'.
     * ====================================================================
     */
    public static void decodevalue(int[] chrom, double[] value)
    {
        int k, j, stop, tp, mask = 1, position, count;
        double bitpow;

        if (nvarBin <= 0)
            return;

        position = 0;
        count = 0;
        for (k = 0; k < chromsize; k++)
        {
            if (k == (chromsize - 1))
                stop = lChrom - (k * Population.UINTSIZE);
            else
                stop = Population.UINTSIZE;
            /* loop thru bits in current byte */
            tp = chrom[k];
            for (j = 0; j < stop; j++)
            {
                /* test for current bit 0 or 1 */
                if ((tp & mask) == 1)
                {
                    bitpow = Math.pow(2.0, (double) (count));
                    value[position] += bitpow;
                }
                tp = tp >> 1;
                count++;
                if (count >= chrLen[position])
                {
                    position += 1;
                    count = 0;
                }
            }
        }
    }

    public static void decodeString(Population ptr_indiv)
    {
        double[] temp;
        double coef;
        int j;

        if (nvarBin > 0)
        {
            temp = new double[nvarBin];
            for (j = 0; j < nvarBin; j++)
                temp[j] = 0.0;
            decodevalue(ptr_indiv.chrom, temp);
            for (j = 0; j < nvarBin; j++)
            {
                coef = Math.pow(2.0, (double) (chrLen[j])) - 1.0;
                temp[j] = temp[j] / coef;
                ptr_indiv.xbin[j] = temp[j] * xbinUpper[j] + (1.0 - temp[j]) * xbinLower[j];
            }
        }
    }

    private static double square(double x)
    {
        return (x * x);
    }

    private static double fTriangle(double a, double b, double c, String functionName)
    {
        double your_func = 0;

        try
        {
            your_func = ctc.calculateDistTriangle(a, b, c, functionName);
        }
        catch (Exception ex)
        {
            System.out.println("Exception");
        }

        return your_func;
    }
    
    private static double fInsertionSort(double[] a, int size, String functionName)
    {
        double your_func = 0;

        try
        {
            your_func = ctc.calculateDistInsertionSort(a, size, functionName);
        }
        catch (Exception ex)
        {
            System.out.println("Exception");
        }
        
        return your_func;
    }
    
    private static double fGreatestCommonDivisor(int a, int b)
    {
        double your_func = 0;

        try
        {
            your_func = ctc.calculateDistGreatestCommonDivisor(a, b);
        }
        catch (Exception ex)
        {
            System.out.println("Exception");
        }
        
        return your_func;
    }


    /*
     * ====================================================================
     * OBJECTIVE FUNCTION ( Supposed to be minimized) : Change it for different
     * applications
     * ====================================================================
     */
    private static int callObject = 0;
    public static void objective(Population indv)
    {
        int i;
        double term1, term2, term3, pi, your_func = 0, gsum;
        double[] g = new double[Population.MAXCONSTR];
        double[] x = new double[2 * Population.MAXVECSIZE];

        for (i = 0; i < nvarBin; i++)
            x[i] = indv.xbin[i];
        for (i = nvarBin; i < nvarBin + nvarReal; i++)
            x[i] = indv.xreal[i - nvarBin];

        MINM = 1; // use -1 for maximization
        // Put your function here
        your_func = 0;
        callObject++;
        
        //System.out.println("Call objective: " + callObject);

        //your_func = fTriangle(x[0], x[1], x[2], "Triangle");
        //your_func = fTriangle(x[0], x[1], x[2], "QuadraticEquation2");
        your_func = fTriangle(x[0], x[1], x[2], "triangleMansour2004");        
        //your_func = fInsertionSort(x, nvarReal, "InsertsionSort");
        //your_func = fInsertionSort(x, nvarReal, "GetMinMax");
        //your_func = fInsertionSort(x, nvarReal, "GetMinMaxTriangle");
        //your_func = fSelectionSort(x, nvarReal);                         
        //your_func = fGreatestCommonDivisor((int)x[0], (int)x[1]);

        nc = 0;
        // Put your constraints here
        
        indv.obj = your_func;
        for (i = 0, gsum = 0.0; i < nc; i++)
        {
            indv.cons[i] = g[i];
            if (g[i] < 0.0)
                gsum += -1.0 * g[i];
        }
        indv.penalty = gsum;
    }

    public static void initialize()
    {
        double u;
        int k, k1, j, j1, stop;
        int mask = 1, nbytes;

        randomize();
        app_initialize();
        oldPop = new Population[popSize];
        newPop = new Population[popSize];
        for (j = 0; j < popSize; j++)
        {
            oldPop[j] = new Population();
            newPop[j] = new Population();
        }

        bestEver = new Population();
        currentBest = new Population();

        chromsize = (lChrom / Population.UINTSIZE);

        if (lChrom % Population.UINTSIZE > 0)
            chromsize++;
        nbytes = chromsize * 1;

        if (nvarBin > 0)
        {
            for (j = 0; j < popSize; j++)
            {
                oldPop[j].chrom = new int[nbytes];
                newPop[j].chrom = new int[nbytes];
            }
            bestEver.chrom = new int[nbytes];
            currentBest.chrom = new int[nbytes];
        }

        for (k = 0; k < popSize; k++)
        {
            oldPop[k].obj = 0.0;
            oldPop[k].parent1 = oldPop[k].parent2 = 0;
            oldPop[k].penalty = 0.0;
            oldPop[k].crossVar = 0;
            for (j = 0; j < nc; j++)
                oldPop[k].cons[j] = 0.0;

            for (j = 0; j < nvarReal; j++)
            {
                u = randomperc();
                oldPop[k].xreal[j] = xrealLower[j] * (1 - u) + xrealUpper[j] * u;
            }

            for (k1 = 0; k1 < chromsize; k1++)
            {
                oldPop[k].chrom[k1] = 0;
                if (k1 == (chromsize - 1))
                    stop = lChrom - (k1 * Population.UINTSIZE);
                else
                    stop = Population.UINTSIZE;
                /* A fair coin toss */

                for (j1 = 1; j1 <= stop; j1++)
                {
                    if (flip(0.5))
                    {
                        oldPop[k].chrom[k1] = oldPop[k].chrom[k1] | mask;
                    }
                    if (j1 != stop)
                    {
                        oldPop[k].chrom[k1] = oldPop[k].chrom[k1] << 1;
                    }
                }
            }
        }
        no_xover = no_mutation = binmut = 0;

        copyIndividual(oldPop[0], bestEver);
        decodeString(bestEver);
        objective(bestEver);
    }

    /* Application-dependent statistics calculations called by statistics() */
    public static void appStatistics()
    {
    }

    /*
     * ====================================================================
     * Calculates statistics of current generation :
     * ====================================================================
     */
    public static void statistics(int genNo)
    {
        int k, j, change_flag;

        for (k = 0; k < popSize; k++)
        {
            decodeString(oldPop[k]);
            objective(oldPop[k]);
        }
        copyIndividual(oldPop[0], currentBest);
        sumObj = avgObj = oldPop[0].obj;
        maxObj = minObj = oldPop[0].obj;

        for (k = 0; k < nvarBin; k++)
            maxx_bin[k] = minx_bin[k] = oldPop[0].xbin[k];
        for (k = 0; k < nvarReal; k++)
            maxx_real[k] = minx_real[k] = oldPop[0].xreal[k];
        for (k = 1; k < popSize; k++)
        {
            if (currentBest.penalty > oldPop[k].penalty)
                copyIndividual(oldPop[k], currentBest);
            else if ((currentBest.penalty <= 0.0) && (oldPop[k].penalty <= 0.0))
                if (MINM * currentBest.obj > MINM * oldPop[k].obj)
                    copyIndividual(oldPop[k], currentBest);
            if (MINM * maxObj < MINM * oldPop[k].obj)
                maxObj = oldPop[k].obj;
            if (MINM * minObj > MINM * oldPop[k].obj)
            {
                minObj = oldPop[k].obj;
            }
            sumObj += oldPop[k].obj;
            for (j = 0; j < nvarBin; j++)
            {
                if (MINM * maxx_bin[j] < MINM * oldPop[k].xbin[j])
                    maxx_bin[j] = oldPop[k].xbin[j];
                if (MINM * minx_bin[j] > MINM * oldPop[k].xbin[j])
                    minx_bin[j] = oldPop[k].xbin[j];
            }
            for (j = 0; j < nvarReal; j++)
            {
                if (MINM * maxx_real[j] < MINM * oldPop[k].xreal[j])
                    maxx_real[j] = oldPop[k].xreal[j];
                if (MINM * minx_real[j] > MINM * oldPop[k].xreal[j])
                    minx_real[j] = oldPop[k].xreal[j];
            }
        }
        avgObj = sumObj / popSize;
        change_flag = 0;
        if (bestEver.penalty > currentBest.penalty)
            change_flag = 1;
        else if ((bestEver.penalty <= 0.0) && (currentBest.penalty <= 0.0))
            if (MINM * bestEver.obj > MINM * currentBest.obj)
                change_flag = 1;
        if (change_flag == 1)
        {
            copyIndividual(currentBest, bestEver);
            bestEver_gen = genNo;
        }
        appStatistics();
    }

    public static void freeAll()
    {

    }

    /*
     * ====================================================================
     * Writes a given string of 0's and 1's puts a `-` between each substring
     * (one substring for one variable) Leftmost bit is most significant bit.
     * ====================================================================
     */
    public static void writechrom(int[] chrom, PrintWriter fp)
    {
        int j, k, stop, bits_per_var, count = 0, bitcount, position;
        int mask = 1, tmp;

        if (nvarBin <= 0)
            return;

        position = 0;
        bitcount = 0;
        for (k = 0; k < chromsize; k++)
        {
            tmp = chrom[k];
            if (k == (chromsize - 1))
                stop = lChrom - (k * Population.UINTSIZE);
            else
                stop = Population.UINTSIZE;

            for (j = 0; j < stop; j++)
            {
                bits_per_var = chrLen[position];
                if ((tmp & mask) != 0)
                    fp.printf("1");
                else
                    fp.printf("0");
                count++;
                bitcount++;
                if (((count % bits_per_var) == 0) && (count < lChrom))
                    fp.printf("-");
                tmp = tmp >> 1;
                if (bitcount >= chrLen[position])
                {
                    bitcount = 0;
                    position += 1;
                }
            }
        }
    }

    /*
     * ====================================================================
     * Reporting the statistics of current population ( gen. no. 'num'): fp is
     * file pointer to output file.
     * ====================================================================
     */
    public static void report(int num)
    {
        int k, j;

        if (num == 0)
            fpPlot.printf("\n# Generation Number  Best Fitness  Average Fitness  Worst Fitness");
        fpPlot.printf("\n %d  %f  %f  %f", num, minObj, avgObj, maxObj);

        if (REPORT)
        {
            /* ----------------------------------------- */
            /* WRITING IN THE OUTPUT FILE FOR INSPECTION */
            /* ----------------------------------------- */
            fpOut.printf("\n========== Generation No. : %3d ===================", num);
            fpOut.printf("\n  No. Binary|Real x   Constr. violation    Fitness    Parents     Cross-site ");
            fpOut.printf("\n===================================================");
            for (k = 0; k < popSize; k++)
            {
                fpOut.printf("\n%3d.  ", k + 1);
                for (j = 0; j < nvarBin; j++)
                    fpOut.printf(" %8.5f", oldPop[k].xbin[j]);
                fpOut.printf("|");

                for (j = 0; j < nvarReal; j++)
                    fpOut.printf(" %8.5f", oldPop[k].xreal[j]);

                for (j = 0; j < nc; j++)
                    fpOut.printf(" %8.5f", oldPop[k].cons[j]);
                fpOut.printf(" %8.5f", oldPop[k].penalty);

                fpOut.printf("  %8.5f (%3d %3d)", oldPop[k].obj, oldPop[k].parent1, oldPop[k].parent2);
                //if (nvarBin > 0)
                //    fpOut.printf("  %d", oldPop[k].crossVar);
                if (nvarBin > 0)
                {
                    fpOut.printf("  %d", oldPop[k].crossVar);
                    fpOut.printf("\n String = ");
                    writechrom(oldPop[k].chrom, fpOut);
                }
            }
        }

        if (num == maxGen)
        {
            fpPlot.printf("\n");

            fpRep.printf("\n===================================================");
            fpRep.printf("\nMax = %8.5f  Min = %8.5f   Avg = %8.5f", maxObj, minObj, avgObj);
            fpRep.printf("\nMutations (real)= %d ; Mutations (binary) = %d ; Crossovers = %d", no_mutation, binmut, no_xover);

            if (bestEver.penalty <= 0.0)
            {
                fpRep.printf("\nBest ever fitness: %f (from generation : %d)\n", bestEver.obj, bestEver_gen);
                fpRep.printf("Variable vector: Binary | Real -> ");
                for (j = 0; j < nvarBin; j++)
                    fpRep.printf(" %f", bestEver.xbin[j]);
                fpRep.printf("|");

                for (j = 0; j < nvarReal; j++)
                    fpRep.printf(" %f", bestEver.xreal[j]);
                if (nvarBin > 0)
                {
                    fpRep.printf("\nBest_ever String = ");
                    writechrom(bestEver.chrom, fpRep);
                }
                fpRep.printf("\nConstraint value:");
                for (j = 0; j < nc; j++)
                    fpRep.printf(" %f", bestEver.cons[j]);
                fpRep.printf("| Overall penalty: %f", bestEver.penalty);
            }
            else
                fpRep.printf("No feasible solution found!\n");
            fpRep.printf("\n===================================================\n");
        }
        appReport();
    }

    public static void appReport()
    {

    }

    /* this routine should contain any application-dependent computations */
    /*
     * that should be performed before each GA cycle. called by generate_new_pop
     */
    public static void appComputation()
    {

    }

    public static void preselectTour()
    {
        reset1();
        tourneypos = 0;
    }

    /* Pick a random integer between low and high */
    public static int rnd(int low, int high)
    {
        int i;

        if (low >= high)
            i = low;
        else
        {
            i = (int) ((randomperc() * (high - low + 1)) + low);
            if (i > high)
                i = high;
        }
        return (i);
    }

    /* Name changed from reset because of clash with lib. function - RBA */
    /* Shuffles the tourneylist at random */
    public static void reset1()
    {
        int i, rand1, rand2, temp_site;

        for (i = 0; i < popSize; i++)
            tourneylist[i] = i;

        for (i = 0; i < popSize; i++)
        {
            rand1 = rnd(0, popSize - 1);
            rand2 = rnd(0, popSize - 1);
            temp_site = tourneylist[rand1];
            tourneylist[rand1] = tourneylist[rand2];
            tourneylist[rand2] = temp_site;
        }
    }

    private static double distanc(int one, int two)
    {
        int k;
        double sum;

        sum = 0.0;
        for (k = 0; k < nvarBin; k++)
            sum += square((oldPop[one].xbin[k] - oldPop[two].xbin[k]) / (xbinUpper[k] - xbinLower[k]));

        for (k = 0; k < nvarReal; k++)
            sum += square((oldPop[one].xreal[k] - oldPop[two].xreal[k]) / (xrealUpper[k] - xrealLower[k]));

        return (Math.sqrt(sum / (nvarBin + nvarReal)));
    };

    public static int tour_select_constr()
    {
        int pick, winner, i, minus = 0, rand_pick, rand_indv, flag, indv;
        boolean isBroken = false;

        /* If remaining members not enough for a tournament, then reset list */
        // start_select:
        while (true)
        {
            if ((popSize - tourneypos) < tourneySize)
            {
                reset1();
                tourneypos = 0;
            }

            /* Select tourneysize structures at random and conduct a tournament */
            winner = tourneylist[tourneypos];
            /* Added by RBA */
            if (winner < 0 || winner > popSize - 1)
            {
                System.out.println(" Warning !! ERROR1");
                System.out.printf(" tourpos = %d\n", tourneypos);
                System.out.printf(" winner  = %d\n", winner);
                preselectTour();
                // goto start_select;
                continue;
            }
            for (i = 1; i < tourneySize; i++)
            {
                pick = tourneylist[i + tourneypos];

                if ((oldPop[winner].penalty > 0.0) && (oldPop[pick].penalty <= 0.0))
                    winner = pick;
                else if ((oldPop[winner].penalty > 0.0) && (oldPop[pick].penalty > 0.0))
                {
                    if (oldPop[pick].penalty < oldPop[winner].penalty)
                        winner = pick;
                }
                else if ((oldPop[winner].penalty <= 0.0) && (oldPop[pick].penalty <= 0.0))
                {
                    if (distanc(winner, pick) < sigmaShare)
                    {
                        if (MINM * oldPop[pick].obj < MINM * oldPop[winner].obj)
                            winner = pick;
                    }
                    else
                    {
                        minus = -1;
                        for (indv = flag = 0; indv < criticalSize && flag == 0; indv++)
                        {
                            rand_indv = rnd(0, popSize - 1);
                            rand_pick = tourneylist[rand_indv];
                            if (oldPop[rand_pick].penalty <= 0.0)
                            {
                                if (distanc(winner, rand_pick) < sigmaShare)
                                {
                                    flag = 1;
                                    if (MINM * oldPop[rand_pick].obj < MINM * oldPop[winner].obj)
                                        winner = rand_pick;
                                }
                            }
                        }
                    }
                }
                if (pick < 0 || pick > popSize - 1)
                {
                    preselectTour();
                    System.out.println("\n Warning !! ERROR2");
                    // goto start_select;
                    isBroken = true;
                    break;
                }
            }
            if (isBroken)
            {
                isBroken = false;
                continue;
            }
            break;
        }
        /* Update tourneypos */
        tourneypos += tourneySize + minus;
        return (winner);
    }

    private static int tour_select()
    {
        int pick, winner, i;
        boolean isBroken = false;

        /* If remaining members not enough for a tournament, then reset list */
        // todo: start_select :
        while (true)
        {
            if ((popSize - tourneypos) < tourneySize)
            {
                reset1();
                tourneypos = 0;
            }

            /* Select tourneysize structures at random and conduct a tournament */
            winner = tourneylist[tourneypos];
            /* Added by RBA */
            if (winner < 0 || winner > popSize - 1)
            {
                System.out.print("\n Warning !! ERROR1");
                System.out.print(" tourpos = %d" + tourneypos);
                System.out.print(" winner = %d" + winner);
                preselectTour();
                // todo: goto start_select;
                continue;
            }
            for (i = 1; i < tourneySize; i++)
            {
                pick = tourneylist[i + tourneypos];
                /* Added by RBA */
                if (pick < 0 || pick > popSize - 1)
                {
                    preselectTour();
                    System.out.print("\n Warning !! ERROR2");
                    // todo: goto start_select;
                    isBroken = true;
                    break;
                }
                // case 1:
                if (oldPop[winner].penalty > oldPop[pick].penalty)
                    winner = pick;
                else if ((oldPop[winner].penalty <= 0.0) && (oldPop[pick].penalty <= 0.0))
                {
                    if (MINM * oldPop[pick].obj < MINM * oldPop[winner].obj)
                        winner = pick;
                }
            }
            if (isBroken)
            {
                isBroken = false;
                continue;
            }
            break;
        }
        /* Update tourneypos */
        tourneypos += tourneySize;
        return (winner);
    }

    /*
     * ====================================================================
     * Binary cross over routine. Cross 2 parent strings, place in 2 child strings
     * ====================================================================
     */
    private static int binary_xover(int[] parent1, int[] parent2, int[] child1, int[] child2)
    {
        int j, jcross, k;
        int mask, temp;

        if (nvarBin <= 0)
            return 0;

        jcross = rnd(1, (lChrom - 1));/* Cross between 1 and l-1 */
        for (k = 1; k <= chromsize; k++)
        {
            if (jcross >= (k * Population.UINTSIZE))
            {
                child1[k - 1] = parent1[k - 1];
                child2[k - 1] = parent2[k - 1];
            }
            else if ((jcross < (k * Population.UINTSIZE)) && (jcross > ((k - 1) * Population.UINTSIZE)))
            {
                mask = 1;
                for (j = 1; j <= (jcross - 1 - ((k - 1) * Population.UINTSIZE)); j++)
                {
                    temp = 1;
                    mask = mask << 1;
                    mask = mask | temp;
                }
                child1[k - 1] = (parent1[k - 1] & mask) | (parent2[k - 1] & (~mask));
                child2[k - 1] = (parent1[k - 1] & (~mask)) | (parent2[k - 1] & mask);
            }
            else
            {
                child1[k - 1] = parent2[k - 1];
                child2[k - 1] = parent1[k - 1];
            }
        }
        return jcross;
    }

    /*
     * ===================================================================
     * Calculates beta value for given random number u (from 0 to 1) If input
     * random numbers (u) are uniformly distributed for a set of inputs, this
     * results in uniform distribution of beta values in case of BLX , and
     * Binary Probability distribution simulation in case of SBX.
     * ====================================================================
     */
    private static double get_beta(double u)
    {
        double beta;

        if (1.0 - u < Population.EPSILON)
            u = 1.0 - Population.EPSILON;
        if (u < 0.0)
            u = 0.0;
        if (u < 0.5)
            beta = Math.pow(2.0 * u, (1.0 / (nDistributionC + 1.0)));
        else
            beta = Math.pow((0.5 / (1.0 - u)), (1.0 / (nDistributionC + 1.0)));
        return beta;
    }

    /*
     * ====================================================================
     * Creates two children from parents p1 and p2, stores them in addresses
     * pointed by c1 and c2. low and high are the limits for x values and
     * rand_var is the random variable used to create children points.
     * ====================================================================
     */
    private static void create_children(double p1, double p2, double low, double high, double[] xreal)
    {
        double difference, x_mean, beta, v2, v1;
        double distance, umax, temp, alpha;
        double rand_var;

        if (p1 > p2)
        {
            temp = p1;
            p1 = p2;
            p2 = temp;
        }
        x_mean = (p1 + p2) * 0.5;
        difference = p2 - p1;
        if ((p1 - low) < (high - p2))
            distance = p1 - low;
        else
            distance = high - p2;
        if (distance < 0.0)
            distance = 0.0;
        if (RIGID && (difference > Population.EPSILON))
        {
            alpha = 1.0 + (2.0 * distance / difference);
            umax = 1.0 - (0.5 / Math.pow((double) alpha, (double) (nDistributionC + 1.0)));
            rand_var = umax * randomperc();
        }
        else
            rand_var = randomperc();
        beta = get_beta(rand_var);
        if (Math.abs(difference * beta) > Population.INFINITY)
            beta = Population.INFINITY / difference;
        v2 = x_mean + beta * 0.5 * difference;
        v1 = x_mean - beta * 0.5 * difference;

        if (v2 < low)
            v2 = low;
        if (v2 > high)
            v2 = high;
        if (v1 < low)
            v2 = low;
        if (v1 > high)
            v2 = high;
        xreal[0] = v1;
        xreal[1] = v2;
    }

    /*
     * ====================================================================
     * CROSS - OVER USING strategy of uniform 50% variables For one variable
     * problem, it is crossed over as usual. For multivariables, each variable
     * is crossed over with a probability of 50 % , each time generating a new
     * random beta.
     * ====================================================================
     */
    private static void cross_over(int first, int second, int childno1, int childno2)
    {
        int site, k, x_s;
        x_s = 0;
        double [] xreal = new double[2];

        if (flip(xOver)) /* Cross over has to be done */
        {
            no_xover++;
            if (nvarBin > 0)
            {
                x_s = binary_xover(oldPop[first].chrom, oldPop[second].chrom, newPop[childno1].chrom, newPop[childno2].chrom);
                newPop[childno1].crossVar = newPop[childno2].crossVar = x_s;
            }
            if (nvarReal > 0)
            {
                for (site = 0; site < nvarReal; site++)
                {
                    if (flip(0.5) || (nvarReal == 1))
                    {
                        create_children(oldPop[first].xreal[site], oldPop[second].xreal[site], xrealLower[site], xrealUpper[site], xreal);
                        newPop[childno1].xreal[site] = xreal[0];
                        newPop[childno2].xreal[site] = xreal[1];
                    }
                    else
                    {
                        newPop[childno1].xreal[site] = oldPop[first].xreal[site];
                        newPop[childno2].xreal[site] = oldPop[second].xreal[site];
                    }
                } /* for loop */
                if (nvarBin == 0)
                    newPop[childno1].crossVar = newPop[childno2].crossVar = 0;
            } /* if REALGA */
        } /* Cross over done */
        else
        /* Passing x-values straight */
        {
            for (k = 0; k < chromsize; k++)
            {
                newPop[childno1].chrom[k] = oldPop[first].chrom[k];
                newPop[childno2].chrom[k] = oldPop[second].chrom[k];
            }
            for (site = 0; site < nvarReal; site++)
            {
                newPop[childno1].xreal[site] = oldPop[first].xreal[site];
                newPop[childno2].xreal[site] = oldPop[second].xreal[site];
            }
            for (site = 0; site < nvarBin; site++)
            {
                newPop[childno1].xbin[site] = oldPop[first].xbin[site];
                newPop[childno2].xbin[site] = oldPop[second].xbin[site];
            }
            newPop[childno1].crossVar = newPop[childno2].crossVar = 0;
        }
    }

    /*
     * ================================================================== For
     * given u value such that -1 <= u <= 1, this routine returns a value of
     * delta from -1 to 1. Exact value of delta depends on specified
     * n_distribution. This is called by mutation().
     * ====================================================================
     */
    private static double get_delta(double u, double delta_l, double delta_u)
    {
        double delta, aa;

        if (u >= 1.0 - 1.0e-9)
            delta = delta_u;
        else if (u <= 0.0 + 1.0e-9)
            delta = delta_l;
        else
        {
            if (u <= 0.5)
            {
                aa = 2.0 * u + (1.0 - 2.0 * u) * Math.pow((1 + delta_l), (nDistributionM + 1.0));
                delta = Math.pow(aa, (1.0 / (nDistributionM + 1.0))) - 1.0;
            }
            else
            {
                aa = 2.0 * (1 - u) + 2.0 * (u - 0.5) * Math.pow((1 - delta_u), (nDistributionM + 1.0));
                delta = 1.0 - Math.pow(aa, (1.0 / (nDistributionM + 1.0)));
            }
        }
        if (delta < -1.0 || delta > 1.0)
        {
            System.out.printf("Error in mutation!! delta = %f\n", delta);
            return (-1);
        }
        return (delta);
    }

    /*
     * ================================================================== Binary
     * mutation routine ( borrowed from sga.c )
     * ====================================================================
     */
    private static void binmutation(int[] child)
    /* Mutate an allele w/ pmutation, count # of mutations */
    {
        int j, k, stop;
        int mask, temp = 1;

        if (nvarBin <= 0)
            return;
        for (k = 0; k < chromsize; k++)
        {
            mask = 0;
            if (k == (chromsize - 1))
                stop = lChrom - (k * Population.UINTSIZE);
            else
                stop = Population.UINTSIZE;
            for (j = 0; j < stop; j++)
            {
                if (flip(mutationBin))
                {
                    mask = mask | (temp << j);
                    binmut++;
                }
            }
            child[k] = child[k] ^ mask;
        }
    }

    /*
     * ===================================================================
     * Mutation Using polynomial probability distribution. Picks up a random
     * site and generates a random number u between -1 to 1, ( or between minu
     * to maxu in case of rigid boudaries) and calls the routine get_delta() to
     * calculate the actual shift of the value.
     * ====================================================================
     */
    private static void mutation(Population indiv)
    {
        double distance1, x, delta_l, delta_u, delta, u;
        int site;

        if (nvarReal > 0)
            for (site = 0; site < nvarReal; site++)
            {
                if (flip(mutationReal))
                {
                    no_mutation++;
                    if (RIGID)
                    {
                        x = indiv.xreal[site];
                        distance1 = xrealLower[site] - x;
                        delta_l = distance1 / (xrealUpper[site] - xrealLower[site]);
                        if (delta_l < -1.0)
                            delta_l = -1.0;

                        distance1 = xrealUpper[site] - x;
                        delta_u = distance1 / (xrealUpper[site] - xrealLower[site]);
                        if (delta_u > 1.0)
                            delta_u = 1.0;

                        if (-1.0 * delta_l < delta_u)
                            delta_u = -1.0 * delta_l;
                        else
                            delta_l = -1.0 * delta_u;
                    }
                    else
                    {
                        delta_l = -1.0;
                        delta_u = 1.0;
                    }
                    u = randomperc();
                    /* calculation of actual delta value */
                    delta = get_delta(u, delta_l, delta_u) * (xrealUpper[site] - xrealLower[site]);
                    indiv.xreal[site] += delta;

                    if (indiv.xreal[site] < xrealLower[site])
                        indiv.xreal[site] = xrealLower[site];
                    if (indiv.xreal[site] > xrealUpper[site])
                        indiv.xreal[site] = xrealUpper[site];
                } /* if flip() */
            }
        if (nvarBin > 0)
            binmutation(indiv.chrom);
    }

    /*
     * ====================================================================
     * GENERATION OF NEW POPULATION through SELECTION, XOVER & MUTATION :
     * ====================================================================
     */
    public static void generatenewPop()
    {
        int k, mate1, mate2;

        appComputation();

        preselectTour();

        for (k = 0; k < popSize; k += 2)
        {
            // selection
            if (SHARING)
            {
                mate1 = tour_select_constr();
                mate2 = tour_select_constr();
            }
            else
            {
                mate1 = tour_select();
                mate2 = tour_select();
            }
            // crossover
            cross_over(mate1, mate2, k, k + 1);
            // mutation
            mutation(newPop[k]);
            mutation(newPop[k + 1]);
            newPop[k].parent1 = newPop[k + 1].parent1 = mate1 + 1;
            newPop[k].parent2 = newPop[k + 1].parent2 = mate2 + 1;
        }
    }    
    
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println("Please give a path of setting file");
            return;
        }
        exeGA(args[0]);
    }

    public static void exeGA(String iniFileName) throws Exception
    {
        int genNo;
        Population[] temp;

        // TODO Auto-generated method stub

        inputParameters(iniFileName);

        fpOut = new PrintWriter("java-report.out", "UTF-8");
        fpRep = new PrintWriter("java-result.out", "UTF-8");
        fpPlot = new PrintWriter("java-plot.out", "UTF-8");

        selectMemory();
        initReport();

        for (int run = 1; run <= maxRun; run++)
        {
            System.out.println("Run No. " + run + " :  Wait Please .........");

            fpRep.printf("Run No. %d ", run);
            seed = basicSeed + (1.0 - basicSeed) * (double) (run - 1) / (double) maxRun;
            if (seed > 1.0)
                System.out.println("Warning !!! seed number exceeds 1.0");
            genNo = 0;
            initialize();
            statistics(genNo);
            report(0);
            for (genNo = 1; genNo <= maxGen; genNo++)
            {
                generatenewPop();

                temp = oldPop;
                oldPop = newPop;
                newPop = temp;

                statistics(genNo);
                report(genNo);
            }
            /* One GA run is over */
            freeAll();
        } /* for loop of run */

        fpOut.close();
        fpRep.close();
        fpPlot.close();
    }
}