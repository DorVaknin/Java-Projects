package edu.cg;
import java.lang.Math;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class SeamsCarver extends ImageProcessor {

    // MARK: An inner interface for functional programming.
    @FunctionalInterface
    interface ResizeOperation {
        BufferedImage resize ();
    }

    // MARK: Fields
    private int numOfSeams;
    private ResizeOperation resizeOp;
    private boolean increaseWidth;
    boolean[][] imageMask;
    private long[][] costMatrix;
    private int[][] indexMap;
    private int k;
    private BufferedImage greyScaledImage;
    int[][] seamsArr;
    private long avgRGBValue;
    private int[][] seamsDirection;
    private boolean[][] seamsMask; 
    private boolean[][] newMask;


    public SeamsCarver (Logger logger, BufferedImage workingImage, int outWidth, RGBWeights rgbWeights,
                        boolean[][] imageMask) {
        super((s) -> logger.log("Seam carving: " + s), workingImage, rgbWeights, outWidth, workingImage.getHeight());

        numOfSeams = Math.abs(outWidth - inWidth);
        this.imageMask = imageMask;
        newMask = new boolean[outHeight][outWidth];
        if (inWidth < 2 | inHeight < 2)
            throw new RuntimeException("Can not apply seam carving: workingImage is too small");

        if (numOfSeams > inWidth / 2)
            throw new RuntimeException("Can not apply seam carving: too many seams...");

        this.k = 0;
        // Setting resizeOp by with the appropriate method reference
        if (outWidth > inWidth) {
            resizeOp = this::increaseImageWidth;
        	increaseWidth = true;
        }
        else if (outWidth < inWidth) {
            logger.log("resizeOp = reduce");
            resizeOp = this::reduceImageWidth;
        } else
            resizeOp = this::duplicateWorkingImage;

        //initialize indexMap matrix to x values
        costMatrix = new long[this.inWidth][this.inHeight];

        indexMap = new int[inWidth][inHeight];
        logger.log("Initiating index map");
        initialIndexMap();
        //initializing the cost matrix
        logger.log("Greyscaling");
        greyScaledImage = greyscale();
        seamsMask = new boolean [inWidth][inHeight];
        logger.log("Avg RGB val");
        avgRGBValue = calcAvgRGBVal();

        logger.log("Finding all seams");
        seamsArr = findAllSeams();
        
        this.logger.log("preliminary calculations were ended.");
    }

    public BufferedImage resize () {
        return resizeOp.resize();
    }

    private BufferedImage reduceImageWidth () {
        logger.log("ReduceImage start");
        BufferedImage result = newEmptyOutputSizedImage();
        setForEachWidth(outWidth);
        setForEachHeight(outHeight);
        forEach((y, x) -> {
            result.setRGB(x, y, workingImage.getRGB(indexMap[x][y],y));
        });
        logger.log("ReduceImage end");
        return result;
    }


    private BufferedImage increaseImageWidth () {
    	logger.log("IncreaseImage start");
    	
    	BufferedImage output = newEmptyOutputSizedImage();
    	int[][] newIndexMap = new int[outWidth][outHeight];
		for (int y = 0; y < inHeight; y++) {
    		int seamsCount = 0;
			for (int x = 0; x < inWidth; x++) {			
				output.setRGB(x + seamsCount, y, this.workingImage.getRGB(x, y));
				newIndexMap[x + seamsCount][y] = x;				
	    		if  (seamsMask[x][y]) { 
	    			seamsCount++;
	    			output.setRGB(x + seamsCount, y, this.workingImage.getRGB(x, y));
					newIndexMap[x + seamsCount][y] = x;
	    		}
			}
			
			if (seamsCount != numOfSeams) {
				System.out.println("BAD: " + y);
			}
        }
		indexMap = newIndexMap;
    		
    return output;

	}

    public BufferedImage showSeams (int seamColorRGB) {
        BufferedImage output = duplicateWorkingImage();        
        for (int i = 0; i < seamsArr.length; i++) {
            for (int y = 0; y < seamsArr[i].length; y++) {
                output.setRGB(seamsArr[i][y], y, seamColorRGB);
             }
        }

        return output;
    }

    public boolean[][] getMaskAfterSeamCarving () {
        setForEachOutputParameters();
        resizeOp = this::increaseImageWidth;
        increaseWidth = true;
        forEach((y,x) -> {
        	newMask[y][x] = imageMask[y][indexMap[x][y]];
        });

        return newMask;
    }

    public long pixelEnergy (int x, int y) {
        long magnitude = gradientMagnitude(x, y);

        if (imageMask[y][indexMap[x][y]]) {
            magnitude += Integer.MAX_VALUE;
        }
        return magnitude;
    }

    public int gradientMagnitude (int x, int y) {
        int pixel = getGreyscale(indexMap[x][y], y);
        int horizontalPixel;
        int verticalPixel;
        int dx = 0;
        int dy = 0;

        int deltaX = 1;
        int deltaY = 1;

        if (x == inWidth - k - 1) {
            deltaX = -1;
        }
        horizontalPixel = getGreyscale(indexMap[x + deltaX][y], y);
        dx = horizontalPixel - pixel;

        if (y == inHeight - 1) {
            deltaY = -1;
        }
        verticalPixel = getGreyscale(indexMap[x][y + deltaY], y + deltaY);
        dy = verticalPixel - pixel;


        int pixelMagnitude = Math.abs(dx) + Math.abs(dy);

        return pixelMagnitude;
    }

    private long pixDiff (int x1, int y1, int x2, int y2) {
        return Math.abs(getGreyscale(x1, y1) - getGreyscale(x2, y2));
    }

    private int getGreyscale (int x, int y) {
        return greyScaledImage.getRGB(x, y) & 0xFF;
    }

    public long[] calcPixCosts (int x, int y) {
        long cL = 0;
        long cV = 0;
        long cR = 0;

        if (y == 0) {
            return new long[]{0, 0, 0};
        }

        if (0 < x && x < inWidth - k - 1) { //if not an edge
            cV = pixDiff(indexMap[x - 1][y], y,
                    indexMap[x + 1][y], y);

            cL = pixDiff(indexMap[x][y - 1], y - 1,
                    indexMap[x - 1][y], y) + cV;

            cR = pixDiff(indexMap[x][y - 1], y - 1,
                    indexMap[x + 1][y], y) + cV;

        } else if (x == 0) {
            //the case when its in the first column, we ignore the costs because we can't get column -1
            cV = Math.abs(avgRGBValue - getGreyscale(indexMap[x + 1][y], y));
            cR = pixDiff(indexMap[x][y - 1], y - 1,
                    indexMap[x + 1][y], y) + cV;
            cL = 255; //Integer.MAX_VALUE;
        } else if (x == inWidth - k - 1) {
            cV = Math.abs(getGreyscale(indexMap[x - 1][y], y) - avgRGBValue);
            cL = pixDiff(indexMap[x][y - 1], y - 1,
                    indexMap[x - 1][y], y) + cV;
            cR = 255; //Integer.MAX_VALUE;
        }

        return new long[]{cL, cV, cR};
    }

    public void costMatrixCalc () {
        costMatrix = new long[inWidth - k][inHeight];
        seamsDirection = new int[inWidth -k][inHeight];
        setForEachWidth(inWidth - k);

        logger.log("foreach width: " + getForEachWidth());
        forEach((y, x) -> {

            costMatrix[x][y] = pixelEnergy(x, y);

            if (y > 0) {
                long[] costs = calcPixCosts(x, y);
                long leftMatrixCost = Integer.MAX_VALUE;
                long rightMatrixCost = Integer.MAX_VALUE;
                long verticalMatrixCost = costMatrix[x][y-1];

                if (x > 0) {
                    leftMatrixCost = costMatrix[x - 1][y - 1];
                }

                if (x < costMatrix.length - 1) {
                    rightMatrixCost = costMatrix[x + 1][y - 1];
                }

                costs[0] += leftMatrixCost;
                costs[1] += verticalMatrixCost;
                costs[2] += rightMatrixCost;

                int minIndex = 1;

                if (x == 0) {
                    long min = Math.min(costs[1], costs[2]);
                    if (min == costs[2]) {
                        minIndex = 2;
                    }
                } else if (x == costMatrix.length - 1) {
                    long min = Math.min(costs[0], costs[1]);
                    if (min == costs[0]) {
                        minIndex = 0;
                    }
                } else {
                    long min = Math.min(Math.min(costs[0], costs[1]), costs[2]);
                    if (min == costs[0]) {
                        minIndex = 0;
                    } else if (min == costs[2]) {
                        minIndex = 2;
                    }
                }

                costMatrix[x][y] += costs[minIndex];
                seamsDirection[x][y] = minIndex - 1;
            }
        });
        logger.log("costMatrixCalc finish");

        setForEachWidth(inWidth);
    }

    public long calcAvgRGBVal () {
        forEach((y, x) -> {
            int greyColor;
            Color c = new Color(greyScaledImage.getRGB(x, y));
            greyColor = c.getRed();
            this.avgRGBValue += greyColor;
        });
        int numOfPixels = inWidth * inHeight;
        long avgValue = avgRGBValue / numOfPixels;

        return avgValue;
    }

    public int[] findSeam () {
        //recalculate cost matrix
        costMatrixCalc();

        logger.log("findSeam start");
        int[] curSeam = new int[inHeight];
        if (k == numOfSeams) {
            System.out.println("Calculating more seams than requested!");
        }

        int minCol = -1;
        long min = Integer.MAX_VALUE;
        for (int x = 0; x < costMatrix.length; x++) {
            if (costMatrix[x][inHeight - 1] < min) {
                minCol = x;
                min = costMatrix[minCol][inHeight - 1];
            }
        }
        curSeam[inHeight - 1] = minCol;

        for (int y = inHeight - 2; y >= 0; y--) {
            int x = curSeam[y + 1];
            curSeam[y] = x + seamsDirection[x][y+1];
        }

        // Convert from costMatrix indices to original indices
        for (int y = 0; y < curSeam.length; y++) {
        	int x = curSeam[y];
            curSeam[y] = indexMap[x][y];
            seamsMask[curSeam[y]][y]=true;  

            shiftIndexMapLeft(x, y);

        }
        
        this.k++;
        return curSeam;
    }

    private void shiftIndexMapLeft (int x, int y) {
        int originalX = indexMap[x][y];
        for (int i = x; i < indexMap.length - 1; i++) {
            indexMap[i][y] = indexMap[i + 1][y];
            
        }

        indexMap[indexMap.length - 1][y] = originalX;

    }

    private void initialIndexMap () {
        logger.log("initialIndexMap start");

        forEach((y, x) -> {
                    this.indexMap[x][y] = x;
                }
        );
        logger.log("initialIndexMap finish");

    }
    
    private int[][] findAllSeams () {
        logger.log("findAllSeams start");
        int[][] seamMatrix = new int[numOfSeams][inHeight];
        for (int i = 0; i < numOfSeams; i++) {
            seamMatrix[i] = findSeam();
        }
        logger.log("findAllSeams finish");
        
        return seamMatrix;
    }
    
    
}